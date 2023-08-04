package brightspot.apple.news.v2.displaycontrol;

import brightspot.apple.news.v2.distribution.AppleNewsDistributionJobSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.tool.ClassDisplay;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.web.WebRequest;

class AppleNewsClassDisplay implements ClassDisplay {

    @Override
    public boolean shouldHide(Class<?> instanceClass) {
        //Only hide for UI
        if (instanceClass.getPackage().getName().startsWith("brightspot.apple.news")
            && !instanceClass.getPackage().getName().startsWith("brightspot.apple.news.v2.displaycontrol")
            && !AppleNewsDistributionJobSettings.class.equals(instanceClass)) {
            if (WebRequest.isAvailable()) {
                Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
                return !SiteSettings.get(
                    site,
                    siteSettings -> siteSettings.as(AppleNewsDisplaySettings.class).isAppleNewsEnabled());
            }
        }
        return false;
    }
}
