package brightspot.core.seo;

import java.util.Optional;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;

public class SeoSettingsModification extends Modification<SiteSettings> {

    protected static final String DEFAULT_ROBOTS_TEXT = "User-agent: *\nCrawl-delay: 10\n";

    @ToolUi.Cluster("SEO")
    @ToolUi.Tab("Front-End")
    @DisplayName("robots.txt")
    @ToolUi.Placeholder(dynamicText = "${modification.getRobotsPlaceholder()}")
    private String robotsTxt;

    protected String getRobotsTxt() {
        return robotsTxt;
    }

    /**
     * Not for external use
     **/
    public String getRobotsPlaceholder() {
        if (getOriginalObject() instanceof Site) {
            // Fall back to global setting first
            return Optional.ofNullable(Query.from(CmsTool.class).first())
                .map(tool -> tool.as(SeoSettingsModification.class).getRobotsTxt())
                .orElse(DEFAULT_ROBOTS_TEXT);
        }
        return DEFAULT_ROBOTS_TEXT;
    }
}
