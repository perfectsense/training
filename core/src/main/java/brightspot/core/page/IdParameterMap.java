package brightspot.core.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.psddev.dari.util.ObjectUtils;

/**
 * See {@link HttpIdPrefixedParameters}. Provides access to ID-prefixed HTTP parameters.
 */
public final class IdParameterMap {

    private final Map<UUID, List<String>> values;

    /**
     * Instantiates the object with a map of ID to the values. For example, the querystring
     *
     * <pre>?0000015f-26ad-d399-a5ff-aefff3e20000-page=3&00000152-4742-dc98-a173-4fc66d6b0000-page=2</pre>
     *
     * when parsed by {@link HttpIdPrefixedParametersProcessor} would result in a map like
     *
     * <pre>
     * {
     *   0000015f-26ad-d399-a5ff-aefff3e20000 : [3],
     *   00000152-4742-dc98-a173-4fc66d6b0000 : [2]
     * }
     * </pre>
     */
    public IdParameterMap(Map<UUID, List<String>> values) {
        this.values = new HashMap<>(values);
    }

    /**
     * Return the first value supplied for the given ID.
     */
    public String getFirstById(UUID id) {
        List<String> vals = values.get(id);

        return vals != null && !vals.isEmpty()
            ? vals.get(0)
            : null;
    }

    public <T> T getFirstById(Class<T> returnClass, UUID id) {
        return ObjectUtils.to(returnClass, getFirstById(id));
    }

    /**
     * Return all values supplied for the given ID.
     */
    public List<String> getAllById(UUID id) {
        List<String> vals = values.get(id);

        return vals != null
            ? new ArrayList<>(vals)
            : Collections.emptyList();
    }

    public <T> List<T> getAllById(Class<T> returnClass, UUID id) {
        return getAllById(id)
            .stream()
            .map(p -> ObjectUtils.to(returnClass, p))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
