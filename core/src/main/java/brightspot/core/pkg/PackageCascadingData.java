package brightspot.core.pkg;

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

@Recordable.FieldInternalNamePrefix("packageCascading.")
public class PackageCascadingData extends Modification<PackagePageElements> {

    @CascadingPageElement
    @ToolUi.Cluster("Package")
    private CascadingNavigation packageNavigation;

    public Cascading<Navigation> getCascadingPackageNavigation() {
        return packageNavigation;
    }

    public void setCascadingPackageNavigation(CascadingNavigation packageNavigation) {
        this.packageNavigation = packageNavigation;
    }

    public Navigation getPackageNavigation(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, PackageCascadingData.class,
                PackageCascadingData::getCascadingPackageNavigation);
    }

    /**
     * To support localization for the "Inherit" placeholder.
     */
    public String getInheritPlaceholder() {
        return Localization.currentUserText(null, "option.inherit", "Inherit");
    }
}
