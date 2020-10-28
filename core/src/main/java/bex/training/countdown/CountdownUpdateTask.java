package bex.training.countdown;

import java.io.IOException;

import bex.training.util.NetworkUtils;
import brightspot.core.tool.TaskUtils;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.RepeatingTask;
import com.psddev.dari.util.Settings;
import com.psddev.dari.util.StringUtils;
import okhttp3.Request;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountdownUpdateTask extends RepeatingTask {

    private static final String API_TOKEN_SETTING = "countdown/apiToken";
    private static final String ENDPOINT_URL_SETTING = "countdown/endpointUrl";
    private static final String TASK_EXECUTOR = "Countdown Update";

    private static final Logger LOGGER = LoggerFactory.getLogger(CountdownUpdateTask.class);

    public CountdownUpdateTask() {
        super(TASK_EXECUTOR, null);
    }

    @Override
    protected DateTime calculateRunTime(DateTime dateTime) {
        return everyHour(dateTime);
    }

    @Override
    protected void doRepeatingTask(DateTime dateTime) {
        if (!TaskUtils.isRunningOnTaskHost()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Not running on task host; aborting...");
            }
            return;
        }

        updateCountdowns();
    }

    public static void updateCountdowns() {

        String endpointUrl = Settings.getOrError(String.class, ENDPOINT_URL_SETTING, "Countdown URL not configured!");
        String apiToken = Settings.getOrError(String.class, API_TOKEN_SETTING, "Countdown API token not configured!");

        Query<Countdown> query = Query.from(Countdown.class)
                .where("countdownId != missing")
                .noCache();

        Database db = Database.Static.getDefault();

        try {
            db.beginWrites();

            for (Countdown countdown : query.iterable(0)) {
                try {

                    Request request = new Request.Builder()
                            .header("Authorization", "Bearer" + apiToken)
                            .url(StringUtils.addQueryParameters(endpointUrl,
                                    "id", countdown.getCountdownId()))
                            .build();

                    Double shape = NetworkUtils.request(
                            request,
                            body -> ObjectUtils.to(
                                    Double.class,
                                    CollectionUtils.getByPath(ObjectUtils.fromJson(body.string()), "shape")));

                    countdown.setGammaDistributionShape(shape);

                    countdown.save();

                } catch (IOException e) {
                    LOGGER.error("Failed to fetch data for {} [{}]", countdown.getName(), countdown.getId(), e);

                } catch (RuntimeException e) {
                    LOGGER.error("Failed to save {} [{}]", countdown.getName(), countdown.getId(), e);
                }
            }

            db.commitWrites();

        } finally {
            db.endWrites();
        }
    }
}
