package brightspot.core.cascading.listmerge;

import java.util.ArrayList;
import java.util.List;

public class BeforeListMergeStrategy<T> implements ListMergeStrategy<T> {

    @Override
    public List<T> merge(List<T> overriding, List<T> existing) {

        List<T> newList = new ArrayList<>();
        newList.addAll(overriding);
        newList.addAll(existing);

        return newList;
    }
}
