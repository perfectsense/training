package brightspot.meal;

import java.util.ArrayList;
import java.util.List;

import brightspot.image.WebImage;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.MoreStringUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;

public class Meal extends Content {

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

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return MoreStringUtils.firstNonBlank(getInternalName(), this::getInternalNameFallback);
    }
}
