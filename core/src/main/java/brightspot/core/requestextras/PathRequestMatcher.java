package brightspot.core.requestextras;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import brightspot.core.page.Page;
import com.psddev.cms.db.PageFilter;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.StringUtils;

/**
 * Simple {@link RequestMatcher} that matches a request against common request parameters.
 */
public class PathRequestMatcher extends RequestMatcher {

    @ToolUi.NoteHtml("Only match the selected HTTP method(s). <br />Leave blank to match everything.")
    @DisplayName("HTTP Methods")
    private Set<HttpMethodName> httpMethods;

    @ToolUi.NoteHtml("Regular expression. For example: \"/stories/.*param=value.*\". Any URL matching this expression will match. The entire pattern must match, so include .* if necessary. <br />Leave blank to match everything.")
    private String urlPattern;

    @ToolUi.NoteHtml("Only match for the selected content types. <br />Leave blank to match everything.")
    @Where("groups = " + Page.INTERNAL_NAME + " and isAbstract = false")
    @ToolUi.DropDown
    private Set<ObjectType> contentTypes;

    @ToolUi.NoteHtml("Stop processing other rules after matching this one?")
    private boolean stopProcessing;

    @Override
    public boolean matches(HttpServletRequest request) {
        return matchesContentTypes(request)
            && matchesUrlPattern(request)
            && matchesHttpMethod(request);
    }

    private boolean matchesHttpMethod(HttpServletRequest request) {
        return getHttpMethods().isEmpty()
            || getHttpMethodNames().contains(request.getMethod());
    }

    private boolean matchesContentTypes(HttpServletRequest request) {
        if (getContentTypes().isEmpty()) {
            return true;
        }
        Object mainObject = PageFilter.Static.getMainObject(request);
        if (!(mainObject instanceof Recordable)) {
            return false;
        }
        ObjectType mainObjectType = State.getInstance(mainObject).getType();
        if (getContentTypes().contains(mainObjectType)) {
            return true;
        }
        for (ObjectType type : getContentTypes()) {
            if (mainObjectType.getGroups().contains(type.getInternalName())) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesUrlPattern(HttpServletRequest request) {

        if (StringUtils.isBlank(getUrlPattern())) {
            return true;
        }

        String uriPath = request.getRequestURI();
        String queryString = request.getQueryString();
        if (!StringUtils.isBlank(queryString)) {
            uriPath += '?' + queryString;
        }

        return StringUtils.matches(uriPath, getUrlPattern());
    }

    @Override
    protected void onValidate() {
        if (!StringUtils.isBlank(urlPattern)) {
            //Throw an exception if the pattern is invalid.
            //noinspection ResultOfMethodCallIgnored
            Pattern.compile(urlPattern);
        }
    }

    public Set<HttpMethodName> getHttpMethods() {
        if (httpMethods == null) {
            httpMethods = new HashSet<>();
        }
        return httpMethods;
    }

    public Set<String> getHttpMethodNames() {
        return getHttpMethods()
            .stream()
            .map(Enum::name)
            .collect(Collectors.toSet());
    }

    public void setHttpMethods(Set<HttpMethodName> httpMethods) {
        this.httpMethods = httpMethods;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public Set<ObjectType> getContentTypes() {
        if (contentTypes == null) {
            contentTypes = new HashSet<>();
        }
        return contentTypes;
    }

    public void setContentTypes(Set<ObjectType> contentTypes) {
        this.contentTypes = contentTypes;
    }

    @Override
    public boolean shouldContinueProcessing() {
        return !stopProcessing;
    }

    public void setContinueProcessing(boolean stopProcessing) {
        this.stopProcessing = stopProcessing;
    }
}
