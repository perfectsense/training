package brightspot.core.action.print;

import brightspot.core.action.actionbar.ActionBarItem;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Print")
@ToolUi.Note("No options")
public class PrintAction extends Record implements ActionBarItem {

    @Override
    public String getLabel() {
        return "Print";
    }
}
