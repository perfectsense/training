package brightspot.core.update;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;

public interface LastUpdatedProvider extends Recordable {

    Date getLastUpdated();

    static Date getMostRecentUpdateDate(Object object) {

        State state = State.getInstance(object);

        LastUpdatedStateTrigger trigger = new LastUpdatedStateTrigger();

        state.fireTrigger(trigger);

        List<Date> lastUpdatedValues = trigger.getLastUpdatedValues();

        if (trigger.getRunCount() > 0) {
            if (lastUpdatedValues == null || lastUpdatedValues.isEmpty()) {
                return null;
            }

            Collections.sort(lastUpdatedValues, Comparator.reverseOrder());

            return lastUpdatedValues.get(0);
        }

        return state.as(Content.ObjectModification.class).getUpdateDate();
    }
}
