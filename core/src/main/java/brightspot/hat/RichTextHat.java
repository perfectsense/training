package brightspot.hat;

import brightspot.rte.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Rich Text Hat")
public class RichTextHat extends Hat {

    @Required
    private String internalName;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String text;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getLabel() {
        return getInternalName();
    }
}
