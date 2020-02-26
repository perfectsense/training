package brightspot.core.timedcontentitemstream;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.core.listmodule.AbstractQueryItemStream;
import brightspot.core.listmodule.DynamicQueryModifier;
import brightspot.core.listmodule.DynamicQuerySort;
import brightspot.core.playlist.PlaylistItem;
import brightspot.core.playlist.PlaylistItemList;
import brightspot.core.playlist.PlaylistItemStream;
import brightspot.core.timed.TimedContent;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Dynamic")
public class DynamicTimedContentItemStream extends AbstractQueryItemStream implements
    Interchangeable,
    PlaylistItemStream,
    PlaylistModuleItemStream,
    TimedContentItemStream {

    private static final int DEFAULT_ITEMS_PER_PAGE = 10;

    @ToolUi.DropDown
    @Where("groups = " + TimedContent.INTERNAL_NAME + " and isAbstract = false")
    private Set<ObjectType> types;

    @ToolUi.Placeholder("" + DEFAULT_ITEMS_PER_PAGE)
    private int itemsPerPage = DEFAULT_ITEMS_PER_PAGE;

    @Embedded
    @ToolUi.DisplayLast
    private DynamicQuerySort sort = DynamicQuerySort.createDefault();

    @ToolUi.DisplayLast
    @Embedded
    @Required
    private PlaylistItemList pinnedItems = new PlaylistItemList();

    public Set<ObjectType> getTypes() {
        if (types == null) {
            types = new HashSet<>();
        }
        return types;
    }

    public void setTypes(Set<ObjectType> types) {
        this.types = types;
    }

    public DynamicQuerySort getSort() {
        return sort;
    }

    public void setSort(DynamicQuerySort sort) {
        this.sort = sort;
    }

    @Override
    public List<PlaylistItem> getPinnedItems() {

        if (pinnedItems == null) {

            pinnedItems = new PlaylistItemList();
        }

        return pinnedItems.getItems();
    }

    public void setPinnedItems(List<PlaylistItem> pinnedItems) {
        if (this.pinnedItems == null) {

            this.pinnedItems = new PlaylistItemList();
        }

        this.pinnedItems.setItems(pinnedItems);
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    @Override
    protected Collection<TimedContent> getPinnedAssets() {
        return getPinnedItems().stream()
            .map(PlaylistItem::getTimedContent)
            .collect(Collectors.toList());
    }

    @Override
    public Query<TimedContent> getQuery(Site site, Object mainObject) {

        // Base query
        Query<TimedContent> query = Query.from(TimedContent.class);

        // limit query to items accessible by site
        if (site != null) {

            query.where(site.itemsPredicate());
        }

        // Restrict types
        Set<ObjectType> types = getTypes();
        if (!types.isEmpty()) {
            query.where("_type = ?", types);
        }

        // Dedupe against pinned assets
        query.and("_id != ?", getPinnedAssets());

        // Consult all modifications to let them modify this query
        DynamicQueryModifier.updateQueryWithModifications(site, mainObject, this, query);

        // Use Solr for query
        query.and("* matches *");

        query.and("brightspot.core.promo.PromotableData/promotable.hideFromDynamicResults != true");

        // Sort
        Optional.ofNullable(getSort())
            .ifPresent(s -> s.updateQuery(site, mainObject, query));

        return query;
    }

    @Override
    public List<TimedContent> getTimedContent() {
        return Collections.emptyList();
    }

    @Override
    public List<TimedContentItem> getItems(Site site, Object mainObject, long offset, int limit) {
        return super.getItems(site, mainObject, offset, limit)
            .stream()
            .filter(TimedContentItem.class::isInstance)
            .map(TimedContentItem.class::cast)
            .collect(Collectors.toList());
    }

    @Override
    public int getItemsPerPage(Site site, Object mainObject) {
        int itemsPerPage = getItemsPerPage();
        return itemsPerPage > 0
            ? itemsPerPage
            : 1;
    }

    @Override
    public boolean loadTo(Object newObj) {

        if (newObj instanceof SimpleTimedContentItemStream) {
            ((SimpleTimedContentItemStream) newObj).getItems().addAll(getPinnedAssets());
            return true;

        } else if (newObj instanceof AdvancedTimedContentItemStream) {
            ((AdvancedTimedContentItemStream) newObj).getItems().addAll(getPinnedItems());
            return true;
        }

        return false;
    }

    @Override
    public String getLabel() {
        // Consult all modifications to create a label
        List<String> labels = DynamicQueryModifier.createLabels(this);
        if (labels.isEmpty()) {
            return super.getLabel();
        }
        return labels.stream()
            .collect(Collectors.joining(", "));
    }

    @Override
    public Long getTimedContentItemStreamDuration() {
        return null;
    }
}
