package brightspot.core.cascading.listmerge;

import java.util.ArrayList;
import java.util.List;

public class AfterListMergeStrategy<T> implements ListMergeStrategy<T> {

    @Override
    public List<T> merge(List<T> overriding, List<T> existing) {

        List<T> newList = new ArrayList<>();
        newList.addAll(existing);
        newList.addAll(overriding);

        return newList;
    }
}
