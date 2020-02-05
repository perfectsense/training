package brightspot.tumblr.action;

import brightspot.core.action.actionbar.ActionBarItem;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Tumblr Share Action model. Object that is added to Action Bar. ViewModel in {@link TumblrShareActionViewModel}
 */
@Recordable.DisplayName("Tumblr Share")
@ToolUi.Note("No options")
public class TumblrShareAction extends Record implements ActionBarItem {

    @Override
    public String getLabel() {
        return "Tumblr";
    }
}
