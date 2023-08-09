package brightspot.section;

import brightspot.cascading.Cascading;
import brightspot.cascading.CascadingPageElement;
import brightspot.cascading.CascadingPageElementsModification;
import brightspot.cascading.navigation.CascadingNavigation;
import brightspot.navigation.Navigation;
import brightspot.page.CascadingPageData;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("sectionCascading.")
public class SectionCascadingData extends Modification<SectionPageElements> {

    @DisplayName("Secondary Navigation")
    @ToolUi.Cluster(CascadingPageData.LAYOUT_CLUSTER_NAME)
    @CascadingPageElement
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
        return ToolLocalization.text(null, "option.inherit", "Inherit");
    }
}
