package brightspot.core.seo;

import java.util.Collection;

import brightspot.core.tool.TaskUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Query;
import com.psddev.sitemap.GlobalSiteMapItem;
import com.psddev.sitemap.SiteMapConfig;
import com.psddev.sitemap.SiteMapType;

/**
 * This class will be found and instantiated by
 * {@link SiteMapConfig#getDefault()}.
 */
public class ExpressSiteMapConfig implements SiteMapConfig {

    @Override
    public boolean isAllowedToRun() {
        return TaskUtils.isRunningOnTaskHost();
    }

    @Override
    public Collection<SiteMapType<GlobalSiteMapItem>> getSiteMapTypes(Site site) {
        SiteSettings settings = site == null
                ? Query.from(CmsTool.class).first()
                : site;

        return settings.as(GlobalSitemapSettingsModification.class)
                .getGenerateSiteMapTypes();
    }
}
