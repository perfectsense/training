package brightspot.core.module;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.cascading.listmerge.ListMergeStrategy;
import brightspot.core.cascading.listmerge.ReplaceListMergeStrategy;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Replace")
public class CascadingModuleTypeListReplace extends CascadingModuleTypeList {

    @ToolUi.Unlabeled
    private List<ModuleType> items;

    @Override
    public List<ModuleType> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    @Override
    protected ListMergeStrategy<ModuleType> getMergeStrategy() {
        return new ReplaceListMergeStrategy<>();
    }
}
