package brightspot.core.navigation;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * A simple text (non-linked) navigation title class.
 */
@Recordable.Embedded
public class NavigationText
    extends Record implements NavigationItemTitle {

    @ToolUi.Unlabeled
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // NavigationItemTitle
    @Override
    public String getTitleText() {
        return getText();
    }
}
