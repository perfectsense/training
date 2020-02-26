package brightspot.core.vanityurlredirect;

import java.io.IOException;
import javax.servlet.ServletException;

import com.psddev.cms.tool.PageServlet;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.State;
import com.psddev.dari.util.RoutingFilter;

/**
 * Class that renders the Vanity Url Redirect page, which consists of a lefthand list of existing urls with search
 * feature and create new redirect form. If a query is typed into the search bar, VanityUrlResults.java is called to
 * render the results.
 */
@RoutingFilter.Path(application = "cms", value = "admin/vanity-url-redirect")
public class VanityUrlRedirectServlet extends PageServlet {

    @Override
    protected String getPermissionId() {
        return "area/admin/vanity-url-redirect";
    }

    @Override
    protected void doService(ToolPageContext page) throws IOException, ServletException {

        Object selected = page.findOrReserve(VanityUrlRedirect.class);
        State selectedState = State.getInstance(selected);
        String queryString = page.param(String.class, "query");

        if (page.tryStandardUpdate(selected)) {
            return;
        }

        page.writeHeader();

        page.writeStart("div", "class", "withLeftNav");
        {
            page.writeStart("div", "class", "leftNav");
            {
                page.writeStart("div", "class", "widget");
                {
                    page.writeStart("h1");
                    page.writeHtml("Vanity URL Redirect");
                    page.writeEnd();
                    page.writeStart("div", "class", "widget-controls");
                    {
                        page.writeStart("ul", "class", "piped");
                        {
                            page.writeStart("li");
                            page.writeStart("a",
                                "class", "IconButton icon icon-action-search icon-only",
                                "data-icon-button-name", "search",
                                "href", page.cmsUrl("/searchAdvancedFull",
                                    "rt", ObjectType.getInstance(VanityUrlRedirect.class).getId(),
                                    "l", 50));
                            page.writeHtml("Search");
                            page.writeEnd();
                            page.writeEnd();
                        }
                        page.writeEnd();
                    }
                    page.writeEnd();

                    page.writeStart("ul", "class", "links");
                    page.writeStart(
                        "li",
                        "class",
                        "new " + (selected instanceof VanityUrlRedirect && selectedState.isNew() ? "selected" : ""));
                    {
                        page.writeStart("a", "href", page.typeUrl(null, VanityUrlRedirect.class));
                        page.writeHtml(page.localize(VanityUrlRedirect.class, "action.newType"));
                        page.writeEnd();
                    }
                    page.writeEnd();
                    page.writeEnd();

                    page.writeStart(
                        "form",
                        "action",
                        page.url("/admin/vanity-url-results"),
                        "data-bsp-autosubmit",
                        "",
                        "method",
                        "get",
                        "target",
                        "vanityUrlResults");
                    page.writeElement("input", "name", "id", "type", "hidden", "value", selectedState.getId());
                    page.writeElement(
                        "input",
                        "name",
                        "offset",
                        "type",
                        "hidden",
                        "value",
                        page.param(Long.TYPE, "offset"));
                    page.writeStart("div", "class", "searchInput");
                    page.writeStart("label", "for", page.createId());
                    page.writeHtml(page.localize(VanityUrlRedirect.class, "label.filter"));
                    page.writeEnd();
                    page.writeElement(
                        "input",
                        "id",
                        page.getId(),
                        "class",
                        "autoFocus",
                        "name",
                        "query",
                        "type",
                        "text",
                        "value",
                        queryString);
                    page.writeElement("input", "type", "submit", "value", "Go");
                    page.writeEnd();
                    page.writeEnd();

                    page.writeStart("div", "class", "frame", "name", "vanityUrlResults");
                    page.writeEnd();

                }
                page.writeEnd();
            }
            page.writeEnd();

            page.writeStart("div", "class", "main");
            {
                page.writeStart("div", "class", "widget");
                page.writeStandardForm(selected);
                page.writeEnd();
            }
            page.writeEnd();

        }
        page.writeEnd();

        page.writeFooter();
    }
}
