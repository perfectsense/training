package brightspot.product;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.psddev.analytics.ui.AnalyticsWidget;
import com.psddev.cms.db.Content;
import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.cms.tool.content.UrlsWidget;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.dari.util.ObjectUtils;

public class ProductCollection extends Content implements ContentEditWidgetDisplay {

    private static final List<String> HIDDEN_WIDGETS = Arrays.asList(
        UrlsWidget.class.getName(),
        AnalyticsWidget.class.getName()
    );

    @DynamicPlaceholderMethod("getInternalNameFallback")
    private String internalName;

    private String title;

    public String getInternalName() {
        return Optional.ofNullable(internalName)
            .orElseGet(this::getInternalNameFallback);
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // --- Fallbacks ---

    private String getInternalNameFallback() {
        if (!ObjectUtils.isBlank(getTitle())) {
            return getTitle();
        }
        return "";
    }

    @Override
    public String getLabel() {
        return getTitle();
    }

    // --- ContentEditWidgetDisplay support ---

    @Override
    public boolean shouldDisplayContentEditWidget(String widgetName) {
        return !HIDDEN_WIDGETS.contains(widgetName);
    }
}
