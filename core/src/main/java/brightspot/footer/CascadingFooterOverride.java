package brightspot.footer;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Select")
public class CascadingFooterOverride extends CascadingFooter {

    @Required
    @ToolUi.Unlabeled
    private Footer footer;

    public Footer getFooter() {
        return footer;
    }

    public void setFooter(Footer footer) {
        this.footer = footer;
    }

    @Override
    public Footer get() {
        return getFooter();
    }
}
