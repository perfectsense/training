package brightspot.page;

import brightspot.image.WebImageAsset;
import com.psddev.dari.util.Substitution;

public class PageHeadingSubstitution extends PageHeading implements
        ModulePageLead,
        Substitution {

    @Override
    public WebImageAsset getModulePageLeadImage() {
        return getBackgroundImage();
    }
}
