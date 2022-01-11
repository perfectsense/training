package brightspot.meal;

import java.util.ArrayList;
import java.util.List;

import brightspot.module.recipe.RecipeModulePlacementInline;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Course")
@Recordable.Embedded
public class MealCourse extends Record {

    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String name;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String summary;

    @CollectionMinimum(1)
    private List<RecipeModulePlacementInline> dishes;

    // --- Getters/setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<RecipeModulePlacementInline> getDishes() {
        if (dishes == null) {
            dishes = new ArrayList<>();
        }
        return dishes;
    }

    public void setDishes(List<RecipeModulePlacementInline> dishes) {
        this.dishes = dishes;
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return RichTextUtils.richTextToPlainText(getName());
    }
}
