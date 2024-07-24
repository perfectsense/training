package brightspot.meal;

import brightspot.image.WebImage;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public class Meal extends Content {

    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String title;

    private String internalName;

    @ToolUi.RichText(inline = false, toolbar = SmallRichTextToolbar.class)
    private String description;

    private WebImage image;

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
}
