package brightspot.core.page;

import javax.servlet.http.HttpServletRequest;

import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessor;

/**
 * See: {@link HttpQueryString}. Produces the entire HTTP query string
 */
public class HttpQueryStringProcessor implements ServletViewRequestAnnotationProcessor<HttpQueryString> {

    @Override
    public Object getValue(HttpServletRequest request, String fieldName, HttpQueryString annotation) {
        return request.getQueryString();
    }
}
