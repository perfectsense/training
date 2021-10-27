package brightspot.section;

import brightspot.cascading.navigation.CascadingNavigation;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Alteration;

public class SectionPageAlteration extends Alteration<SectionPage> {

    @InternalName("sectionCascading.sectionNavigation")
    @ToolUi.Cluster("")
    @ToolUi.Tab("")
    private CascadingNavigation sectionNavigation;
}
