package brightspot.core.timedcontentitemstream;

import java.util.List;

import brightspot.core.listmodule.ItemStream;
import brightspot.core.timed.TimedContent;
import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.cms.db.Site;

public interface TimedContentItemStream extends ItemStream {

    @Override
    List<? extends TimedContentItem> getItems(Site site, Object mainObject, long offset, int limit);

    /**
     * Returns the timed content of the items in the itemstream for containment queries.
     * <p/>
     * <strong>Note:</strong> For internal use only!
     *
     * @return a {@link List} of {@link TimedContent}.
     */
    List<TimedContent> getTimedContent();

    /**
     * Returns the duration of the {@link TimedContentItemStream} (in milliseconds).
     *
     * @return a {@link Long} (optional).
     */
    Long getTimedContentItemStreamDuration();

    static TimedContentItemStream createDefault() {
        return DefaultImplementationSupplier
            .createDefault(TimedContentItemStream.class, SimpleTimedContentItemStream.class);
    }

    static TimedContentItemStream createDynamic() {
        return new DynamicTimedContentItemStream();
    }

}
