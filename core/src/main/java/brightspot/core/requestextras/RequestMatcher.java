package brightspot.core.requestextras;

import javax.servlet.http.HttpServletRequest;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Editorially managed request matcher. See {@link AbstractRequestExtra} for usage.
 */
@Recordable.Embedded
public abstract class RequestMatcher extends Record {

    public abstract boolean matches(HttpServletRequest request);

    public abstract boolean shouldContinueProcessing();

}
