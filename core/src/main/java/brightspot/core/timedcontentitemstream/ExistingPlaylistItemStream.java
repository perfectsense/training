package brightspot.core.timedcontentitemstream;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import brightspot.core.playlist.Playlist;
import brightspot.core.playlist.PlaylistItemStream;
import brightspot.core.timed.TimedContent;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
@Recordable.DisplayName("Use Existing Playlist")
public class ExistingPlaylistItemStream extends Record implements PlaylistModuleItemStream {

    @Required
    private Playlist playlist;

    @Override
    public List<? extends TimedContentItem> getItems(Site site, Object mainObject, long offset, int limit) {
        return getPlaylistItemStream()
            .map(playlistItemStream -> playlistItemStream.getItems(site, mainObject, offset, limit))
            .orElse(Collections.emptyList());
    }

    @Override
    public long getCount(Site site, Object mainObject) {
        return getPlaylistItemStream()
            .map(playlistItemStream -> playlistItemStream.getCount(site, mainObject))
            .orElse(0L);
    }

    @Override
    public boolean hasMoreThan(Site site, Object mainObject, long count) {
        return getPlaylistItemStream()
            .map(playlistItemStream -> playlistItemStream.hasMoreThan(site, mainObject, count))
            .orElse(false);
    }

    @Override
    public int getItemsPerPage(Site site, Object mainObject) {
        return getPlaylistItemStream()
            .map(playlistItemStream -> playlistItemStream.getItemsPerPage(site, mainObject))
            .orElse(0);
    }

    @Override
    public List<TimedContent> getTimedContent() {
        return getPlaylistItemStream()
            .map(TimedContentItemStream::getTimedContent)
            .orElse(Collections.emptyList());
    }

    @Override
    public Long getTimedContentItemStreamDuration() {
        return getPlaylistItemStream()
            .map(TimedContentItemStream::getTimedContentItemStreamDuration)
            .orElse(0L);
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    private Optional<PlaylistItemStream> getPlaylistItemStream() {
        return Optional.ofNullable(getPlaylist())
            .map(Playlist::getItemStream);
    }
}
