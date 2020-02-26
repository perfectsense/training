package brightspot.core.seo;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.sitemap.GlobalSiteMapItem;
import com.psddev.sitemap.SiteMapType;
import com.psddev.sitemap.SiteMapTypeValueGenerator;

@ToolUi.FieldInternalNamePrefix("sitemap.")
public class GlobalSitemapSettingsModification extends Modification<SiteSettings> {

    protected static final String SITEMAP_SETTINGS_CLUSTER_NAME = "Sitemap Settings";

    @ToolUi.Cluster(SITEMAP_SETTINGS_CLUSTER_NAME)
    @ToolUi.Tab("Front-End")
    @ToolUi.ValueGeneratorClass(SiteMapTypeValueGenerator.class)
    @DisplayName("Site Map Types")
    private Set<String> generateSiteMapTypeClassNames;

    public Set<SiteMapType<GlobalSiteMapItem>> getGenerateSiteMapTypes() {
        if (generateSiteMapTypeClassNames == null) {
            generateSiteMapTypeClassNames = new HashSet<>();
        }
        return generateSiteMapTypeClassNames.stream()
                .map(SiteMapType::instantiateFromName)
                .collect(Collectors.toSet());
    }

    public void setGenerateSiteMapTypeClassNames(
            Set<String> generateSiteMapTypeClassNames) {
        this.generateSiteMapTypeClassNames = generateSiteMapTypeClassNames;
    }
}
