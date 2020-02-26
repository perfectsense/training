package brightspot.core.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

@Recordable.Embedded
public abstract class Filter extends Record {

    @ToolUi.Note("Excludes this filter if 'Type Filter' is enabled and the search is not filtered by all types.")
    private Boolean excludeFromAllTypes;

    @ToolUi.Placeholder(dynamicText = "${content.defaultHeading}", editable = true)
    private String heading;

    @ToolUi.Hidden
    private String parameterName;

    public boolean isExcludeFromAllTypes() {
        return Boolean.TRUE.equals(excludeFromAllTypes);
    }

    public void setExcludeFromAllTypes(boolean excludeFromAllTypes) {
        this.excludeFromAllTypes = excludeFromAllTypes ? Boolean.TRUE : null;
    }

    public String getHeading() {
        return !StringUtils.isBlank(heading) ? heading : getDefaultHeading();
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public abstract String getDefaultHeading();

    public abstract void updateQuery(SiteSearchViewModel search, Query<?> query, Set<String> values);

    public abstract List<FilterItem> getFilterItems(SiteSearchViewModel search, Set<String> values);

    protected boolean isSelected(Set<String> values, String value) {
        return values != null && values.contains(value);
    }

    protected String createUrl(SiteSearchViewModel search, Set<String> values, String value) {
        // Remove any current page parameters. Will remove in case if it is an additional parameter or the first parameter
        String url = StringUtils.addQueryParameters(search.getPath(), SiteSearchViewModel.PAGE_NUMBER_PARAMETER, null);

        // If the value being selected is already a part of the current filtered IDs, remove the ID. Otherwise add it.
        Set<String> ids = new HashSet<>(StringUtils.getQueryParameterValues(url, getParameterName()));

        if (isSelected(ids, value)) {
            ids.remove(value);
        } else {
            ids.add(value);
        }

        return StringUtils.addQueryParameters(url, getParameterName(), ids);
    }

    @Override
    public String getLabel() {
        return getHeading();
    }
}
