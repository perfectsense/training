package brightspot.core.section;

import brightspot.core.hierarchy.Hierarchical;

public interface Sectionable extends Hierarchical, SectionPageElements {

    default SectionableData asSectionableData() {
        return as(SectionableData.class);
    }

    @Override
    default Hierarchical getHierarchicalParent() {
        return asSectionableData().getSection();
    }

    default Section getSection() {
        return as(SectionableData.class).getSection();
    }
}
