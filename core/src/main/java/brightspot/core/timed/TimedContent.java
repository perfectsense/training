package brightspot.core.timed;

import brightspot.core.timedcontentitemstream.TimedContentItem;

/**
 * Content that has the concept of time and an explicit duration that can be played with an appropriate player; usually
 * audio or video based content.
 */
public interface TimedContent extends TimedContentItem {

    String INTERNAL_NAME = "brightspot.core.timed.TimedContent";
    String METADATA_TAB = "Metadata";

    /**
     * @return The duration of this timed content in milliseconds. If the duration is unknown, null is returned.
     */
    Long getTimedContentDuration();

    /**
     * @return A player suitable for previewing timed content in the CMS.
     */
    TimedContentToolPlayer getTimedContentToolPlayer();

    default TimedContentData asTimedContentData() {
        return as(TimedContentData.class);
    }
}
