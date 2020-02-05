package brightspot.core.timedcontentitemstream;

import brightspot.core.tool.DefaultImplementationSupplier;

/**
 * Interface added as intermediary for the purposes of using an existing {@link brightspot.core.playlist.Playlist} when
 * creating some content that encompasses a playlist using {@link brightspot.core.playlist.PlaylistModule}
 */
public interface PlaylistModuleItemStream extends TimedContentItemStream {

    static PlaylistModuleItemStream createDefault() {
        return DefaultImplementationSupplier.createDefault(
            PlaylistModuleItemStream.class,
            SimpleTimedContentItemStream.class);
    }

    static PlaylistModuleItemStream createDynamic() {
        return new DynamicTimedContentItemStream();
    }
}
