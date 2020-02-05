package brightspot.core.requestextras;

import javax.servlet.http.HttpServletRequest;

/**
 * Extra objects associated with a request. For example, {@link brightspot.core.requestextras.headelement.CustomHeadElements}
 * or {@link brightspot.core.requestextras.responseheader.CustomResponseHeaders}.
 *
 * Implement this interface as well as an accompanying {@link RequestExtrasProducer}, then use {@link RequestExtras} to
 * find the objects in the ViewModel.
 */
public interface RequestExtra {

    boolean matches(HttpServletRequest request);

    boolean shouldContinueProcessing();

}
