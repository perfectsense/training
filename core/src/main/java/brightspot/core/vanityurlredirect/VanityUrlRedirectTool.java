package brightspot.core.vanityurlredirect;

import java.util.ArrayList;
import java.util.List;

import com.psddev.cms.db.Localization;
import com.psddev.cms.tool.Plugin;
import com.psddev.cms.tool.Tool;

/**
 * Class that establishes VanityUrlRedirect as an administrator tool, rendered in the dropdown menu.
 */
public class VanityUrlRedirectTool extends Tool {

    @Override
    public String getApplicationName() {
        return "cms";
    }

    @Override
    public List<Plugin> getPlugins() {
        List<Plugin> plugins = new ArrayList<>();

        plugins.add(createArea2(Localization.currentUserText(VanityUrlRedirectTool.class, "title", "Vanity Redirects"),
            "vanity-url-redirect", "admin/vanity-url-redirect", "admin/vanity-url-redirect"));

        return plugins;
    }
}
