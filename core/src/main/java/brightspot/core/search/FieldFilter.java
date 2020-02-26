package brightspot.core.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Grouping;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

public abstract class FieldFilter extends Filter {

    private static final String DEFAULT_ALL_LABEL = "All";

    @ToolUi.Placeholder(value = DEFAULT_ALL_LABEL, editable = true)
    private String allLabel;

    public String getAllLabel() {
        return ObjectUtils.firstNonBlank(allLabel, DEFAULT_ALL_LABEL);
    }

    public void setAllLabel(String allLabel) {
        this.allLabel = allLabel;
    }

    public abstract String getItemLabel(Object object);

    public abstract String getField();

    /**
     * Returns {@code true} if a result can have no more than one possible value for this field. Used to determine how
     * to measure total counts in FieldFilter#getFilterItems (so this method is currently irrelevant if that method is
     * overridden).
     */
    public boolean hasMutuallyExclusiveValues() {
        return true;
    }

    @Override
    public void updateQuery(SiteSearchViewModel search, Query<?> query, Set<String> values) {
        query.and(getField() + " = ?", values);
    }

    protected boolean shouldIncludeFilterItem(Object object) {
        return true;
    }

    @Override
    public List<FilterItem> getFilterItems(SiteSearchViewModel search, Set<String> values) {
        Query<?> query = search.createQueryWithoutFilter(this);
        List<FilterItem> items = new ArrayList<>();
        long total = 0;

        for (Grouping<?> grouping : query.groupBy(getField())) {
            Object key = grouping.getKeys().get(0);
            State state = null;

            if (key instanceof Recordable) {
                state = ((Recordable) key).getState();

            } else {
                UUID id = ObjectUtils.to(UUID.class, key);

                if (id != null) {
                    Object object = Query.fromAll().where("_id = ?", id).first();

                    if (object != null) {
                        state = State.getInstance(object);
                    }
                }
            }

            if (state != null) {
                Object originalObject = state.getOriginalObjectOrNull();
                String itemLabel = getItemLabel(originalObject);

                if (shouldIncludeFilterItem(originalObject) && itemLabel != null) {
                    String idString = state.getId().toString();
                    long count = grouping.getCount();
                    total += count;

                    items.add(new FilterItem(
                        itemLabel,
                        isSelected(values, idString),
                        createUrl(search, values, idString),
                        count,
                        idString));
                }
            }
        }

        // sort before adding 'All' filter so it will always end up first in the list
        items.sort(Comparator.comparing(FilterItem::getLabel, Comparator.nullsLast(Comparator.naturalOrder())));

        // 'All' filter
        // not needed if no other filterItems; skip if so, as getting total count can be costly
        if (items.size() > 1) {
            items.add(0, new FilterItem(
                getAllLabel(),
                values == null,
                StringUtils.addQueryParameters(
                    search.getPath(),
                    getParameterName(),
                    null),
                hasMutuallyExclusiveValues() ? total : search.createQueryWithoutFilter(this).count()));
        }

        return items;
    }
}
