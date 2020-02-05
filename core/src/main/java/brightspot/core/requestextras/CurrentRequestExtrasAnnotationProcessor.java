package brightspot.core.requestextras;

import javax.servlet.http.HttpServletRequest;

import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessor;

/**
 * See: {@link CurrentRequestExtras}.
 */
public class CurrentRequestExtrasAnnotationProcessor
    implements ServletViewRequestAnnotationProcessor<CurrentRequestExtras> {

    @Override
    public RequestExtras getValue(HttpServletRequest request, String fieldName, CurrentRequestExtras annotation) {
        return RequestExtras.getRequestExtras(request);
    }
}
