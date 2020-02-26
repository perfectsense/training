package brightspot.core.playlist;

import brightspot.core.timedcontentitemstream.DynamicTimedContentItemStream;
import brightspot.core.timedcontentitemstream.SimpleTimedContentItemStream;
import brightspot.core.timedcontentitemstream.TimedContentItemStream;
import brightspot.core.tool.DefaultImplementationSupplier;

public interface PlaylistItemStream extends TimedContentItemStream {

    static PlaylistItemStream createDefault() {
        return DefaultImplementationSupplier.createDefault(
            PlaylistItemStream.class,
            SimpleTimedContentItemStream.class);
    }

    static PlaylistItemStream createDynamic() {
        return new DynamicTimedContentItemStream();
    }

}
