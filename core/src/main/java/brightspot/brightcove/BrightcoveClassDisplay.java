package brightspot.brightcove;

import com.psddev.brightcove.BrightcoveSiteSettingsModification;
import com.psddev.brightcove.BrightcoveVideo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.tool.ClassDisplay;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.web.WebRequest;
import org.apache.commons.lang3.ObjectUtils;

public class BrightcoveClassDisplay implements ClassDisplay {

    @Override
    public boolean shouldHide(Class<?> instanceClass) {
        if (BrightcoveVideo.class.isAssignableFrom(instanceClass)
            || BrightcoveIdVideo.class.isAssignableFrom(instanceClass)) {

            Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();

            return ObjectUtils.isEmpty(SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(BrightcoveSiteSettingsModification.class).getBrightcoveAccounts()));
        }
        return false;
    }
}
