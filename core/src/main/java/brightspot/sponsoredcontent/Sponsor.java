package brightspot.sponsoredcontent;

import brightspot.image.WebImageAsset;
import brightspot.link.Link;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public class Sponsor extends Content implements ContentSponsor {

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String displayName;

    private WebImageAsset logo;

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String description;

    private Link callToAction;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public WebImageAsset getLogo() {
        return logo;
    }

    public void setLogo(WebImageAsset logo) {
        this.logo = logo;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Link getCallToAction() {
        return callToAction;
    }

    public void setCallToAction(Link callToAction) {
        this.callToAction = callToAction;
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return RichTextUtils.richTextToPlainText(getDisplayName());
    }
}
