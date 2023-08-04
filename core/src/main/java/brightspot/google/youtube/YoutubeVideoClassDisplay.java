package brightspot.google.youtube;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.tool.ClassDisplay;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.web.WebRequest;
import com.psddev.google.youtube.YouTubeSettings;
import com.psddev.google.youtube.YouTubeVideo;
import org.apache.commons.lang3.ObjectUtils;

public class YoutubeVideoClassDisplay implements ClassDisplay {

    @Override
    public boolean shouldHide(Class<?> instanceClass) {
        if (YouTubeVideo.class.isAssignableFrom(instanceClass)) {

            Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();

            return ObjectUtils.isEmpty(SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(YouTubeSettings.class).getApiClient()));
        }
        return false;
    }
}
