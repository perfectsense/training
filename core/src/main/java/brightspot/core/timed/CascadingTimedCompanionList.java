package brightspot.core.timed;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.cascading.CascadingList;

/**
 * Base class for timed companion cascading list implementations.
 */
public abstract class CascadingTimedCompanionList extends CascadingList<TimedCompanion>
    implements TimedContentProvider {

    private transient TimedContent timedContent;

    @Override
    public List<TimedCompanion> getItems() {
        return null;
    }

    public void updateItems(TimedContent timedContent) {
        this.timedContent = timedContent;

        List<TimedCompanion> items = getItems();
        if (items != null) {
            items.forEach(tc -> tc.setTimedContent(timedContent));
        }
    }

    @Override
    public TimedContent getTimedContent() {
        return timedContent;
    }

    public static List<TimedCompanion> getItemsMerged(
        TimedContentProvider timedContentProvider,
        CascadingTimedCompanionList timedCompanionList) {

        List<TimedCompanion> merged;

        TimedContent timedContent = timedContentProvider != null ? timedContentProvider.getTimedContent() : null;
        List<TimedCompanion> existing;

        if (timedContent != null) {
            existing = timedContent.as(TimedContentData.class).getTimedCompanions();
        } else {
            existing = new ArrayList<>();
        }

        if (timedCompanionList != null) {
            timedCompanionList.merge(existing);
            merged = timedCompanionList.get();

        } else {
            merged = existing;
        }

        TimedCompanion.sortList(merged);

        return merged;
    }
}
