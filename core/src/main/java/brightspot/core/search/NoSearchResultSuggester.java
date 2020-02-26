package brightspot.core.search;

import java.io.IOException;

import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.SearchResultSuggester;
import com.psddev.cms.tool.ToolPageContext;

/**
 * Suppresses all search result suggestions under a certain priority.
 */
public class NoSearchResultSuggester implements SearchResultSuggester {

    public static final int MINIMUM_SEARCH_RESULT_SUGGESTER_PRIORITY = 100;

    @Override
    public double getPriority(Search search) {
        return MINIMUM_SEARCH_RESULT_SUGGESTER_PRIORITY;
    }

    @Override
    public void writeHtml(Search search, ToolPageContext toolPageContext) throws IOException {
    }
}
