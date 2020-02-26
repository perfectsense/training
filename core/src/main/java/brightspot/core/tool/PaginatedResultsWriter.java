package brightspot.core.tool;

import java.io.IOException;

import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.Content;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PaginatedResult;

/**
 * Provides an extensible base implementation for rendering PaginatedResults in the Tool UI.
 *
 * Minimal implementation requires overriding {@link #getQuery(ToolPageContext)}.
 *
 * Optionally override other methods for advanced customization.
 */
public abstract class PaginatedResultsWriter {

    protected static final int[] LIMITS = { 10, 20, 50 };
    public static final String OFFSET_PARAMETER = "offset";
    public static final String LIMIT_PARAMETER = "limit";

    /**
     * Implementations should produce a Query for the type to be displayed.
     *
     * @param page Used for access to request parameters.
     * @return the {@link Query} to produce the {@link PaginatedResult}.
     */
    public abstract Query<?> getQuery(ToolPageContext page);

    /**
     * Optionally override for more control over the creation of the {@link PaginatedResult}.
     *
     * @param page Used for access to request parameters.
     * @return the {@link PaginatedResult} to be displayed by the widget.
     */
    public PaginatedResult<?> getPaginatedResult(ToolPageContext page) {
        return getQuery(page).select(
            page.param(long.class, OFFSET_PARAMETER),
            page.paramOrDefault(int.class, LIMIT_PARAMETER, LIMITS[0]));
    }

    /**
     * Optionally override to provide filters for the PaginatedResult displayed by the widget.
     *
     * @param page Used to write HTML.
     * @throws IOException
     */
    public void writeFiltersHtml(ToolPageContext page) throws IOException {
        // Default implementation has no filters
    }

    /**
     * Optionally override to customize the container of the results.
     *
     * @param page Used to write HTML.
     * @param result A {@link PaginatedResult} from {@link #getQuery(ToolPageContext)}
     * @throws IOException
     */
    public void writeResultsHtml(ToolPageContext page, PaginatedResult<?> result) throws IOException {

        page.writeStart("table", "class", "links table-striped pageThumbnails");

        page.writeStart("thead");
        writeResultsTableHead(page);
        page.writeEnd();

        page.writeStart("tbody");
        for (Object object : result.getItems()) {
            writeResultsItemHtml(page, (Recordable) object);
        }
        page.writeEnd();

        page.writeEnd();

    }

    public void writeEmptyHtml(ToolPageContext page) throws IOException {
        page.writeStart("div", "class", "message message-info");
        page.writeStart("p");
        page.writeHtml(page.localize(PaginatedResultsWriter.class, "message.noResults"));
        page.writeEnd();
        page.writeEnd();
    }

    public void writeResultsTableHead(ToolPageContext page) throws IOException {
        // Render nothing by default
    }

    /**
     * Optionally override to customize appearance of a row in the table.
     *
     * @param page Used to write HTML.
     * @param recordable A {@link Recordable} from the items produced by the {@link PaginatedResult} used by {@link
     * #writeResultsHtml(ToolPageContext, PaginatedResult)}
     * @throws IOException
     */
    public void writeResultsItemHtml(ToolPageContext page, Recordable recordable) throws IOException {

        page.writeStart("tr");
        page.writeStart("td");
        page.writeTypeLabel(recordable);
        page.writeEnd();

        page.writeStart("td");

        if (recordable instanceof Content) {
            page.writeStart("a",
                "target", "_top",
                "href", page.cmsUrl(page.objectUrl("/content/edit.jsp", recordable)));
        }

        page.writeObjectLabel(recordable);

        if (recordable instanceof Content) {
            page.writeEnd();
        }

        page.writeEnd();
        page.writeEnd();
    }

    public void writePaginationHtml(ToolPageContext page, PaginatedResult<?> result, int limit) throws IOException {

        // Pagination
        page.writeStart("ul", "class", "pagination");
        if (result.hasPrevious()) {
            page.writeStart("li", "class", "first");
            page.writeStart("a", "href", page.url("", OFFSET_PARAMETER, result.getFirstOffset()));
            page.writeHtml(page.localize(PaginatedResultsWriter.class, "pagination.newest"));
            page.writeEnd();
            page.writeEnd();

            page.writeStart("li", "class", "previous");
            page.writeStart("a", "href", page.url("", OFFSET_PARAMETER, result.getPreviousOffset()));
            page.writeHtml(page.localize(
                PaginatedResultsWriter.class,
                ImmutableMap.of("count", limit),
                "pagination.newerCount"));
            page.writeEnd();
            page.writeEnd();
        }

        if (result.getOffset() > 0 || result.hasNext() || result.getItems().size() > LIMITS[0]) {
            page.writeStart("li");
            writePaginationLimitFormHtml(page, limit);
            page.writeEnd();
        }

        if (result.hasNext()) {
            page.writeStart("li", "class", "next");
            page.writeStart("a", "href", page.url("", "offset", result.getNextOffset()));
            page.writeHtml(page.localize(
                PaginatedResultsWriter.class,
                ImmutableMap.of("count", limit),
                "pagination.olderCount"));
            page.writeEnd();
            page.writeEnd();
        }
        writeExtraButtons(page);

        page.writeEnd();
    }

    public void writeExtraButtons(ToolPageContext page) throws IOException {
        //override to add extra buttons to top bar
    }

    public void writePaginationLimitFormHtml(ToolPageContext page, int limit) throws IOException {
        page.writeStart("form",
            "data-bsp-autosubmit", "",
            "method", "get",
            "action", page.url(null));
        page.writeStart("select", "name", LIMIT_PARAMETER);
        for (int l : LIMITS) {
            page.writeStart("option",
                "value", l,
                "selected", limit == l ? "selected" : null);
            page.writeHtml(page.localize(
                PaginatedResultsWriter.class,
                ImmutableMap.of("count", l),
                "option.showCount"));
            page.writeEnd();
        }
        page.writeEnd();
        page.writeEnd();
    }

    /**
     * Optionally override to add a container.
     *
     * @param page Used to write HTML.
     * @throws IOException, ServletException
     */
    public void writeHtml(ToolPageContext page) throws IOException {

        page.writeStart("div", "class", "PR");
        page.writeStart("div", "class", "widget-filters");
        page.writeStart("form",
            "method", "get",
            "action", page.url(null));
        writeFiltersHtml(page);
        page.writeEnd();
        page.writeEnd();

        PaginatedResult result = getPaginatedResult(page);

        writePaginationHtml(page, result, page.paramOrDefault(int.class, LIMIT_PARAMETER, LIMITS[0]));

        if (result.hasPages()) {
            writeResultsHtml(page, result);
        } else {
            writeEmptyHtml(page);
        }
        page.writeEnd();
    }
}
