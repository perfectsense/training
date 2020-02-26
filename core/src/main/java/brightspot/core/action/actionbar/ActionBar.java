package brightspot.core.action.actionbar;

import java.util.ArrayList;
import java.util.List;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;

/**
 * Configured in {@link com.psddev.cms.db.SiteSettings} -> action bar. Create and select an Action Bar, then select the
 * type that action bar should be displayed with.
 */
public class ActionBar extends Record {

    public static final String ACTION_BAR_CONTEXT_ATTRIBUTE = "action.bar.context.attribute";

    @IgnoredIfEmbedded
    private String name;

    @ToolUi.Embedded
    private List<ActionBarItem> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ActionBarItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<ActionBarItem> items) {
        this.items = items;
    }
}
