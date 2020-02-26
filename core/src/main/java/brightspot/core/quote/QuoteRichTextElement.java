package brightspot.core.quote;

import java.util.LinkedHashMap;
import java.util.Map;

import brightspot.core.image.ImageOption;
import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Quote")
@RichTextElement.Tag(value = QuoteRichTextElement.TAG_NAME,
    block = true,
    initialBody = "Quote",
    preview = true,
    position = -60,
    readOnly = true,
    root = true,
    keymaps = { "Shift-Ctrl-'" },
    tooltip = "Add Quote (Shift-Ctrl-')"
)
@ToolUi.IconName("format_quote")
public class QuoteRichTextElement extends RichTextElement {

    public static final String TAG_NAME = "bsp-quote";

    private static final String STATE_ATTRIBUTE = "data-state";

    private String quote;

    private String attribution;

    private ImageOption image;

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

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
    public String toBody() {
        return quote != null ? quote : "";
    }

    @Override
    public void fromBody(String body) {
        // do nothing
    }
}
