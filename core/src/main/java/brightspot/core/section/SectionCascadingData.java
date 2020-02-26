package brightspot.core.section;

import brightspot.core.cascading.Cascading;
import brightspot.core.cascading.CascadingPageElement;
import brightspot.core.cascading.CascadingPageElementsModification;
import brightspot.core.navigation.CascadingNavigation;
import brightspot.core.navigation.Navigation;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("sectionCascading.")
public class SectionCascadingData extends Modification<SectionPageElements> {

    @CascadingPageElement
    @ToolUi.Cluster("Section")
    private CascadingNavigation sectionNavigation;

    public Cascading<Navigation> getCascadingSectionNavigation() {
        return sectionNavigation;
    }

    public void setCascadingSectionNavigation(CascadingNavigation sectionNavigation) {
        this.sectionNavigation = sectionNavigation;
    }

    // Site is ignored; here for future proofing
    public Navigation getSectionNavigation(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, SectionCascadingData.class,
                SectionCascadingData::getCascadingSectionNavigation);
    }

    /**
     * To support localization for the "Inherit" placeholder.
     */
    public String getInheritPlaceholder() {
        return Localization.currentUserText(null, "option.inherit", "Inherit");
    }
}
