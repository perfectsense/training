package brightspot.core.page;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.psddev.cms.db.PageFilter;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewModelCreator;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.ServletViewModelCreator;
import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessor;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.JspUtils;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.TypeDefinition;

/**
 * Create a {@link PageViewModel} in a ViewModel.
 */
public class CurrentPageViewModelAnnotationProcessor
    implements ServletViewRequestAnnotationProcessor<CurrentPageViewModel> {

    private static final LoadingCache<Class<?>, Class<? extends ViewModel<? super Object>>> PAGE_VM_CACHE = CacheBuilder
        .newBuilder()
        .maximumSize(500)
        .weakKeys()
        .build(new PageVMCacheLoader());

    @Override
    public Object getValue(HttpServletRequest request, String fieldName, CurrentPageViewModel annotation) {

        Class<?> viewModelClass = annotation.value();

        Class<? extends ViewModel<? super Object>> implementationClass = PAGE_VM_CACHE.getUnchecked(viewModelClass);

        Class<?> declaredModelClass = TypeDefinition.getInstance(implementationClass)
            .getInferredGenericTypeArgumentClass(ViewModel.class, 0);

        Object model = PageFilter.Static.getMainObject(request);
        if (model == null) {
            throw new RuntimeException("Main Object is not set!");
        }

        Class<?> modelClass = model.getClass();

        if (!declaredModelClass.isAssignableFrom(modelClass)) {
            throw new RuntimeException("Model is not a " + modelClass.getName() + '!');
        }

        ViewModelCreator vmCreator = new ServletViewModelCreator(request);

        ViewResponse viewResponse = new ViewResponse();
        try {
            return vmCreator.createViewModel(implementationClass, model, viewResponse);

        } catch (RuntimeException e) {
            // Logic duplicated from PageFilter, should live somewhere reusable
            // Process thrown ViewResponses
            ViewResponse vr = ViewResponse.findInExceptionChain(e);
            if (vr != null) {
                viewResponse = vr;
                HttpServletResponse response = PageContextFilter.Static.getResponseOrNull();
                if (response != null) {
                    updateViewResponse(request, response, vr);
                }
                return null;

            } else {
                throw e;
            }
        }
    }

    // Duplicated from PageFilter, should probably live somewhere reusable
    // Copies the information on the ViewResponse to the actual http servlet response.
    private static void updateViewResponse(
        HttpServletRequest request,
        HttpServletResponse response,
        ViewResponse viewResponse) {

        Integer status = viewResponse.getStatus();
        if (status != null) {
            response.setStatus(status);
        }

        for (Map.Entry<String, List<String>> entry : viewResponse.getHeaders().entrySet()) {

            String name = entry.getKey();
            List<String> values = entry.getValue();

            for (String value : values) {
                response.addHeader(name, value);
            }
        }

        viewResponse.getCookies().forEach(response::addCookie);
        viewResponse.getSignedCookies().forEach(cookie -> JspUtils.setSignedCookie(response, cookie));

        String redirectUrl = viewResponse.getRedirectUri();
        if (redirectUrl != null) {
            try {
                JspUtils.redirect(request, response, redirectUrl);
            } catch (IOException e) {
                // ignore
            }
        }
    }

    private static class PageVMCacheLoader extends CacheLoader<Class<?>, Class<? extends ViewModel<? super Object>>> {

        @Override
        public Class<? extends ViewModel<? super Object>> load(Class<?> vmClass) throws Exception {

            Set<Class<?>> concreteViewClasses = new HashSet<>(ClassFinder.findConcreteClasses(vmClass));

            // ClassFinder only finds sub-classes, so if the current viewClass is also concrete, add it to the set.
            if (!vmClass.isInterface() && !Modifier.isAbstract(vmClass.getModifiers())) {
                concreteViewClasses.add(vmClass);
            }

            Set<Class<?>> concreteViewModelClasses = concreteViewClasses
                .stream()
                .filter(ViewModel.class::isAssignableFrom)
                .collect(Collectors.toSet());

            // Eliminate any super classes if there are sub-class / super-class
            // combinations in the set since the sub-class takes precedence (Rule #3).
            Set<Class<?>> superClassesToRemove = new HashSet<>();

            for (Class<?> concreteClass : concreteViewModelClasses) {

                Set<Class<?>> superClasses = new HashSet<>();

                Class<?> superClass = concreteClass.getSuperclass();

                while (superClass != null) {
                    superClasses.add(superClass);
                    superClass = superClass.getSuperclass();
                }

                superClassesToRemove.addAll(superClasses);
            }

            concreteViewModelClasses.removeAll(superClassesToRemove);

            if (concreteViewModelClasses.isEmpty()) {
                throw new RuntimeException("No implementation of " + vmClass + " was found!");
            }

            if (concreteViewModelClasses.size() > 1) {
                throw new RuntimeException(
                    "More than one implementation of " + vmClass + " was found; Could not determine correct one.");
            }

            Class<?> implementationClass = concreteViewModelClasses.iterator().next();

            if (!ViewModel.class.isAssignableFrom(implementationClass)) {
                throw new RuntimeException(implementationClass.getName() + " isn't a ViewModel!");
            }

            return (Class<? extends ViewModel<? super Object>>) implementationClass;
        }
    }
}
