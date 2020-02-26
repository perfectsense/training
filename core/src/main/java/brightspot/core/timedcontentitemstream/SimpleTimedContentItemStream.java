package brightspot.core.timedcontentitemstream;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import brightspot.core.listmodule.AbstractListItemStream;
import brightspot.core.playlist.PlaylistItem;
import brightspot.core.playlist.PlaylistItemStream;
import brightspot.core.timed.TimedContent;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Basic")
public class SimpleTimedContentItemStream extends AbstractListItemStream implements
    Interchangeable,
    PlaylistItemStream,
    PlaylistModuleItemStream,
    TimedContentItemStream {

    @ToolUi.Unlabeled
    private List<TimedContent> items;

    @Override
    public List<TimedContent> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    @Override
    public List<TimedContent> getItems(Site site, Object mainObject, long offset, int limit) {

        long toIndex = limit + offset;
        long count = getCount(site, mainObject);

        if (toIndex >= count) {
            toIndex = count;
        }

        return getItems().subList(Math.toIntExact(offset), Math.toIntExact(toIndex));
    }

    @Override
    public int getItemsPerPage(Site site, Object mainObject) {
        return getItems().size();
    }

    @Override
    public boolean loadTo(Object newObj) {

        if (newObj instanceof AdvancedTimedContentItemStream) {

            ((AdvancedTimedContentItemStream) newObj).getItems().addAll(getPlaylistItems());
            return true;

        } else if (newObj instanceof DynamicTimedContentItemStream) {

            ((DynamicTimedContentItemStream) newObj).getPinnedItems().addAll(getPlaylistItems());
            return true;
        }
        return false;
    }

    @Override
    public List<TimedContent> getTimedContent() {
        return getItems();
    }

    private List<PlaylistItem> getPlaylistItems() {
        return getItems().stream()
            .map(timedContent -> {
                PlaylistItem playlistItem = new PlaylistItem();
                playlistItem.setTimedContent(timedContent);
                return playlistItem;
            })
            .collect(Collectors.toList());
    }

    @Override
    public Long getTimedContentItemStreamDuration() {
        return getItems().stream()
            .map(TimedContentItem::getTimedContentItemDuration)
            .filter(Objects::nonNull)
            .mapToLong(l -> l)
            .filter(duration -> duration >= 0)
            .sum();
    }
}
