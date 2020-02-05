package brightspot.core.video;

import brightspot.core.cascading.Cascading;
import brightspot.core.playlist.Playlist;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class CascadingPlaylist extends Record implements Cascading<Playlist> {

}
