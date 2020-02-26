package brightspot.core.navigation;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Select")
public class CascadingNavigationOverride extends CascadingNavigation {

    @Required
    @ToolUi.Unlabeled
    private Navigation navigation;

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    @Override
    public Navigation get() {
        return getNavigation();
    }
}
