package brightspot.core.listmodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;

import brightspot.core.module.ModuleType;
import brightspot.core.promo.Promo;
import brightspot.core.promo.Promotable;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.tool.AuthenticationFilter;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PageContextFilter;

@Recordable.DisplayName("Dynamic")
public class DynamicItemStream extends AbstractQueryItemStream implements
    Interchangeable,
    ListModuleItemStream {

    private static final int DEFAULT_ITEMS_PER_PAGE = 10;

    @ToolUi.DropDown
    @Where("groups = " + Promotable.INTERNAL_NAME + " and isAbstract = false")
    private Set<ObjectType> types;

    @ToolUi.Placeholder("" + DEFAULT_ITEMS_PER_PAGE)
    private int itemsPerPage = DEFAULT_ITEMS_PER_PAGE;

    @Required
    @Embedded
    @ToolUi.DisplayLast
    private DynamicQuerySort sort = DynamicQuerySort.createDefault();

    @ToolUi.DisplayLast
    @Embedded
    @ToolUi.AddToTop
    private List<DynamicPin> pinnedItems = IntStream
        .range(0, itemsPerPage)
        .mapToObj(i -> new DynamicResult())
        .collect(Collectors.toList());

    @ToolUi.DisplayLast
    @ToolUi.Note("This renders if no dynamic items are found.")
    private ModuleType noResultsModule;

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
    public List<DynamicPin> getPinnedItems() {
        if (pinnedItems == null) {
            pinnedItems = new ArrayList<>();
        }
        return pinnedItems;
    }

    public void setPinnedItems(List<DynamicPin> pinnedItems) {
        this.pinnedItems = pinnedItems;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    @Override
    protected Collection<?> getPinnedAssets() {
        return Optional.ofNullable(getPinnedItems()
            .stream()
            .filter(pin -> pin instanceof ModuleType)
            .map(pin -> (ModuleType) pin)
            .collect(Collectors.toList()))
            .orElse(new ArrayList<>());
    }

    /**
     * Returns the {@code module} returned when the {@code item stream} returns no results.
     *
     * @return a {@link ModuleType} (optional).
     */
    @Override
    public ModuleType getNoResultsModule() {
        return noResultsModule;
    }

    /**
     * Sets the {@code module} returned when the {@code item stream} returns no results.
     *
     * @param noResultsModule a {@link ModuleType} (optional).
     */
    public void setNoResultsModule(ModuleType noResultsModule) {
        this.noResultsModule = noResultsModule;
    }

    @Override
    public Query<Promotable> getQuery(Site site, Object mainObject) {

        // Base query
        Query<Promotable> query = Query.from(Promotable.class);

        // limit query to items accessible by site
        if (site != null) {

            query.where(site.itemsPredicate());
        }

        // Restrict types
        Set<ObjectType> types = getTypes();
        if (!types.isEmpty()) {
            query.where("_type = ?", types);
        }

        // Restrict to only Objects with a permalink
        query.and(Directory.Static.hasPathPredicate());

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
    public int getItemsPerPage(Site site, Object mainObject) {
        int itemsPerPage = getItemsPerPage();
        return itemsPerPage > 0
            ? itemsPerPage
            : 1;
    }

    @Override
    public boolean loadTo(Object newObj) {
        if (newObj instanceof AdvancedItemStream) {
            for (DynamicPin m : getPinnedItems()) {
                if (m instanceof ModuleType) {
                    ((AdvancedItemStream) newObj).getItems().add((ModuleType) m);
                }
            }
            return true;
        }
        if (newObj instanceof SimpleItemStream) {
            for (DynamicPin p : getPinnedItems()) {
                if (p instanceof Promo) {
                    if (((Promo) p).getPromotable().isPresent()) {
                        ((SimpleItemStream) newObj).getItems().add(((Promo) p).getPromotable().get());
                    }
                }
            }
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
    public void beforeSave() {
        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();

        if (request != null) {
            ToolUser user = AuthenticationFilter.Static.getUser(request);

            if (user != null) {
                Object mainObject = request.getAttribute("containerObject");

                if (mainObject != null) {
                    int size = pinnedItems.size();
                    List<?> items = getItems(user.getCurrentSetSite(), mainObject, 0, size);
                    size = Math.min(size, items.size());

                    for (int i = 0; i < size; ++i) {
                        DynamicPin pin = pinnedItems.get(i);

                        if (pin instanceof DynamicResult) {
                            ((DynamicResult) pin).setItem(items.get(i));
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<?> getItems(Site site, Object mainObject, long offset, int limit) {
        List<Object> items = new ArrayList<>();
        List<DynamicPin> pins = getPinnedItems();
        HashMap<Integer, ModuleType> pinPoints = new HashMap<>();

        int alreadyPinned = Math.toIntExact(pins.subList(0, Math.min(Math.toIntExact(offset), pins.size()))
            .stream()
            .filter(i -> !(i instanceof DynamicResult))
            .count());

        // only execute if within pinnable indeces
        if (offset <= getPinnedItems().size()) {

            for (int i = Math.toIntExact(offset);
                i < Math.min(pins.size(), offset + limit); i++) {
                if (pins.get(i) instanceof ModuleType) {
                    pinPoints.put(i - Math.toIntExact(offset), (ModuleType) pins.get(i));
                }
            }

        }

        int queryLimit = limit - pinPoints.size();
        long queryOffset = offset - alreadyPinned;

        if (queryLimit > 0 && queryOffset >= 0) {
            Query<?> query = getQuery(site, mainObject);
            Predicate excludePinned = excludePinnedPredicate();
            if (excludePinned != null) {
                query.and(excludePinned);
            }
            items.addAll(query
                .select(queryOffset, queryLimit)
                .getItems());

        }

        if (!pinPoints.isEmpty()) {
            for (int index : pinPoints.keySet()) {
                items.listIterator(Math.min(Math.max(0, index), items.size()))
                    .add(pinPoints.get(index));
            }
        }

        return items;
    }
}
