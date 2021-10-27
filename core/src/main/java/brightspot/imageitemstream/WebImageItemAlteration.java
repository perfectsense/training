package brightspot.imageitemstream;

import brightspot.rte.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.crosslinker.db.Crosslinkable;
import com.psddev.dari.db.Alteration;

public class WebImageItemAlteration extends Alteration<WebImageItem> {

    @InternalName("webImage.captionOverride")
    @Crosslinkable.Crosslinked
    @DisplayName("Caption")
    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${modification.getCaptionFallback()}", editable = true)
    private String captionOverride;
}
