package brightspot.core.site;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.PageFilter;
import com.psddev.dari.util.HtmlWriter;
import org.slf4j.LoggerFactory;

public class ContentErrorHandler extends ErrorHandler {

    private Set<Integer> statusCodes;
    private Set<ErrorStatusCategory> statusCategories;
    private Set<String> exceptionClasses;
    private Content content;

    public ContentErrorHandler(
        Set<Integer> statusCodes,
        Set<ErrorStatusCategory> statusCategories,
        Set<String> exceptionClasses,
        Content content) {
        this.statusCodes = statusCodes;
        this.statusCategories = statusCategories;
        this.exceptionClasses = exceptionClasses;
        this.content = content;
    }

    public ContentErrorHandler() {
    }

    @Override
    public boolean handleStatusCode(HttpServletRequest request, HttpServletResponse response, int statusCode) {
        if (content != null && (getStatusCodes().contains(statusCode)) || matchesStatusCategories(statusCode)) {
            writeResponse(request, response);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleException(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
        if (content != null && Optional.ofNullable(exception)
            .map(Object::getClass)
            .map(Class::getSimpleName)
            .map(name -> getExceptionClasses().contains(name))
            .orElse(false)) {
            writeResponse(request, response);
            return true;
        }
        return false;
    }

    private void writeResponse(HttpServletRequest request, HttpServletResponse response) {
        try {
            PageFilter.Static.setMainObject(request, content);
            Writer writer = new HtmlWriter(response.getWriter());
            PageFilter.renderObject(request, response, writer, content);
        } catch (IOException | ServletException e) {
            LoggerFactory.getLogger(ContentErrorHandler.class).warn("Unable to render error page.", e);
        }
    }

    public Set<Integer> getStatusCodes() {
        if (statusCodes == null) {
            statusCodes = new HashSet<>();
        }
        return statusCodes;
    }

    public void setStatusCodes(Set<Integer> statusCodes) {
        this.statusCodes = statusCodes;
    }

    public Set<ErrorStatusCategory> getStatusCategories() {
        if (statusCategories == null) {
            statusCategories = new HashSet<>();
        }
        return statusCategories;
    }

    public void setStatusCategories(Set<ErrorStatusCategory> statusCategories) {
        this.statusCategories = statusCategories;
    }

    public Set<String> getExceptionClasses() {
        if (exceptionClasses == null) {
            exceptionClasses = new HashSet<>();
        }
        return exceptionClasses;
    }

    public void setExceptionClasses(Set<String> exceptionClasses) {
        this.exceptionClasses = exceptionClasses;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    private boolean matchesStatusCategories(int statusCode) {
        if (!getStatusCategories().isEmpty()) {
            return getStatusCategories()
                .stream()
                .anyMatch(category -> category.matches(statusCode));
        }
        return false;
    }

    @Override
    public String getLabel() {
        StringBuilder sb = new StringBuilder();

        if (!getStatusCodes().isEmpty()) {
            sb.append(getStatusCodes().stream().map(Object::toString).collect(Collectors.joining(", "))).append(" | ");
        } else if (!getStatusCategories().isEmpty()) {
            sb.append(getStatusCategories().stream().map(Object::toString).collect(Collectors.joining(", ")))
                .append(" | ");
        }

        if (!getExceptionClasses().isEmpty()) {
            sb.append(getExceptionClasses().stream().map(Object::toString).collect(Collectors.joining(", ")))
                .append(" | ");
        }

        if (content != null) {
            sb.append(content.getLabel());
        }

        return sb.toString();
    }
}
