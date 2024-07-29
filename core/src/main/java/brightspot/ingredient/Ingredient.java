package brightspot.ingredient;

import java.util.Objects;

import brightspot.recalculate.MethodRecalculation;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.NoUrlsWidget;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;

public class Ingredient extends Content implements
    NoUrlsWidget {

    public static final String NAME_PLAIN_TEXT_FIELD = "getNamePlainText";
    private static final String NAME_MODIFIED_EXTRA = Ingredient.class.getName() + ".nameModified";

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

    // --- Indexes ---

    @Indexed
    @ToolUi.Hidden
    public String getNamePlainText() {
        return RichTextUtils.richTextToPlainText(getName());
    }

    // --- Record support ---

    @Override
    protected void beforeCommit() {
        Object previousVersion = Query.fromAll()
            .where("_id = ?", this)
            .noCache()
            .first();
        if (!(previousVersion instanceof Ingredient)) {
            return;
        }

        Ingredient previousIngredient = (Ingredient) previousVersion;

        if (!Objects.equals(previousIngredient.getNamePlainText(), this.getNamePlainText())
            || !Objects.equals(previousIngredient.getState().isVisible(), this.getState().isVisible())) {

            getState().getExtras().put(NAME_MODIFIED_EXTRA, true);
        }
    }

    @Override
    protected void afterSave() {
        if (Boolean.TRUE.equals(getState().getExtra(NAME_MODIFIED_EXTRA))) {
            MethodRecalculation.recalculateMethodByQuery(
                Query.fromAll()
                    .where(HasIngredientsData.class.getName() + "/" + HasIngredientsData.INGREDIENTS_FIELD + " = ?", this),
                HasIngredientsData.INGREDIENT_NAMES_FIELD);
        }
    }

    @Override
    protected void afterDelete() {
        MethodRecalculation.recalculateMethodByQuery(
            Query.fromAll()
                .where(HasIngredientsData.class.getName() + "/" + HasIngredientsData.INGREDIENTS_FIELD + " = ?", this),
            HasIngredientsData.INGREDIENT_NAMES_FIELD);
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getNamePlainText();
    }
}
