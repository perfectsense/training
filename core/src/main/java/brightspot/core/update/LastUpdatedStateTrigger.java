package brightspot.core.update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.psddev.dari.db.Trigger;

public class LastUpdatedStateTrigger implements Trigger {

    private int runCount;

    private List<Date> lastUpdatedValues;

    @Override
    public void execute(Object object) {

        if (object instanceof LastUpdatedProvider) {

            runCount += 1;

            Date lastUpdated = ((LastUpdatedProvider) object).getLastUpdated();

            if (lastUpdated != null) {

                getLastUpdatedValues().add(lastUpdated);
            }
        }
    }

    public int getRunCount() {
        return runCount;
    }

    public List<Date> getLastUpdatedValues() {
        if (lastUpdatedValues == null) {
            lastUpdatedValues = new ArrayList<>();
        }
        return lastUpdatedValues;
    }
}
