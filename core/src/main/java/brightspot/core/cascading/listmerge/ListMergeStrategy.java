package brightspot.core.cascading.listmerge;

import java.util.List;

/**
 * Insert an overriding list into the existing list
 */
public interface ListMergeStrategy<T> {

    List<T> merge(List<T> overriding, List<T> existing);

}
