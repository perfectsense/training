package brightspot.core.containermodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.core.module.ModuleType;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * A {@link ContainerColumnOption} used in the {@link ContainerModule} that has 3 lists of {@link ModuleType}s.
 */
@Recordable.DisplayName("3 Columns")
public class ThreeColumnContainer extends Record implements
        ContainerColumnOption,
        Interchangeable {

    private List<ModuleType> columnOne;

    private List<ModuleType> columnTwo;

    private List<ModuleType> columnThree;

    public List<ModuleType> getColumnOne() {
        if (columnOne == null) {
            columnOne = new ArrayList<>();
        }
        return columnOne;
    }

    public void setColumnOne(List<ModuleType> columnOne) {
        this.columnOne = columnOne;
    }

    public List<ModuleType> getColumnTwo() {
        if (columnTwo == null) {
            columnTwo = new ArrayList<>();
        }
        return columnTwo;
    }

    public void setColumnTwo(List<ModuleType> columnTwo) {
        this.columnTwo = columnTwo;
    }

    public List<ModuleType> getColumnThree() {
        if (columnThree == null) {
            columnThree = new ArrayList<>();
        }
        return columnThree;
    }

    public void setColumnThree(List<ModuleType> columnThree) {
        this.columnThree = columnThree;
    }

    @Override
    public boolean loadTo(Object newObj) {
        if (newObj instanceof OneColumnContainer) {
            ((OneColumnContainer) newObj).setColumnOne(this.getColumnOne());
            return true;
        }
        if (newObj instanceof TwoColumnContainer) {
            ((TwoColumnContainer) newObj).setColumnOne(this.getColumnOne());
            ((TwoColumnContainer) newObj).setColumnTwo(this.getColumnTwo());
            return true;
        }
        if (newObj instanceof brightspot.core.containermodule.FourColumnContainer) {
            ((FourColumnContainer) newObj).setColumnOne(this.getColumnOne());
            ((FourColumnContainer) newObj).setColumnTwo(this.getColumnTwo());
            ((FourColumnContainer) newObj).setColumnThree(this.getColumnThree());
            return true;
        }
        return false;
    }

    @Override
    public Set<UUID> getContainerContentIds() {
        return Stream.of(getColumnOne(), getColumnTwo(), getColumnThree())
            .flatMap(List::stream)
            .map(ModuleType::getModuleTypeContentIds)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }
}
