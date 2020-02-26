package brightspot.core.tool;

import com.psddev.cms.tool.ToolPageContext;

/**
 * Provides a mechanism to add a block of HTML above the edit page fields for supported Objects.
 */
public interface EditTopHtml {

    default int getPriority() {
        return 0;
    }

    boolean isSupported(Object object);

    void writeHtml(Object item, ToolPageContext page);
}
