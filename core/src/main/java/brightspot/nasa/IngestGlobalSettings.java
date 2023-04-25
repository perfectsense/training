package brightspot.nasa;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.ui.form.Placeholder;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Singleton;
import org.apache.commons.lang3.StringUtils;

public class IngestGlobalSettings extends Record {

    private static final String DEFAULT_URL = "https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss";
    private Boolean enable;

    @Placeholder(DEFAULT_URL)
    private String feedUrl;

    private ToolUser user;
    private Site site;

    public boolean isEnable() {
        return Boolean.TRUE.equals(enable);
    }

    public void setEnable(boolean enable) {
        this.enable = Boolean.TRUE.equals(enable) ? true : null;
    }

    public String getFeedUrl() {

        return StringUtils.firstNonBlank(feedUrl, DEFAULT_URL);
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public ToolUser getUser() {
        return user;
    }

    public void setUser(ToolUser user) {
        this.user = user;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public static IngestGlobalSettings getSettings() {
        return Singleton.getInstance(CmsTool.class)
            .as(NasaIngesterGlobalSettingsModification.class).getIngestGlobalSettings();
    }
}
