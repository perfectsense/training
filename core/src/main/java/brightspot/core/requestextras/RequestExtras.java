package brightspot.core.requestextras;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import com.psddev.cms.db.PageFilter;
import com.psddev.cms.db.Site;
import com.psddev.dari.util.ClassFinder;

/**
 * ViewModel helper class to find {@link RequestExtra}s that match a request.
 */
public final class RequestExtras {

    private static final String REQUEST_ATTRIBUTE = RequestExtras.class.getName() + "/requestExtras";

    private final HttpServletRequest request;

    private Collection<RequestExtra> extras;

    private RequestExtras(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Create or retrieve the instance for the given request.
     */
    public static RequestExtras getRequestExtras(HttpServletRequest request) {
        Object obj = request.getAttribute(REQUEST_ATTRIBUTE);
        if (!(obj instanceof RequestExtras)) {
            obj = new RequestExtras(request);
            request.setAttribute(REQUEST_ATTRIBUTE, obj);
        }
        return (RequestExtras) obj;
    }

    /**
     * Find the list of {@link RequestExtra}s that are instances of the given class.
     */
    @SuppressWarnings("unchecked")
    public <T extends RequestExtra> List<T> getByClass(Class<T> requestExtraClass) {
        return findExtras()
            .stream()
            .filter(requestExtraClass::isInstance)
            .map(requestExtraClass::cast)
            .collect(Collectors.toList());
    }

    private Collection<RequestExtra> findExtras() {
        if (extras == null) {

            extras = new LinkedHashSet<>();
            Site site = PageFilter.Static.getSite(request);
            Object mainObject = PageFilter.Static.getMainObject(request);

            for (Class<? extends RequestExtrasProducer> cls : ClassFinder.findClasses(RequestExtrasProducer.class)) {
                try {
                    RequestExtrasProducer<?> producer = cls.newInstance();
                    List<?> list = producer.produce(site, mainObject);
                    if (list != null) {
                        for (Object extraObj : list) {
                            if (extraObj instanceof RequestExtra) {
                                RequestExtra extra = (RequestExtra) extraObj;
                                if (extra.matches(request)) {
                                    extras.add(extra);
                                    if (!extra.shouldContinueProcessing()) {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("Exception when finding RequestExtras from " + cls.getName(), e);
                }
            }
        }
        return extras;
    }
}
