package brightspot.core.search;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

/**
 * Adds fields which modify the behavior of an object in {@link SiteSearch}.
 */
@Recordable.FieldInternalNamePrefix(SiteSearchableData.FIELD_INTERNAL_NAME_PREFIX)
public class SiteSearchableData extends Modification<Object> {

    public static final String FIELD_INTERNAL_NAME_PREFIX = "siteSearchable.";
    private static final String OVERRIDES_TAB_TITLE = "Overrides";

    @Indexed
    @ToolUi.Cluster("Search")
    @ToolUi.Hidden(false)
    @ToolUi.Tab(OVERRIDES_TAB_TITLE)
    private Boolean hideFromSearchResults;

    public boolean isHideFromSearchResults() {
        return Boolean.TRUE.equals(hideFromSearchResults);
    }

    public void setHideFromSearchResults(boolean hideFromSearchResults) {
        this.hideFromSearchResults = hideFromSearchResults ? Boolean.TRUE : null;
    }
}
