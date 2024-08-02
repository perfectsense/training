package brightspot.meal;

import java.util.ArrayList;
import java.util.List;

import brightspot.recipe.Recipe;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class MealCourse extends Record {

    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String name;

    @ToolUi.RichText(inline = false, toolbar = SmallRichTextToolbar.class)
    private String summary;

    @Required
    private List<Recipe> recipes;

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

    /**
     * @return rich text
     */
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Recipe> getRecipes() {
        if (recipes == null) {
            recipes = new ArrayList<>();
        }
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
