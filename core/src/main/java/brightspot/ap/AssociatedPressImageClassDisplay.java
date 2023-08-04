package brightspot.ap;

import com.psddev.ap.AssociatedPressImage;
import com.psddev.ap.AssociatedPressSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.tool.ClassDisplay;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.web.WebRequest;
import org.apache.commons.lang3.ObjectUtils;

public class AssociatedPressImageClassDisplay implements ClassDisplay {

    @Override
    public boolean shouldHide(Class<?> instanceClass) {
        if (AssociatedPressImage.class.isAssignableFrom(instanceClass)) {

            Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();

            return ObjectUtils.isEmpty(SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(AssociatedPressSettings.class).getApiClient()));
        }
        return false;
    }
}
