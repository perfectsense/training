package brightspot.core.taxon;

import java.util.Set;

import com.psddev.cms.db.Taxon;

/**
 * Supports the use of multi-hierarchical structures
 */
public interface ExpressTaxon<T extends ExpressTaxon<T>> extends Taxon {

    Set<T> getParents();
}
