package brightspot.core.text;

import java.util.Optional;

import brightspot.core.footer.FooterModuleType;
import brightspot.core.module.ModuleType;
import brightspot.core.tool.LargeRichTextToolbar;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Rich Text")
public class RichTextModule extends ModuleType implements FooterModuleType {

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class)
    @ToolUi.Unlabeled
    private String richText;

    public String getRichText() {
        return richText;
    }

    public void setRichText(String richText) {
        this.richText = richText;
    }

    @Override
    public String getLabel() {

        return Optional.ofNullable(getRichText())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::getFirstBodyParagraph)
            .map(RichTextUtils::richTextToPlainText)
            .orElse(super.getLabel());
    }
}
