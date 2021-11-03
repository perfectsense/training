package brightspot.recipe;

import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.MoreStringUtils;
import brightspot.util.NoUrlsWidget;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;

public class RecipeTag extends Content implements
    NoUrlsWidget {

    @Indexed
    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String name;

    @DynamicPlaceholderMethod("getInternalNameFallback")
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

    // --- Fallbacks ---

    private String getInternalNameFallback() {
        return RichTextUtils.richTextToPlainText(getName());
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return MoreStringUtils.firstNonBlank(getInternalName(), this::getInternalNameFallback);
    }
}
