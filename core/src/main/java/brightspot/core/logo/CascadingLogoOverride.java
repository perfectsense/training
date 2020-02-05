package brightspot.core.logo;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Select")
public class CascadingLogoOverride extends CascadingLogo {

    @Required
    @ToolUi.Unlabeled
    private Logo logo;

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    @Override
    public Logo get() {
        return getLogo();
    }
}
