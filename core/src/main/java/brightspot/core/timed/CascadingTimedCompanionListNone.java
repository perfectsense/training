package brightspot.core.timed;

import java.util.List;

import brightspot.core.cascading.listmerge.ListMergeStrategy;
import com.psddev.dari.db.Recordable;

/**
 * A timed companion cascading list that is always empty.
 */
@Recordable.DisplayName("None")
public class CascadingTimedCompanionListNone extends CascadingTimedCompanionList {

    @Override
    public List<TimedCompanion> getItems() {
        return null;
    }

    @Override
    protected ListMergeStrategy<TimedCompanion> getMergeStrategy() {
        return null;
    }
}
