package brightspot.core.hierarchy;

import com.psddev.dari.db.Recordable;

public interface Hierarchical extends Recordable {

    default HierarchicalData asHierarchicalData() {

        return this.as(HierarchicalData.class);
    }

    Hierarchical getHierarchicalParent();
}
