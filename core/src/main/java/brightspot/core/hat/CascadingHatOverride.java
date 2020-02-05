package brightspot.core.hat;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Select")
public class CascadingHatOverride extends CascadingHat {

    @Required
    @ToolUi.Unlabeled
    private Hat hat;

    public Hat getHat() {
        return hat;
    }

    public void setHat(Hat hat) {
        this.hat = hat;
    }

    @Override
    public Hat get() {
        return getHat();
    }
}
