package brightspot.apple.news.v2.displaycontrol;

import brightspot.apple.news.v2.automation.AppleNewsAutoCreationEvent;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.dari.util.Substitution;

public class AppleNewsAutoCreationEventSubstitution extends AppleNewsAutoCreationEvent implements Substitution {

    @Override
    public boolean shouldExecute(Site site, Object content) {
        if (!SiteSettings.get(
            site,
            siteSettings -> siteSettings.as(AppleNewsDisplaySettings.class).isAppleNewsEnabled())) {
            return false;
        }
        return super.shouldExecute(site, content);
    }
}
