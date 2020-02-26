package brightspot.core.imageitemstream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.carousel.CarouselItemStream;
import brightspot.core.gallery.GalleryItemStream;
import brightspot.core.image.Image;
import brightspot.core.listmodule.AbstractQueryItemStream;
import brightspot.core.listmodule.DynamicQueryModifier;
import brightspot.core.listmodule.DynamicQuerySort;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Dynamic")
public class DynamicImageItemStream extends AbstractQueryItemStream implements
    CarouselItemStream,
    GalleryItemStream,
    ImageItemStream,
    Interchangeable {

    private static final int DEFAULT_ITEMS_PER_PAGE = 10;

    @ToolUi.Placeholder("" + DEFAULT_ITEMS_PER_PAGE)
    private int itemsPerPage = DEFAULT_ITEMS_PER_PAGE;

    @Required
    @Embedded
    @ToolUi.DisplayLast
    private DynamicQuerySort sort = DynamicQuerySort.createDefault();

    @ToolUi.DisplayLast
    @Embedded
    private List<ImageItemPromo> pinnedItems;

    public DynamicQuerySort getSort() {
        return sort;
    }

    public void setSort(DynamicQuerySort sort) {
        this.sort = sort;
    }

    @Override
    public List<ImageItemPromo> getPinnedItems() {
        if (pinnedItems == null) {
            pinnedItems = new ArrayList<>();
        }
        return pinnedItems;
    }

    public void setPinnedItems(List<ImageItemPromo> pinnedItems) {
        this.pinnedItems = pinnedItems;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    @Override
    protected Collection<Image> getPinnedAssets() {
        return getPinnedItems().stream()
            .map(ImageItemPromo::getImageItemImage)
            .collect(Collectors.toList());
    }

    @Override
    public Query<Image> getQuery(Site site, Object mainObject) {

        // Base query
        Query<Image> query = Query.from(Image.class);

        // limit query to items accessible by site
        if (site != null) {

            query.where(site.itemsPredicate());
        }

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
    public List<ImageItem> getItems(Site site, Object mainObject, long offset, int limit) {
        return super.getItems(site, mainObject, offset, limit)
            .stream()
            .filter(ImageItem.class::isInstance)
            .map(ImageItem.class::cast)
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

        if (newObj instanceof SimpleImageItemStream) {
            ((SimpleImageItemStream) newObj).getItems().addAll(getPinnedAssets());
            return true;

        } else if (newObj instanceof AdvancedImageItemStream) {
            ((AdvancedImageItemStream) newObj).getItems().addAll(getPinnedItems());
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
}
