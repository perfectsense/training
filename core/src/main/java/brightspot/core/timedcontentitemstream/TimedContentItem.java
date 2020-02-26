package brightspot.core.timedcontentitemstream;

import brightspot.core.timed.TimedContent;
import com.psddev.dari.db.Recordable;

public interface TimedContentItem extends Recordable {

    /**
     * Returns the {@link TimedContent} of this {@link TimedContentItem}.
     *
     * @return a {@link TimedContent}.
     */
    TimedContent getTimedContentItemContent();

    /**
     * Returns the duration of the {@link TimedContentItem} (in milliseconds).
     *
     * @return a {@link Long} (optional).
     */
    Long getTimedContentItemDuration();
}
