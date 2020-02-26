package brightspot.core.tool;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.psddev.dari.db.Database;
import com.psddev.dari.db.DatabaseException;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.RepeatingTask;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ingests and processes {{@link TaxonRecalculation}}s, then deletes them. Runs every minute.
 */
public class TaxonRecalculateTask extends RepeatingTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxonRecalculateTask.class);

    @Override
    protected DateTime calculateRunTime(DateTime currentTime) {
        if (TaskUtils.isRunningOnTaskHost()) {
            return everyMinute(currentTime);
        }
        // Never runs because it will never get there.
        return new DateTime().plusHours(1);
    }

    @Override
    protected void doRepeatingTask(DateTime runTime) throws Exception {

        Set<TaxonRecalculation> processed = new HashSet<>();
        Set<Recordable> recalculated = new HashSet<>();
        try {
            for (TaxonRecalculation recalculation : Query.from(TaxonRecalculation.class)
                .where("calculationSaveDate < ?", Database.Static.getDefault().now())
                .iterable(0)) {

                try {
                    Query<Object> query = recalculation.getAncestorQuery();
                    // Unset sort to avoid leaking database resources
                    query.setSorters(Collections.emptyList());
                    query.iterable(0)
                        .forEach(ancestor -> {
                            if (ancestor instanceof Recordable && recalculated.add((Recordable) ancestor)) {
                                State state = State.getInstance(ancestor);
                                if (state != null) {
                                    state.getType()
                                        .getMethod(recalculation.getMethodName())
                                        .recalculate(state);
                                }
                            }
                        });
                    processed.add(recalculation);
                } catch (RuntimeException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        } finally {
            processed.forEach(record -> {
                try {
                    record.delete();
                } catch (DatabaseException e) {
                    LOGGER.warn(e.getMessage(), e);
                }
            });
        }
    }
}
