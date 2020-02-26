package brightspot.core.anchor;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

public interface Anchorage extends Recordable {

    default Set<Anchorable> getAnchors() {
        return Collections.emptySet();
    }

    static Set<Anchorable> getAnchorsForObject(Object object) {

        if (object instanceof Recordable) {

            Recordable recordable = (Recordable) object;

            if (recordable.isInstantiableTo(Anchorage.class)) {

                return Optional.ofNullable(recordable.as(Anchorage.class))
                    .map(Anchorage::getAnchors)
                    .orElseGet(Collections::emptySet)
                    .stream()
                    .filter(anchorable -> !ObjectUtils.isBlank(anchorable.getAnchorableAnchor()))
                    .collect(Collectors.toSet());

            } else if (recordable.isInstantiableTo(Anchorable.class)) {

                return Optional.ofNullable(recordable.as(Anchorable.class))
                    .filter(anchorable -> !ObjectUtils.isBlank(anchorable.getAnchorableAnchor()))
                    .map(Collections::singleton)
                    .orElseGet(Collections::emptySet);
            }
        }

        return Collections.emptySet();
    }
}
