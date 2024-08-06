package brightspot.ingredient;

import brightspot.rte.TinyRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public class Ingredient extends Content {

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
}
