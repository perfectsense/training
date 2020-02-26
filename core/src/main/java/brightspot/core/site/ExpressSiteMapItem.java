package brightspot.core.site;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import brightspot.core.update.LastUpdatedProvider;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;
import com.psddev.sitemap.SiteMapEntry;
import com.psddev.sitemap.SiteMapItem;
import com.psddev.sitemap.SiteMapSettingsModification;

public interface ExpressSiteMapItem extends SiteMapItem {

    @Override
    default List<SiteMapEntry> getSiteMapEntries() {
        List<SiteMapEntry> siteMapEntries = new ArrayList<>();
        Stream.concat(Site.Static.findAll().stream(), Stream.of(new Site[] { null })).forEach(site -> {
            String sitePermalinkPath = as(Directory.ObjectModification.class).getSitePermalinkPath(site);

            if (!StringUtils.isBlank(sitePermalinkPath)) {
                SiteMapEntry siteMapEntry = new SiteMapEntry();
                siteMapEntry.setUpdateDate(
                    ObjectUtils.firstNonNull(
                        LastUpdatedProvider.getMostRecentUpdateDate(getState()),
                        getState().as(Content.ObjectModification.class).getPublishDate()
                    )
                );
                siteMapEntry.setPermalink(SiteSettings.get(
                    site,
                    f -> f.as(SiteMapSettingsModification.class).getSiteMapDefaultUrl() + StringUtils.ensureStart(
                        sitePermalinkPath,
                        "/")));

                siteMapEntries.add(siteMapEntry);
            }
        });
        return siteMapEntries;
    }
}
