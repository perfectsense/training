package brightspot.core.module;

import java.util.List;

import brightspot.core.cascading.listmerge.ListMergeStrategy;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("None")
public class CascadingModuleTypeListNone extends CascadingModuleTypeList {

    @Override
    public List<ModuleType> getItems() {
        return null;
    }

    @Override
    protected ListMergeStrategy<ModuleType> getMergeStrategy() {
        return null;
    }
}
