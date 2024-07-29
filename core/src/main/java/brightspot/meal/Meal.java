package brightspot.meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import brightspot.difficulty.Difficulty;
import brightspot.difficulty.HasDifficultyWithField;
import brightspot.difficulty.HasDifficultyWithFieldData;
import brightspot.image.WebImage;
import brightspot.ingredient.HasIngredients;
import brightspot.ingredient.Ingredient;
import brightspot.recipe.Recipe;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.MoreStringUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;

@ToolUi.FieldDisplayOrder({
    "title",
    "internalName",
    "description",
    HasDifficultyWithFieldData.DIFFICULTY_FIELD
})
public class Meal extends Content implements
    HasDifficultyWithField,
    HasIngredients {

    public static final String TITLE_PLAIN_TEXT_FIELD = "getTitlePlainText";

    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String title;

    @DynamicPlaceholderMethod("getInternalNameFallback")
    private String internalName;

    @ToolUi.RichText(inline = false, toolbar = SmallRichTextToolbar.class)
    private String description;

    private WebImage image;

    @Required
    private List<MealCourse> courses;

    // --- Getters/setters ---

    /**
     * @return rich text
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    /**
     * @return rich text
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WebImage getImage() {
        return image;
    }

    public void setImage(WebImage image) {
        this.image = image;
    }

    public List<MealCourse> getCourses() {
        if (courses == null) {
            courses = new ArrayList<>();
        }
        return courses;
    }

    public void setCourses(List<MealCourse> courses) {
        this.courses = courses;
    }

    // --- Indexes ---

    @Indexed
    @ToolUi.Hidden
    public String getTitlePlainText() {
        return RichTextUtils.richTextToPlainText(getTitle());
    }

    // --- Fallbacks ---

    private String getInternalNameFallback() {
        return getTitlePlainText();
    }

    // --- Utility ---

    private List<Recipe> getRecipes() {
        return getCourses()
            .stream()
            .map(MealCourse::getRecipes)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    // --- HasDifficultyWithField support ---

    @Override
    public Difficulty getDifficultyFallback() {
        return getCourses()
            .stream()
            .map(MealCourse::getRecipes)
            .flatMap(List::stream)
            .map(Recipe::getDifficulty)
            .filter(Objects::nonNull)
            .reduce((lhs, rhs) -> lhs.getLevel() > rhs.getLevel() ? lhs : rhs)
            .orElse(null);
    }

    // --- HasIngredients support ---

    @Override
    public List<Ingredient> getIngredients() {
        return getRecipes()
            .stream()
            .flatMap(r -> r.getIngredients().stream())
            .distinct()
            .collect(Collectors.toList());
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return MoreStringUtils.firstNonBlank(getInternalName(), this::getInternalNameFallback);
    }
}
