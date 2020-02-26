package brightspot.core.playlist;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.timedcontentitemstream.AdvancedTimedContentItemStream;
import com.psddev.dari.db.StateValueAdapter;
import com.psddev.dari.util.ObjectUtils;

public class ListOfPlaylistItemToPlaylistItemStream
    implements StateValueAdapter<List<PlaylistItem>, PlaylistItemStream> {

    @Override
    public PlaylistItemStream adapt(List<PlaylistItem> source) {

        AdvancedTimedContentItemStream itemStream = new AdvancedTimedContentItemStream();

        if (!ObjectUtils.isBlank(source)) {

            itemStream.setItems(new ArrayList<>(source));
        }

        return itemStream;
    }

    public static ListOfPlaylistItemToPlaylistItemStream adapter() {
        return new ListOfPlaylistItemToPlaylistItemStream();
    }
}
