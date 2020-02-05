package brightspot.core.cache;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;
import com.psddev.cms.view.ViewModelOverlay;
import com.psddev.dari.util.PageContextFilter;

/**
 * Adds an "isCached" {@code boolean} value to any {@link com.psddev.cms.view.ViewMap}.
 */
public class CacheControlViewModelOverlay implements ViewModelOverlay {

    @Override
    public Map<String, Supplier<Object>> create(Object o, String s) {
        return ImmutableMap.of(
            "isCached", () -> CacheControlFilter.isResponseCached(
                PageContextFilter.Static.getRequest(), PageContextFilter.Static.getResponse())
        );
    }
}
