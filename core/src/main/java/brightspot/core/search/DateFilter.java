package brightspot.core.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.CompoundPredicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

public class DateFilter extends Filter {

    private static final String DEFAULT_ALL_LABEL = "All";

    @ToolUi.Placeholder(value = DEFAULT_ALL_LABEL, editable = true)
    private String allLabel;

    private List<DateRange> dateRanges;

    public String getAllLabel() {
        return ObjectUtils.firstNonBlank(allLabel, DEFAULT_ALL_LABEL);
    }

    public void setAllLabel(String allLabel) {
        this.allLabel = allLabel;
    }

    public List<DateRange> getDateRanges() {
        if (dateRanges == null) {
            dateRanges = new ArrayList<>();
        }
        return dateRanges;
    }

    public void setDateRanges(List<DateRange> dateRanges) {
        this.dateRanges = dateRanges;
    }

    @Override
    public String getDefaultHeading() {
        return "Date";
    }

    @Override
    public void updateQuery(SiteSearchViewModel search, Query<?> query, Set<String> values) {
        values.stream()
            .flatMap(v -> getDateRanges().stream().filter(dr -> dr.getParameterValue().equals(v)))
            .map(DateRange::toPredicate)
            .reduce((x, y) -> CompoundPredicate.combine(PredicateParser.OR_OPERATOR, x, y))
            .ifPresent(query::and);
    }

    @Override
    public List<FilterItem> getFilterItems(SiteSearchViewModel search, Set<String> values) {
        List<FilterItem> items = new ArrayList<>();

        for (DateRange dateRange : getDateRanges()) {
            long count = search
                .createQueryWithoutFilter(this)
                .and(dateRange.toPredicate())
                .count();

            if (count > 0) {
                String value = dateRange.getParameterValue();

                items.add(new FilterItem(
                    dateRange.getLabel(),
                    isSelected(values, value),
                    createUrl(search, values, value),
                    count,
                    value));
            }
        }

        // 'All' filter
        // not needed if no other filterItems; skip if so, as getting total count is costly
        if (items.size() > 0) {
            items.add(0, new FilterItem(
                getAllLabel(),
                values == null,
                StringUtils.addQueryParameters(
                    search.getPath(),
                    getParameterName(),
                    null),
                search.createQueryWithoutFilter(this).count()));
        }

        return items;
    }

    @Override
    protected void beforeCommit() {
        IntStream.range(0, getDateRanges().size())
            .forEach(i -> getDateRanges().get(i).setParameterValue(String.valueOf(i)));
    }
}
