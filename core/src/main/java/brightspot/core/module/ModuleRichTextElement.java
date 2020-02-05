package brightspot.core.module;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Module")
@RichTextElement.Tag(value = ModuleRichTextElement.TAG_NAME,
    block = true,
    initialBody = "Module",
    preview = true,
    position = -10.0,
    readOnly = true,
    root = true,
    tooltip = "Add Module",
    menu = "Enhancements"
)
@ToolUi.IconName("widgets")
public class ModuleRichTextElement extends RichTextElement {

    public static final String TAG_NAME = "bsp-module";

    private static final String STATE_ATTRIBUTE = "data-state";

    @Required
    private ModuleType module;

    public ModuleType getModule() {
        return module;
    }

    public void setModule(ModuleType module) {
        this.module = module;
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

    /**
     * TODO: Make this better
     *
     * @param page
     * @throws IOException
     */
    @Override
    public void writePreviewHtml(ToolPageContext page) throws IOException {
        if (module != null) {
            page.writeStart("strong").writeHtml(module.getLabel()).writeEnd();
        }
    }
}
