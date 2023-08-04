package brightspot.image;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Alteration;

@ToolUi.FieldDisplayPreview({
    "getCaption",
    "getCredit",
    "getSource",
    "getCopyrightNotice",
    "hasTags.tags",
    "cms.content.updateDate",
    "cms.content.updateUser" })
public class WebImageAlteration extends Alteration<WebImage> {

}
