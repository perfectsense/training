package brightspot.core.requestextras.headelement;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.requestextras.RequestExtrasProducer;
import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Application;

/**
 * Get the ExtraHeadElements from the global and site settings.
 */
public class ExtraHeadElementProducer implements RequestExtrasProducer<CustomHeadElements> {

    @Override
    public List<CustomHeadElements> produce(Site site, Object mainObject) {
        List<CustomHeadElements> headElements = new ArrayList<>();
        CmsTool cmsTool = Application.Static.getInstance(CmsTool.class);

        // First add the defaults
        headElements.addAll(cmsTool.as(FrontEndSettings.class).getCustomScriptsAndStyles());

        if (site != null) {
            // Then add the site settings to the end of the list
            headElements.addAll(site.as(FrontEndSettings.class).getCustomScriptsAndStyles());
        }

        return headElements;
    }
}
