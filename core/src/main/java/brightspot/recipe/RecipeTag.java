package brightspot.recipe;

import brightspot.rte.TinyRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public class RecipeTag extends Content {

    @Indexed
    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String name;

    private String internalName;

    // --- Getters/setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }
}
