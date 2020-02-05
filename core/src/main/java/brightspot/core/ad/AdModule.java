package brightspot.core.ad;

import java.util.Arrays;
import java.util.List;

import com.psddev.cms.db.Managed;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.cms.tool.content.UrlsWidget;
import com.psddev.dari.db.Record;

/**
 * The {@link AdModule} provides an abstract {@link Record} class for an Ad.
 */
public abstract class AdModule extends Record implements
    ContentEditWidgetDisplay,
    Managed {

    private static final List<String> HIDDEN_WIDGETS = Arrays.asList(
        UrlsWidget.class.getName());

    /**
     * @return A unique Name for this AdModule defined by {@link #getLabel()}.
     */
    @Indexed(unique = true)
    @ToolUi.Hidden
    public String getUniqueName() {
        //Using getLabel instead of indexing getLabel so we can use overridden getLabels in instances like oneOff
        return getLabel();
    }

    /**
     * Overrides {@link ContentEditWidgetDisplay#shouldDisplayContentEditWidget(String)} returning true for the widgets
     * defined in {@link #HIDDEN_WIDGETS}.
     */
    @Override
    public boolean shouldDisplayContentEditWidget(String widgetName) {
        return !HIDDEN_WIDGETS.contains(widgetName);
    }

    @Override
    public String createManagedEditUrl(ToolPageContext page) {
        return page.objectUrl("/admin/sites.jsp", SiteSettings.get(page.getSite(), f -> f));
    }
}
