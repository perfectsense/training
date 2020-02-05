package brightspot.core.navigation;

import java.util.ArrayList;
import java.util.List;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * The {@link TopNavigationItem} has no parent {@link NavigationItem} and holds a list of {@link SubNavigationItem} that
 * fall under its categorizing title.
 */
@Recordable.DisplayName("Simple Navigation Item")
@Recordable.Embedded
public class TopNavigationItem extends Record implements NavigationItem {

    @Required
    private NavigationItemTitle title;

    @ToolUi.Tab("Sub-Navigation")
    @ToolUi.Unlabeled
    private List<SubNavigationItem> subNavigation;

    public NavigationItemTitle getTitle() {
        return title;
    }

    public void setTitle(NavigationItemTitle title) {
        this.title = title;
    }

    public List<SubNavigationItem> getSubNavigation() {
        if (subNavigation == null) {
            subNavigation = new ArrayList<>();
        }
        return subNavigation;
    }

    public void setSubNavigation(List<SubNavigationItem> subNavigation) {
        this.subNavigation = subNavigation;
    }

    @Override
    public String getLabel() {
        return title != null ? title.getTitleText() : super.getLabel();
    }
}
