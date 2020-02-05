package brightspot.core.hierarchy;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import brightspot.core.page.TypeSpecificCascadingPageElements;
import brightspot.core.taxon.ExpressTaxon;

public interface Hierarchy extends Hierarchical,
    ExpressTaxon<Hierarchy>,
    TypeSpecificCascadingPageElements {

    Hierarchy getParent();

    @Override
    default Set<Hierarchy> getParents() {
        return Optional.ofNullable(getParent()).map(Collections::singleton).orElse(null);
    }
}
