package brightspot.jwplayer;

import brightspot.jwplayer.db.JwPlayerSiteSettings;
import brightspot.jwplayer.db.JwPlayerVideo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.tool.ClassDisplay;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.web.WebRequest;
import org.apache.commons.lang3.ObjectUtils;

public class JwPlayerClassDisplay implements ClassDisplay {

    @Override
    public boolean shouldHide(Class<?> instanceClass) {
        if (JwPlayerVideo.class.isAssignableFrom(instanceClass)
            || JwPlayerIdVideo.class.isAssignableFrom(instanceClass)) {

            Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();

            return ObjectUtils.isEmpty(SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(JwPlayerSiteSettings.class).getJwPlayerAccounts()));
        }
        return false;
    }
}
