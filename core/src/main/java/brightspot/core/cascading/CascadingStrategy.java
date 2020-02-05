package brightspot.core.cascading;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import brightspot.core.tool.CascadingStrategyModification;
import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.cms.db.Site;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Application;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Abstract strategy to determine how overrides of {@link Cascading} page elements are handled.
 */
@Recordable.Embedded
public abstract class CascadingStrategy extends Record {

    /**
     * Add items to the given list in order of precedence, starting with the individual asset level overrides and ending
     * with the global fallbacks.
     *
     * @param asset The asset.
     * @param site The current site.
     * @param queue The List to append results to.
     */
    protected abstract <T> void doResolve(Object asset, Site site, List<CascadingResult<T>> queue);

    /**
     * Descriptive text to display when this type is selected.
     */
    public abstract String getNoteHtml();

    /**
     * Return the entire unprocessed queue.
     */
    public final <T> List<CascadingResult<T>> getUnprocessedQueue(
        Object asset,
        Site site,
        Function<Object, Cascading<T>> getter) {
        List<CascadingResult<T>> queue = new ArrayList<>();
        doResolve(asset, site, queue);
        return preProcessQueue(queue);
    }

    /**
     * Resolve the value and its provider.
     */
    public final <T> CascadingResult<T> resolve(Object asset, Site site, Function<Object, Cascading<T>> getter) {
        List<CascadingResult<T>> queue = new ArrayList<>();
        doResolve(asset, site, queue);
        return processQueue(queue, getter);
    }

    /**
     * Resolve the value and its provider, but ignore the provided asset - only look at the parent and above.
     */
    public final <T> CascadingResult<T> resolveParent(Object asset, Site site, Function<Object, Cascading<T>> getter) {
        List<CascadingResult<T>> queue = new ArrayList<>();
        doResolve(asset, site, queue);
        queue = preProcessQueue(queue);
        if (!queue.isEmpty()) {
            CascadingResult<T> head = queue.get(0);
            if (head != null && head.getProviderOriginalObject().isPresent() && (asset == null
                || head.getProviderOriginalObject().get().equals(asset))) {
                queue.remove(0);
            }
        }
        return processQueue(queue, getter);
    }

    private <T> List<CascadingResult<T>> preProcessQueue(List<CascadingResult<T>> queue) {
        return queue.stream()
            .filter(Objects::nonNull)
            .filter(result -> result.getProvider().isPresent())
            .collect(Collectors.toList());
    }

    private <T> CascadingResult<T> processQueue(List<CascadingResult<T>> queue, Function<Object, Cascading<T>> getter) {

        Optional<CascadingResult<T>> filteredQueue = preProcessQueue(queue).stream()
            .map(result -> result.apply(getter))
            .reduce((overriding, existing) -> reduceQueue(overriding, existing, getter));

        return filteredQueue.orElse(null);
    }

    private <T> CascadingResult<T> reduceQueue(
        CascadingResult<T> overriding,
        CascadingResult<T> existing,
        Function<Object, Cascading<T>> getter) {

        Cascading<T> overridingResult = overriding.applyAndGetResult(getter);
        Cascading<T> existingResult = existing.applyAndGetResult(getter);

        if (overridingResult == null) {
            return existing;
        }

        if (overridingResult instanceof CascadingList && existingResult instanceof CascadingList) {
            ((CascadingList<T>) overridingResult).merge((CascadingList<T>) existingResult);
        }

        return overriding;
    }

    public <T> T get(Object asset, Site site, Function<Object, Cascading<T>> getter) {
        return Optional.ofNullable(resolve(asset, site, getter))
            .map(CascadingResult::getResult)
            .map(Cascading::get)
            .orElse(null);
    }

    public <T> Object getProvider(Object asset, Site site, Function<Object, Cascading<T>> getter) {
        return Optional.ofNullable(resolve(asset, site, getter))
            .map(CascadingResult::getProviderOriginalObject)
            .orElse(null);
    }

    // Note that this only checks {@link CmsTool} at this time, but
    // asset and site are provided for future proofing.
    public static CascadingStrategy getInstance(Object asset, Site site) {
        return Optional.ofNullable(Application.Static.getInstance(CmsTool.class)
            .as(CascadingStrategyModification.class)
            .getCascadingStrategy())
            .orElse(createDefault());
    }

    public static CascadingStrategy createDefault() {
        return DefaultImplementationSupplier.createDefault(CascadingStrategy.class, DefaultCascadingStrategy.class);
    }
}
