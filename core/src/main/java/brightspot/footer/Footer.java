package brightspot.footer;

import brightspot.util.NoUrlsWidget;
import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.dari.db.Recordable;

/**
 * The {@link Footer} interface establishes the contract for all classes implementing a page's footer.
 * <p/>
 * For use in {@link brightspot.page.PageViewModel} and managed via {@link CascadingFooter} in
 * {@link brightspot.page.CascadingPageData}.
 */
public interface Footer extends
    ContentEditWidgetDisplay,
    NoUrlsWidget,
    Recordable {

}
