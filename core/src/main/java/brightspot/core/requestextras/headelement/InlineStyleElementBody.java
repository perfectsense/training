package brightspot.core.requestextras.headelement;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Inline")
public class InlineStyleElementBody extends StyleElementBody {

    @ToolUi.CodeType("text/css")
    private String body;

    public String getBody() {
        return body;
    }
}
