package brightspot.core.page;

import java.util.List;

import brightspot.core.cascading.Cascading;
import brightspot.core.cascading.CascadingPageElement;
import brightspot.core.cascading.CascadingPageElements;
import brightspot.core.cascading.CascadingPageElementsModification;
import brightspot.core.footer.CascadingFooter;
import brightspot.core.footer.Footer;
import brightspot.core.hat.CascadingHat;
import brightspot.core.hat.Hat;
import brightspot.core.logo.CascadingLogo;
import brightspot.core.logo.Logo;
import brightspot.core.module.CascadingModuleTypeList;
import brightspot.core.module.ModuleType;
import brightspot.core.navigation.CascadingNavigation;
import brightspot.core.navigation.Navigation;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

/**
 * Modification of {@link CascadingPageElements} to add global page elements.
 */
@Recordable.FieldInternalNamePrefix("cascadingPage.")
@Modification.Classes({ CascadingPageElements.class, SiteSettings.class })
public class CascadingPageData extends Modification<Object> {

    protected static final String HEADER_CLUSTER_NAME = "Header";
    protected static final String FOOTER_CLUSTER_NAME = "Footer";
    protected static final String MODULE_PLACEMENT_CLUSTER_NAME = "Module Placement";

    @ToolUi.Cluster(HEADER_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingLogo logo;

    @ToolUi.Cluster(HEADER_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingHat hat;

    @ToolUi.Cluster(HEADER_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingNavigation navigation;

    @ToolUi.Cluster(FOOTER_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingFooter footer;

    @ToolUi.Cluster(MODULE_PLACEMENT_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingModuleTypeList above;

    @ToolUi.Cluster(MODULE_PLACEMENT_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingModuleTypeList aside;

    @ToolUi.Cluster(MODULE_PLACEMENT_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingModuleTypeList below;

    public Cascading<Logo> getCascadingLogo() {
        return logo;
    }

    public Logo getLogo(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingLogo);
    }

    public Cascading<Hat> getCascadingHat() {
        return hat;
    }

    public Hat getHat(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingHat);
    }

    public Cascading<Navigation> getCascadingNavigation() {
        return navigation;
    }

    public Navigation getNavigation(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingNavigation);
    }

    public Cascading<Footer> getCascadingFooter() {
        return footer;
    }

    public Footer getFooter(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingFooter);
    }

    public CascadingModuleTypeList getCascadingAbove() {
        return above;
    }

    public List<ModuleType> getAbove(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingAbove);
    }

    public CascadingModuleTypeList getCascadingBelow() {
        return below;
    }

    public List<ModuleType> getBelow(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingBelow);
    }

    public CascadingModuleTypeList getCascadingAside() {
        return aside;
    }

    public List<ModuleType> getAside(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingAside);
    }

    public void setLogo(CascadingLogo logo) {
        this.logo = logo;
    }

    public void setFooter(CascadingFooter footer) {
        this.footer = footer;
    }

    public void setAbove(CascadingModuleTypeList above) {
        this.above = above;
    }

    public void setBelow(CascadingModuleTypeList below) {
        this.below = below;
    }

    public void setAside(CascadingModuleTypeList aside) {
        this.aside = aside;
    }

    public void setNavigation(CascadingNavigation navigation) {
        this.navigation = navigation;
    }

    public void setHat(CascadingHat hat) {
        this.hat = hat;
    }

    /**
     * To support localization for the "Inherit" placeholder.
     */
    public String getInheritPlaceholder() {
        return Localization.currentUserText(null, "option.inherit", "Inherit");
    }
}
