package brightspot.core.section;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;

class SectionPostProcessor implements ObjectType.PostProcessor {

    @Override
    public void process(ObjectType type) {
        ObjectField navigationField = type.getField("sectionCascading.sectionNavigation");

        if (navigationField != null) {
            navigationField.as(ToolUi.class).setTab(null); // move to main tab
            navigationField.as(ToolUi.class).setCluster(null); // remove cluster heading
        }
    }
}
