package brightspot.core.search;

import brightspot.core.tag.Tag;
import brightspot.core.tag.TaggableData;

public class TagFilter extends FieldFilter {

    @Override
    public String getDefaultHeading() {
        return "Tag";
    }

    @Override
    public String getField() {
        return TaggableData.class.getName() + '/' + TaggableData.VISIBLE_TAGS_AND_ANCESTORS_FIELD;
    }

    @Override
    protected boolean shouldIncludeFilterItem(Object object) {
        return !(object instanceof Tag)
            || !((Tag) object).isHidden();
    }

    @Override
    public boolean hasMutuallyExclusiveValues() {
        return false;
    }

    @Override
    public String getItemLabel(Object object) {
        return object instanceof Tag ? ((Tag) object).getDisplayName() : null;
    }
}
