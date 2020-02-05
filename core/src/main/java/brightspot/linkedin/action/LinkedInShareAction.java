package brightspot.linkedin.action;

import brightspot.core.action.actionbar.ActionBarItem;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Linkedin Share Action model. Object that is added to Action Bar. ViewModel in {@link LinkedinShareActionViewModel}
 */
@Recordable.DisplayName("Linkedin Share")
@ToolUi.Note("No options")
public class LinkedInShareAction extends Record implements ActionBarItem {

    @Override
    public String getLabel() {
        return "LinkedIn";
    }
}
