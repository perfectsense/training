package brightspot.filteringrequests;

import brightspot.link.Link;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;

public class RequestRedirectSiteSettingsModification extends Modification<SiteSettings> {

    @ToolUi.Tab("CMS")
    @ToolUi.Cluster("Request Redirect Filter")
    private Boolean isFilterEnabled;
    @ToolUi.Tab("CMS")
    @ToolUi.Cluster("Request Redirect Filter")
    private String regex;
    @ToolUi.Tab("CMS")
    @ToolUi.Cluster("Request Redirect Filter")
    private Link redirectTo;

    public Boolean getFilterEnabled() {
        return Boolean.TRUE.equals(isFilterEnabled);
    }

    public void setFilterEnabled(boolean filterEnabled) {
        this.isFilterEnabled = Boolean.TRUE.equals(isFilterEnabled) ? true : null;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Link getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(Link redirectTo) {
        this.redirectTo = redirectTo;
    }

}
