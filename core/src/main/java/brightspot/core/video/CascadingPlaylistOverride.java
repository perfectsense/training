package brightspot.core.video;

import brightspot.core.playlist.Playlist;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Select")
public class CascadingPlaylistOverride extends CascadingPlaylist {

    @Required
    @ToolUi.Unlabeled
    private Playlist playlist;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public Playlist get() {
        return getPlaylist();
    }
}
