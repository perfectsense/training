package brightspot.recipe;

import brightspot.image.WebImage;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class RecipeStep extends Record {

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String heading;

    @Required
    @ToolUi.RichText(inline = false, toolbar = SmallRichTextToolbar.class)
    private String directions;

    private WebImage image;

    /**
     * @return rich text
     */
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    /**
     * @return rich text
     */
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
}
