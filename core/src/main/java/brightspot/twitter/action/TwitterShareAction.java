package brightspot.twitter.action;

import brightspot.core.action.actionbar.ActionBarItem;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Twitter Share Action model. Object that is added to Action Bar. ViewModel in {@link TwitterShareActionViewModel}
 */
@Recordable.DisplayName("Twitter Share")
@ToolUi.Note("No options")
public class TwitterShareAction extends Record implements ActionBarItem {

    @Override
    public String getLabel() {
        return "Twitter";
    }
}
