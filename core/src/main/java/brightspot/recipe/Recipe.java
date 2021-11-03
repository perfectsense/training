package brightspot.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import brightspot.image.WebImage;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public class Recipe extends Content {

    @Indexed
    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String title;

    private String internalName;

    private Difficulty difficulty;

    @ToolUi.RichText(inline = false, toolbar = SmallRichTextToolbar.class)
    private String ingredients;

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class)
    private String directions;

    private WebImage image;

    private List<RecipeTag> recipeTags;

    private Integer prepTime;

    private Integer inactivePrepTime;

    private Integer cookTime;

    @DisplayName("Total Time")
    private Integer totalTimeOverride;

    // --- Getters/setters ---

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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public WebImage getImage() {
        return image;
    }

    public void setImage(WebImage image) {
        this.image = image;
    }

    public List<RecipeTag> getRecipeTags() {
        if (recipeTags == null) {
            recipeTags = new ArrayList<>();
        }
        return recipeTags;
    }

    public void setRecipeTags(List<RecipeTag> recipeTags) {
        this.recipeTags = recipeTags;
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
