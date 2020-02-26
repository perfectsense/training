package brightspot.core.tool;

import java.io.IOException;

import com.psddev.cms.tool.ToolPageContext;
import com.psddev.cms.tool.ToolPageHead;
import com.psddev.dari.util.Cdn;

public class ToolExtrasToolPageHead implements ToolPageHead {

    @Override
    public void writeHtml(ToolPageContext page) throws IOException {
        page.writeStart(
            "script",
            "type",
            "text/javascript",
            "src",
            Cdn.getUrl(page.getRequest(), "/_resource/ToolExtras.js"));
        page.writeEnd();
    }
}
