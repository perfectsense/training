package brightspot.core.timedcontentitemstream;

import java.util.List;
import java.util.stream.Collectors;

import brightspot.core.listmodule.AbstractListItemStream;
import brightspot.core.playlist.PlaylistItem;
import brightspot.core.playlist.PlaylistItemList;
import brightspot.core.playlist.PlaylistItemStream;
import brightspot.core.timed.TimedContent;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Advanced")
public class AdvancedTimedContentItemStream extends AbstractListItemStream implements
    Interchangeable,
    PlaylistItemStream,
    PlaylistModuleItemStream,
    TimedContentItemStream {

    @ToolUi.Unlabeled
    @Embedded
    @Required
    private PlaylistItemList items = new PlaylistItemList();

    @Override
    public List<PlaylistItem> getItems() {

        return getPlaylistItemList().getItems();
    }

    public void setItems(List<PlaylistItem> pinnedItems) {

        getPlaylistItemList().setItems(pinnedItems);
    }

    private PlaylistItemList getPlaylistItemList() {
        if (this.items == null) {

            this.items = new PlaylistItemList();
        }
        return items;
    }

    @Override
    public List<PlaylistItem> getItems(Site site, Object mainObject, long offset, int limit) {

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
        if (newObj instanceof DynamicTimedContentItemStream) {
            ((DynamicTimedContentItemStream) newObj).getPinnedItems().addAll(getItems());
            return true;
        }
        if (newObj instanceof SimpleTimedContentItemStream) {
            ((SimpleTimedContentItemStream) newObj).getItems().addAll(getTimedContent());

            return true;
        }
        return false;
    }

    @Override
    public List<TimedContent> getTimedContent() {
        return getItems().stream()
            .map(PlaylistItem::getTimedContent)
            .collect(Collectors.toList());
    }

    @Override
    public Long getTimedContentItemStreamDuration() {
        return getPlaylistItemList().getDuration();
    }
}
