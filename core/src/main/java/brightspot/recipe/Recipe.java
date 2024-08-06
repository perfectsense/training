package brightspot.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import brightspot.image.WebImage;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public class Recipe extends Content {

    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String title;

    private String internalName;

    @ToolUi.RichText(inline = false, toolbar = SmallRichTextToolbar.class)
    private String description;

    private WebImage image;

    @Required
    private List<RecipeIngredient> recipeIngredients;

    @Required
    private List<RecipeStep> steps;

    private Integer prepTime;

    private Integer inactivePrepTime;

    private Integer cookTime;

    private Integer totalTimeOverride;

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

    public List<RecipeIngredient> getRecipeIngredients() {
        if (recipeIngredients == null) {
            recipeIngredients = new ArrayList<>();
        }
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<RecipeStep> getSteps() {
        if (steps == null) {
            steps = new ArrayList<>();
        }
        return steps;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getInactivePrepTime() {
        return inactivePrepTime;
    }

    public void setInactivePrepTime(Integer inactivePrepTime) {
        this.inactivePrepTime = inactivePrepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public Integer getTotalTimeOverride() {
        return totalTimeOverride;
    }

    public void setTotalTimeOverride(Integer totalTimeOverride) {
        this.totalTimeOverride = totalTimeOverride;
    }

    // --- API methods ---

    public Integer getTotalTime() {
        return Optional.ofNullable(getTotalTimeOverride())
            .orElseGet(this::getTotalTimeFallback);
    }

    // --- Fallbacks ---

    private Integer getTotalTimeFallback() {
        return Stream.of(
                getPrepTime(),
                getInactivePrepTime(),
                getCookTime())
            .filter(Objects::nonNull)
            .mapToInt(Integer::intValue)
            .reduce(0, Integer::sum);
    }
}
