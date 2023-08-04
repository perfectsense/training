package brightspot.image;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Select")
public class CascadingWebImageOverride extends CascadingWebImage {

    @Required
    @ToolUi.Unlabeled
    private WebImage webImage;

    public WebImage getWebImage() {
        return webImage;
    }

    public void setWebImage(WebImage webImage) {
        this.webImage = webImage;
    }

    @Override
    public WebImage get() {
        return getWebImage();
    }
}
