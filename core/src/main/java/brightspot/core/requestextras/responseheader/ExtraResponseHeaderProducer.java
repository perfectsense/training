package brightspot.core.requestextras.responseheader;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.requestextras.RequestExtrasProducer;
import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Application;

/**
 * Get the ExtraResponseHeaders from the global and site settings.
 */
public class ExtraResponseHeaderProducer implements RequestExtrasProducer<CustomResponseHeaders> {

    @Override
    public List<CustomResponseHeaders> produce(Site site, Object mainObject) {
        List<CustomResponseHeaders> responseHeaders = new ArrayList<>();
        CmsTool cmsTool = Application.Static.getInstance(CmsTool.class);

        // First add the defaults
        responseHeaders.addAll(cmsTool.as(FrontEndSettings.class).getCustomResponseHeaders());

        if (site != null) {
            // Then add the site settings to the end of the list
            responseHeaders.addAll(site.as(FrontEndSettings.class).getCustomResponseHeaders());
        }

        return responseHeaders;
    }
}
