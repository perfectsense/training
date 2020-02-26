package brightspot.core.readreceipt.v1;

import java.util.Map;

import com.psddev.auth.AuthenticationEntity;
import com.psddev.dari.util.WebPageContext;

/**
 * Enhances the JSON response from {@link ReadReceiptAPIv1#doMarkUnread(WebPageContext, UUID, AuthenticationEntity)}.
 */
public interface MarkUnreadResponseEnhancer {

    void enhanceResponse(
        Map<String, Object> responseJson,
        AuthenticationEntity authenticationEntity,
        WebPageContext page) throws Exception;
}
