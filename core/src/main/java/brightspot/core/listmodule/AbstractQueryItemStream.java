package brightspot.core.listmodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import brightspot.core.module.ModuleType;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class AbstractQueryItemStream extends Record implements ItemStream {

    public abstract Query<?> getQuery(Site site, Object mainObject);

    /**
     * The items that should be rendered on the page.
     */
    public abstract List<?> getPinnedItems();

    /**
     * The underlying assets, not modules, for the purpose of deduping and excluding pinned assets.
     */
    protected abstract Collection<?> getPinnedAssets();

    @Override
    public List<?> getItems(Site site, Object mainObject, long offset, int limit) {

        List<Object> items = new ArrayList<>();
        List<?> pinned = getPinnedItems();
        int pinnedCount = 0;

        // find pinned items within the specified range
        if (pinned != null) {
            pinnedCount = pinned.size();
            for (int i = Math.toIntExact(offset); i < (limit + offset) && i < pinnedCount; i++) {
                items.add(pinned.get(i));
            }
        }

        int itemCount = items.size();
        int queryLimit = limit - itemCount;
        long queryOffset = offset - (pinnedCount - itemCount);

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

        return items;
    }

    @Override
    public long getCount(Site site, Object mainObject) {

        long count = 0L;

        Collection<?> pinned = getPinnedAssets();
        if (pinned != null) {
            count += pinned.size();
        }

        count += getQuery(site, mainObject).count();

        return count;
    }

    @Override
    public boolean hasMoreThan(Site site, Object mainObject, long count) {

        int pinnedCount = Math.toIntExact(Optional.ofNullable(getPinnedItems())
            .map(Collection::stream)
            .orElse(Stream.empty())
            .filter(e -> !(e instanceof DynamicResult))
            .count());

        Query<?> query = getQuery(site, mainObject).clone();
        query.setSorters(null);
        return count < pinnedCount
            || query.hasMoreThan(Math.max(0, count - pinnedCount));
    }

    protected Predicate excludePinnedPredicate() {
        Collection<Object> pinnedAssets = new HashSet<>();
        getPinnedAssets().forEach(asset -> {
            if (asset instanceof ModuleType) {
                pinnedAssets.addAll(((ModuleType) asset).getModuleTypeContentIds());
            } else {
                pinnedAssets.add(asset);
            }
        });

        return !pinnedAssets.isEmpty()
            ? PredicateParser.Static.parse("_id != ?", pinnedAssets)
            : null;
    }
}
