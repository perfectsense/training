package brightspot.core.hat;

import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Rich Text Hat")
public class RichTextHat extends Hat {

    private String name;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
