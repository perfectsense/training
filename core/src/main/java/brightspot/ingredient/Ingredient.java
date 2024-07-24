package brightspot.ingredient;

import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.NoUrlsWidget;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public class Ingredient extends Content implements
    NoUrlsWidget {

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String name;

    // --- Getters/setters ---

    /**
     * @return rich text
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // --- Utility ---

    public String getNamePlainText() {
        return RichTextUtils.richTextToPlainText(getName());
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getNamePlainText();
    }
}
