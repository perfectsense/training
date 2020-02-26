package brightspot.pinterest.action;

import brightspot.core.action.actionbar.ActionBarItem;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Pinterest Share Action model. Object that is added to Action Bar. ViewModel in {@link PinterestShareActionViewModel}
 */
@Recordable.DisplayName("Pinterest Share")
@ToolUi.Note("No options")
public class PinterestShareAction extends Record implements ActionBarItem {

    @Override
    public String getLabel() {
        return "Pinterest";
    }
}
