package brightspot.recipe;

import java.util.Objects;

import brightspot.recalculate.MethodRecalculation;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.MoreStringUtils;
import brightspot.util.NoUrlsWidget;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.dari.db.Query;

public class RecipeTag extends Content implements
    NoUrlsWidget {

    private static final String NAME_MODIFIED_EXTRA = RecipeTag.class.getName() + ".nameModified";

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

    // --- Record support ---

    @Override
    protected void beforeCommit() {
        Object previousVersion = Query.fromAll()
            .where("_id = ?", this)
            .noCache()
            .first();
        if (!(previousVersion instanceof RecipeTag)) {
            return;
        }

        RecipeTag previousRecipeTag = (RecipeTag) previousVersion;

        if (!Objects.equals(previousRecipeTag.getName(), this.getName())) {
            getState().getExtras().put(NAME_MODIFIED_EXTRA, true);
        }
    }

    @Override
    protected void afterSave() {
        if (Boolean.TRUE.equals(getState().getExtra(NAME_MODIFIED_EXTRA))) {
            MethodRecalculation.recalculateMethodByQuery(
                Query.fromAll()
                    .where(Recipe.class.getName() + "/" + Recipe.RECIPE_TAGS_FIELD + " = ?", this),
                Recipe.RECIPE_TAG_NAMES_FIELD);
        }
    }

    @Override
    protected void afterDelete() {
        MethodRecalculation.recalculateMethodByQuery(
            Query.fromAll()
                .where(Recipe.class.getName() + "/" + Recipe.RECIPE_TAGS_FIELD + " = ?", this),
            Recipe.RECIPE_TAG_NAMES_FIELD);
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return MoreStringUtils.firstNonBlank(getInternalName(), this::getInternalNameFallback);
    }
}
