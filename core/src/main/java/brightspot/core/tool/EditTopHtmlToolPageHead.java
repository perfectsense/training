package brightspot.core.tool;

import java.io.IOException;

import com.psddev.cms.tool.ToolPageContext;
import com.psddev.cms.tool.ToolPageHead;
import com.psddev.dari.util.Cdn;

/**
 * Provides script and style dependencies for EditTopHtml interface functionality.
 */
public class EditTopHtmlToolPageHead implements ToolPageHead {

    @Override
    public void writeHtml(ToolPageContext page) throws IOException {

        page.writeStart("script", "src", Cdn.getUrl(page.getRequest(), "/_resource/edit-top-html.js"));
        page.writeEnd();
    }
}
