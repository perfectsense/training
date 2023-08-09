package brightspot.adobe.stock;

import com.psddev.adobe.stock.AdobeStockImage;
import com.psddev.adobe.stock.AdobeStockSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.tool.ClassDisplay;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.web.WebRequest;
import org.apache.commons.lang3.ObjectUtils;

public class AdobeStockClassDisplay implements ClassDisplay {

    @Override
    public boolean shouldHide(Class<?> instanceClass) {
        if (AdobeStockImage.class.isAssignableFrom(instanceClass)) {

            Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();

            return ObjectUtils.isEmpty(SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(AdobeStockSettings.class).getApiClient()));
        }
        return false;
    }
}
