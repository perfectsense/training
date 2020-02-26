package brightspot.core.tool;

import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.ToolPageContext;

public interface SearchInitializer {

    void initializeSearch(Search search, ToolPageContext page);
}
