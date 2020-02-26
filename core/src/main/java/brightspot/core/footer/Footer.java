package brightspot.core.footer;

import java.util.Arrays;
import java.util.List;

import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.cms.tool.content.UrlsWidget;
import com.psddev.dari.db.Recordable;

/**
 * The {@link Footer} interface establishes the contract for all classes implementing a page's footer.
 * <p/>
 * For use in {@link brightspot.core.page.PageViewModel} and managed via {@link CascadingFooter} in {@link
 * brightspot.core.page.CascadingPageData}.
 */
public interface Footer extends
    ContentEditWidgetDisplay,
    Recordable {

    List<String> HIDDEN_WIDGETS = Arrays.asList(
        UrlsWidget.class.getName());

    @Override
    default boolean shouldDisplayContentEditWidget(String widgetName) {
        return !HIDDEN_WIDGETS.contains(widgetName);
    }
}
