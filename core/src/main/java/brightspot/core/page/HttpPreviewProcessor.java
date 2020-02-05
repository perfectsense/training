package brightspot.core.page;

import javax.servlet.http.HttpServletRequest;

import com.psddev.cms.db.PageFilter;
import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessor;

/**
 * See: {@link HttpPreview}. Returns true if HTTP Request is a CMS Preview
 */
public class HttpPreviewProcessor implements ServletViewRequestAnnotationProcessor<HttpPreview> {

    @Override
    public Object getValue(HttpServletRequest request, String fieldName, HttpPreview httpPreview) {
        return PageFilter.Static.isPreview(request);
    }
}
