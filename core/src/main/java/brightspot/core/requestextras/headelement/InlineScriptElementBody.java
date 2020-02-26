package brightspot.core.requestextras.headelement;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Inline")
public class InlineScriptElementBody extends ScriptElementBody {

    @ToolUi.CodeType("text/javascript")
    private String body;

    public String getBody() {
        return body;
    }
}
