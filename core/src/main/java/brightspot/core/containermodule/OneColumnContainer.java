package brightspot.core.containermodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import brightspot.core.module.ModuleType;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * A {@link ContainerColumnOption} used in the {@link ContainerModule} that has one list of {@link ModuleType}s.
 */
@Recordable.DisplayName("1 Column")
public class OneColumnContainer extends Record implements
        ContainerColumnOption,
        Interchangeable {

    private List<ModuleType> columnOne;

    public List<ModuleType> getColumnOne() {
        if (columnOne == null) {
            columnOne = new ArrayList<>();
        }
        return columnOne;
    }

    public void setColumnOne(List<ModuleType> columnOne) {
        this.columnOne = columnOne;
    }

    @Override
    public boolean loadTo(Object newObj) {
        if (newObj instanceof TwoColumnContainer) {
            ((TwoColumnContainer) newObj).setColumnOne(this.getColumnOne());
            return true;
        }
        if (newObj instanceof ThreeColumnContainer) {
            ((ThreeColumnContainer) newObj).setColumnOne(this.getColumnOne());
            return true;
        }
        if (newObj instanceof FourColumnContainer) {
            ((FourColumnContainer) newObj).setColumnOne(this.getColumnOne());
            return true;
        }
        return false;
    }

    @Override
    public Set<UUID> getContainerContentIds() {
        return getColumnOne().stream()
                .map(ModuleType::getModuleTypeContentIds)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}