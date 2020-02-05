package brightspot.core.cascading.listmerge;

import java.util.ArrayList;
import java.util.List;

public class ReplaceListMergeStrategy<T> implements ListMergeStrategy<T> {

    @Override
    public List<T> merge(List<T> overriding, List<T> existing) {
        return new ArrayList<>(overriding);
    }
}
