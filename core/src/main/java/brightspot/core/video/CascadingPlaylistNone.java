package brightspot.core.video;

import brightspot.core.playlist.Playlist;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("None")
public class CascadingPlaylistNone extends CascadingPlaylist {

    @Override
    public Playlist get() {
        return null;
    }
}
