package brightspot.core.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.cms.tool.content.UrlsWidget;

/**
 * Class representing entire navigation bar.
 */
public class PageNavigation extends Content implements
    ContentEditWidgetDisplay,
    Navigation {

    private static final List<String> HIDDEN_WIDGETS = Arrays.asList(
        UrlsWidget.class.getName());

    @IgnoredIfEmbedded
    @Required
    private String name;

    @ToolUi.Unlabeled
    private List<NavigationItem> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<NavigationItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<NavigationItem> items) {
        this.items = items;
    }

    @Override
    public boolean shouldDisplayContentEditWidget(String widgetName) {
        return !HIDDEN_WIDGETS.contains(widgetName);
    }
}
