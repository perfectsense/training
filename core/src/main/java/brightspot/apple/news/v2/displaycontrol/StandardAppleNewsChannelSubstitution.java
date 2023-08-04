package brightspot.apple.news.v2.displaycontrol;

import brightspot.apple.news.v2.channel.StandardAppleNewsChannel;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.dari.util.Substitution;

public class StandardAppleNewsChannelSubstitution extends StandardAppleNewsChannel implements Substitution {

    @Override
    public boolean isEnabled() {
        if (!SiteSettings.get(
            this.as(Site.ObjectModification.class).getOwner(),
            siteSettings -> siteSettings.as(AppleNewsDisplaySettings.class).isAppleNewsEnabled())) {
            return false;
        }
        return super.isEnabled();
    }
}
