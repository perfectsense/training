package brightspot.core.search;

import brightspot.core.section.Section;
import brightspot.core.section.SectionableData;

public class SectionFilter extends FieldFilter {

    @Override
    public String getDefaultHeading() {
        return "Section";
    }

    @Override
    public String getField() {
        return SectionableData.class.getName() + "/sectionable.section";
    }

    @Override
    public String getItemLabel(Object object) {
        return object instanceof Section ? ((Section) object).getDisplayName() : null;
    }
}
