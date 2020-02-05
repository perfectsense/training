package brightspot.core.listmodule;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * @deprecated Use {@link brightspot.core.playlist.PlaylistModule} instead.
 */
@Deprecated
@Recordable.DisplayName("Shared")
public class SharedItemStream extends Record implements ListModuleItemStream {

    @Required
    private ItemStreamContainer content;

    public ItemStreamContainer getContent() {
        return content;
    }

    public void setContent(ItemStreamContainer content) {
        this.content = content;
    }

    @Override
    public List<?> getItems(Site site, Object mainObject, long offset, int limit) {
        return getItemStream()
            .map(itemStream -> itemStream.getItems(site, mainObject, offset, limit))
            .orElse(Collections.emptyList());
    }

    @Override
    public long getCount(Site site, Object mainObject) {
        return getItemStream()
            .map(itemStream -> itemStream.getCount(site, mainObject))
            .orElse(0L);
    }

    @Override
    public boolean hasMoreThan(Site site, Object mainObject, long count) {
        return getItemStream()
            .map(itemStream -> itemStream.hasMoreThan(site, mainObject, count))
            .orElse(false);
    }

    @Override
    public int getItemsPerPage(Site site, Object mainObject) {
        return getItemStream()
            .map(itemStream -> itemStream.getItemsPerPage(site, mainObject))
            .orElse(0);
    }

    private Optional<ItemStream> getItemStream() {

        return Optional.ofNullable(content)
            .map(ItemStreamContainer::getItemStream);
    }
}
