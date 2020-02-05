package brightspot.core.page.opengraph.image;

import brightspot.core.image.ImageOption;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;

public class OpenGraphImageSettingsModification extends Modification<SiteSettings> {

    @ToolUi.Cluster("Open Graph")
    @ToolUi.Tab("Front-End")
    @ToolUi.Placeholder("None")
    private ImageOption defaultOpenGraphImage;

    public ImageOption getDefaultOpenGraphImage() {
        return defaultOpenGraphImage;
    }

    public void setDefaultOpenGraphImage(ImageOption defaultOpenGraphImage) {
        this.defaultOpenGraphImage = defaultOpenGraphImage;
    }
}
