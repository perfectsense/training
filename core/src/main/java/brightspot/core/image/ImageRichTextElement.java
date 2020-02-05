package brightspot.core.image;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Image")
@RichTextElement.Tag(value = ImageRichTextElement.TAG_NAME,
    block = true,
    initialBody = "Image",
    position = -50.0,
    preview = true,
    readOnly = true,
    root = true,
    keymaps = { "Shift-Ctrl-I" },
    tooltip = "Add Image (Shift-Ctrl-I)",
    menu = "Enhancements"
)
@ToolUi.IconName(Image.ICON_NAME)
public class ImageRichTextElement extends RichTextElement {

    public static final String TAG_NAME = "bsp-image";

    private static final String STATE_ATTRIBUTE = "data-state";

    @Required
    @DisplayName("Image")
    private ImageOption image = ImageOption.createDefault();

    public ImageOption getImage() {
        return image;
    }

    public void setImage(ImageOption image) {
        this.image = image;
    }

    @Override
    public void fromAttributes(Map<String, String> attributes) {
        if (attributes != null) {
            String stateString = attributes.get(STATE_ATTRIBUTE);
            if (stateString != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> simpleValues = (Map<String, Object>) ObjectUtils.fromJson(stateString);
                getState().setValues(simpleValues);
            }
        }
    }

    @Override
    public Map<String, String> toAttributes() {

        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put(STATE_ATTRIBUTE, ObjectUtils.toJson(getState().getSimpleValues()));
        return attributes;
    }

    @Override
    public void fromBody(String body) {
        // do nothing
    }

    @Override
    public String toBody() {
        return Optional.ofNullable(getImage())
            .map(ImageOption::getLabel)
            .orElse("Image");
    }

    @Override
    public void writePreviewHtml(ToolPageContext page) throws IOException {

        ImageOption image = Optional.ofNullable(getImage()).orElse(null);

        if (image != null) {
            String imageUrl = page.getPreviewThumbnailUrl(image);
            if (imageUrl != null) {
                page.writeElement("img", "src", imageUrl, "height", 300);
            }
        }
    }
}
