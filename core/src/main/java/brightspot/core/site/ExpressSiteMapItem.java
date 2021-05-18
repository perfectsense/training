package brightspot.core.site;

import java.util.Collections;
import java.util.List;

import brightspot.core.update.LastUpdatedProvider;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.sitemap.SiteMapEntry;
import com.psddev.sitemap.SiteMapItem;
import com.psddev.sitemap.SiteMapSettingsModification;
import org.apache.commons.lang3.StringUtils;

public interface ExpressSiteMapItem extends SiteMapItem {

    @Override
    default List<SiteMapEntry> getSiteMapEntries(Site site) {
        String sitePermalinkPath = as(Directory.ObjectModification.class).getSitePermalinkPath(site);

        if (StringUtils.isBlank(sitePermalinkPath)) {
            return Collections.emptyList();
        }

        SiteMapEntry siteMapEntry = new SiteMapEntry();
        siteMapEntry.setUpdateDate(
                ObjectUtils.firstNonNull(
                        LastUpdatedProvider.getMostRecentUpdateDate(getState()),
                        getState().as(Content.ObjectModification.class).getPublishDate()
                )
        );
        siteMapEntry.setPermalink(SiteSettings.get(
                site,
                f -> f.as(SiteMapSettingsModification.class).getSiteMapDefaultUrl()
                        + StringUtils.prependIfMissing(sitePermalinkPath, "/")));

        return Collections.singletonList(siteMapEntry);
    }
}
