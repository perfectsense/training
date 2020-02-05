package brightspot.core.timed;

import java.io.IOException;
import javax.servlet.ServletException;

import com.psddev.cms.tool.PageServlet;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.util.RoutingFilter;

/**
 * Placeholder player UI used for debugging purposes and doesn't actually do anything.
 */

@RoutingFilter.Path(value = NoOpToolPlayerServlet.PAGE_URL)
public class NoOpToolPlayerServlet extends PageServlet {

    static final String PAGE_URL = "/_express/timed/no-op-tool-player";

    public static String getPageUrl() {
        return PAGE_URL;
    }

    @Override
    protected String getPermissionId() {
        return null;
    }

    @Override
    protected void doService(ToolPageContext page) throws IOException, ServletException {

        page.writeStart("div", "class", "NoOpToolPlayer");
        {
            page.writeHtml("Player Goes Here...");
        }
        page.writeEnd();
    }
}
