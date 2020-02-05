package brightspot.core.requestextras.responseheader;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.requestextras.AbstractRequestExtra;

/**
 * Custom HTTP response headers to be added to a matching request.
 */
public class CustomResponseHeaders extends AbstractRequestExtra {

    private List<ResponseHeader> responseHeaders;

    public List<ResponseHeader> getResponseHeaders() {
        if (responseHeaders == null) {
            responseHeaders = new ArrayList<>();
        }
        return responseHeaders;
    }

    public void setResponseHeaders(List<ResponseHeader> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }
}
