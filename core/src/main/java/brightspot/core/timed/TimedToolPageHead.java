package brightspot.core.timed;

import java.io.IOException;

import com.psddev.cms.tool.ToolPageContext;
import com.psddev.cms.tool.ToolPageHead;
import com.psddev.dari.util.Cdn;

/**
 * Adds scripts and styles related to editing {@linkplain TimedContent}.
 */
public class TimedToolPageHead implements ToolPageHead {

    @Override
    public void writeHtml(ToolPageContext page) throws IOException {
        includePlyrMediaToolPlayer(page);
    }

    private void includePlyrMediaToolPlayer(ToolPageContext page) throws IOException {
        page.writeElement("link", "rel", "stylesheet", "type", "text/css", "href", "//cdn.plyr.io/2.0.12/plyr.css");
        page.writeElement(
            "link",
            "rel",
            "stylesheet",
            "type",
            "text/css",
            "href",
            Cdn.getUrl(page.getRequest(), "/_resource/brightspot/core/timed/PlyrMediaToolPlayer.css"));

        page.writeStart(
            "script",
            "type",
            "text/javascript",
            "src",
            Cdn.getUrl(page.getRequest(), "/_resource/brightspot/core/timed/PlyrMediaToolPlayer.js"));
        page.writeEnd();
    }
}
