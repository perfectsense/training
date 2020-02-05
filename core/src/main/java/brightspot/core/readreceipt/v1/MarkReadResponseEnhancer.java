package brightspot.core.readreceipt.v1;

import java.util.Map;

import com.psddev.auth.AuthenticationEntity;
import com.psddev.dari.util.WebPageContext;

/**
 * Enhances the JSON response from {@link ReadReceiptAPIv1#doMarkRead(WebPageContext, UUID, AuthenticationEntity,
 * Date)}.
 */
public interface MarkReadResponseEnhancer {

    void enhanceResponse(
        Map<String, Object> responseJson,
        AuthenticationEntity authenticationEntity,
        WebPageContext page) throws Exception;
}
