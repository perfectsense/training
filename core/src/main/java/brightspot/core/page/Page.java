package brightspot.core.page;

import brightspot.core.cascading.CascadingPageElements;
import com.psddev.cms.db.Seo;

/**
 * The {@link Page} interface should be implemented by all content types that are displayed as a stand-alone page.
 * <p/>
 */
public interface Page extends CascadingPageElements, Seo {

    String INTERNAL_NAME = "brightspot.core.page.Page";
}
