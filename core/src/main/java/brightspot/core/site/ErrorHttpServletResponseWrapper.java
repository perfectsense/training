package brightspot.core.site;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Display not found and server error pages. See: {@link ErrorFilter}.
 */
class ErrorHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Set<ErrorHandler> errorHandlers;

    public ErrorHttpServletResponseWrapper(
        HttpServletRequest request,
        HttpServletResponse response,
        Set<ErrorHandler> errorHandlers) {
        super(response);
        this.request = request;
        this.response = response;
        this.errorHandlers = errorHandlers;
    }

    @Override
    public void sendError(int sc) throws IOException {
        response.setStatus(sc);
        getErrorHandlers().stream()
            .filter(Objects::nonNull)
            // Try all until one returns true
            .anyMatch(handler -> handler.handleStatusCode(request, response, sc));
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        sendError(sc);
    }

    private Set<ErrorHandler> getErrorHandlers() {
        return errorHandlers != null ? errorHandlers : Collections.emptySet();
    }
}
