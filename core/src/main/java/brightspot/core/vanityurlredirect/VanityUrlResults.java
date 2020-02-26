package brightspot.core.vanityurlredirect;

import java.io.IOException;
import javax.servlet.ServletException;

import com.psddev.cms.tool.PageServlet;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PaginatedResult;
import com.psddev.dari.util.RoutingFilter.Path;

@Path(
    application = "cms",
    value = "admin/vanity-url-results"
)

/**
 * Gets called by VanityRedirectServlet class to dynamically
 * re-render Vanity Redirect page if a query is typed into the search bar
 */
public class VanityUrlResults extends PageServlet {

    @Override
    protected String getPermissionId() {
        return "area/admin/vanity-url-redirect";
    }

    @Override
    protected void doService(ToolPageContext page) throws IOException, ServletException {
        Object selected = page.findOrReserve(VanityUrlRedirect.class);
        State selectedState = State.getInstance(selected);

        String queryString = page.param(String.class, "query");
        long offset = page.param(Long.class, "offset");

        page.writeStart("div");
        if (!ObjectUtils.isBlank(queryString)) {
            queryString = queryString.trim();
            PaginatedResult<VanityUrlRedirect> results = Query.from(VanityUrlRedirect.class)
                .where("name contains ? or destination contains ? or localUrls contains ?",
                    queryString, queryString, queryString)
                .sortDescending("name")
                .select(offset, 15);

            if (results.hasPrevious() || results.hasNext()) {
                page.writeStart("br").writeEnd();
                page.writeStart("ul", "class", "pagination");
                page.writeStart("li", "class", "previous");
                if (results.hasPrevious()) {
                    page.writeStart("a", "href", page.url("", "offset", results.getPreviousOffset()));
                    page.writeHtml(results.getLimit());
                    page.writeEnd();
                }

                page.writeEnd();
                page.writeStart("li", "class", "next");
                if (results.hasNext()) {
                    page.writeStart("a", "href", page.url("", "offset", results.getNextOffset()));
                    page.writeHtml(results.getLimit());
                    page.writeEnd();
                }
                page.writeEnd();

                page.writeEnd();
            }

            page.writeStart("ul", "class", "links");

            if (results.getOffset() <= 0L && results.getItems().isEmpty()) {
                page.writeStart("div", "class", "message message-warning");
                page.writeHtml(page.localize(this.getClass(), "message.noMatches"));
                page.writeEnd();
            } else {

                for (VanityUrlRedirect v : results.getItems()) {
                    page.writeStart("li", "class", v.equals(selected) ? "selected" : null);
                    page.writeStart(
                        "a",
                        "target",
                        "_top",
                        "href",
                        page.objectUrl("/admin/vanity-url-redirect", v, "query", queryString, "offset", offset));
                    page.writeObjectLabel(v);
                    page.writeEnd();
                    page.writeEnd();
                }
            }

            page.writeEnd();

        } else {
            page.writeStart("ul", "class", "links");

            for (VanityUrlRedirect v : Query.from(VanityUrlRedirect.class).sortAscending("destination").selectAll()) {
                page.writeStart("li", "class", v.equals(selected) ? "selected" : null);
                page.writeStart(
                    "a",
                    "target",
                    "_top",
                    "href",
                    page.objectUrl("/admin/vanity-url-redirect", v, "query", queryString, "offset", offset));
                page.writeObjectLabel(v);
                page.writeEnd();
                page.writeEnd();
            }
            page.writeEnd();
        }
        page.writeEnd();

    }
}
