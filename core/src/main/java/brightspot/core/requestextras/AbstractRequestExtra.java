package brightspot.core.requestextras;

import javax.servlet.http.HttpServletRequest;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * A simple {@link RequestExtra} suitable to be managed in the CMS.
 */
@Recordable.Embedded
public abstract class AbstractRequestExtra extends Record implements RequestExtra {

    @Required
    private String internalName;

    @Recordable.Required
    @Recordable.Embedded
    @ToolUi.Unlabeled
    private RequestMatcher requestMatcher;

    @Override
    public boolean matches(HttpServletRequest request) {
        return requestMatcher != null && requestMatcher.matches(request);
    }

    @Override
    public boolean shouldContinueProcessing() {
        return requestMatcher == null || requestMatcher.shouldContinueProcessing();
    }
}
