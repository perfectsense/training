package brightspot.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.image.WebImage;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.MoreStringUtils;
import brightspot.util.NoUrlsWidget;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.cms.ui.form.Note;
import com.psddev.dari.util.UnresolvedState;

public class Recipe extends Content implements
    NoUrlsWidget {

    public static final String COOK_TIME_FIELD = "cookTime";
    public static final String DIFFICULTY_FIELD = "difficulty";
    public static final String INACTIVE_PREP_TIME_FIELD = "inactivePrepTime";
    public static final String PREP_TIME_FIELD = "prepTime";
    public static final String RECIPE_TAG_NAMES_FIELD = "getRecipeTagNames";
    public static final String RECIPE_TAGS_FIELD = "recipeTags";
    public static final String TITLE_PLAIN_TEXT_FIELD = "getTitlePlainText";
    public static final String TOTAL_TIME_FIELD = "getTotalTime";

    private static final String TIMING_CLUSTER = "Timing";

    @Indexed
    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String title;

    @DynamicPlaceholderMethod("getInternalNameFallback")
    private String internalName;

    @Indexed
    private Difficulty difficulty;

    @ToolUi.RichText(inline = false, toolbar = SmallRichTextToolbar.class)
    private String ingredients;

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class)
    private String directions;

    private WebImage image;

    @Indexed
    private List<RecipeTag> recipeTags;

    @Indexed
    @Note("Value is in minutes")
    @ToolUi.Cluster(TIMING_CLUSTER)
    @ToolUi.CssClass("is-third")
    private Integer prepTime;

    @Indexed
    @Note("Value is in minutes")
    @ToolUi.Cluster(TIMING_CLUSTER)
    @ToolUi.CssClass("is-third")
    private Integer inactivePrepTime;

    @Indexed
    @Note("Value is in minutes")
    @ToolUi.Cluster(TIMING_CLUSTER)
    @ToolUi.CssClass("is-third")
    private Integer cookTime;

    @DisplayName("Total Time")
    @DynamicPlaceholderMethod("getTotalTimeFallback")
    @Note("Value is in minutes")
    @ToolUi.Cluster(TIMING_CLUSTER)
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

    @Indexed
    @ToolUi.Hidden
    public Integer getTotalTime() {
        return Optional.ofNullable(getTotalTimeOverride())
            .orElseGet(this::getTotalTimeFallback);
    }

    // --- Fallbacks ---

    private String getInternalNameFallback() {
        return RichTextUtils.richTextToPlainText(getTitle());
    }

    private Integer getTotalTimeFallback() {
        return Stream.of(
            getPrepTime(),
            getInactivePrepTime(),
            getCookTime())
            .filter(Objects::nonNull)
            .mapToInt(Integer::intValue)
            .reduce(0, Integer::sum);
    }

    // --- Indexes ---

    @Indexed
    @ToolUi.Hidden
    @ToolUi.Sortable
    public Integer getDifficultyLevel() {
        return Optional.ofNullable(getDifficulty())
            .map(Difficulty::getCode)
            .orElse(null);
    }

    @Indexed
    @ToolUi.Hidden
    public String getTitlePlainText() {
        return RichTextUtils.richTextToPlainText(getTitle());
    }

    @Indexed
    @ToolUi.Hidden
    public Set<String> getRecipeTagNames() {
        return UnresolvedState.resolveAndGet(this, Recipe::getRecipeTags)
            .stream()
            .map(RecipeTag::getName)
            .map(RichTextUtils::richTextToPlainText)
            .collect(Collectors.toSet());
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return MoreStringUtils.firstNonBlank(getInternalName(), this::getInternalNameFallback);
    }
}
