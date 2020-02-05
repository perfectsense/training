package brightspot.core.cascading;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.cascading.listmerge.ListMergeStrategy;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * This class must be extended to concretely define the "items" field. See {@link brightspot.core.module.CascadingModuleTypeList}
 * for an example implementation.
 */
@Recordable.Embedded
public abstract class CascadingList<T> extends Record implements Cascading<List<T>> {

    protected abstract List<T> getItems();

    protected abstract ListMergeStrategy<T> getMergeStrategy();

    private transient List<T> mergedItems;

    @Override
    public List<T> get() {
        if (mergedItems != null) {
            return mergedItems;
        }
        List<T> items = getItems();
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void merge(List<T> existing) {
        if (mergedItems == null && existing != null) {
            ListMergeStrategy<T> mergeStrategy = getMergeStrategy();
            if (mergeStrategy != null) {
                mergedItems = mergeStrategy.merge(get(), existing);
            }
        }
    }

    public void merge(CascadingList<T> existing) {
        if (existing != null) {
            merge(existing.get());
        }
    }
}
