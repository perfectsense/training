package brightspot.getty;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.tool.ClassDisplay;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.web.WebRequest;
import com.psddev.getty.GettyImage;
import com.psddev.getty.GettySettings;
import org.apache.commons.lang3.ObjectUtils;

public class GettyImageClassDisplay implements ClassDisplay {

    @Override
    public boolean shouldHide(Class<?> instanceClass) {
        if (GettyImage.class.isAssignableFrom(instanceClass)) {

            Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();

            return ObjectUtils.isEmpty(SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(GettySettings.class).getApiClient()));
        }
        return false;
    }
}
