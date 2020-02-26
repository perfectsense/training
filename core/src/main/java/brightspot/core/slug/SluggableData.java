package brightspot.core.slug;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("sluggable.")
public class SluggableData extends Modification<Sluggable> {

    @ToolUi.Placeholder(dynamicText = "${content.getSluggableSlugFallback()}", editable = true)
    private String slug;

    @Indexed
    @ToolUi.Hidden
    public String getSlug() {
        if (slug == null) {
            return getOriginalObject().getSluggableSlugFallback();
        }
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
