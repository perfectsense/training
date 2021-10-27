package brightspot.page;

import java.util.List;

import brightspot.cascading.Cascading;
import brightspot.cascading.CascadingPageElement;
import brightspot.cascading.CascadingPageElements;
import brightspot.cascading.CascadingPageElementsModification;
import brightspot.cascading.module.CascadingModuleList;
import brightspot.cascading.navigation.CascadingNavigation;
import brightspot.footer.CascadingFooter;
import brightspot.footer.Footer;
import brightspot.hat.CascadingHat;
import brightspot.hat.Hat;
import brightspot.logo.CascadingLogo;
import brightspot.logo.Logo;
import brightspot.module.ModulePlacement;
import brightspot.navigation.Navigation;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

/**
 * Modification of {@link CascadingPageElements} to add global page elements.
 */
@Recordable.FieldInternalNamePrefix("cascadingPage.")
@Modification.Classes({ CascadingPageElements.class, SiteSettings.class })
public class CascadingPageData extends Modification<Object> {

    public static final String LAYOUT_CLUSTER_NAME = "Layout";

    @ToolUi.Cluster("Logos & Icons")
    @CascadingPageElement
    private CascadingLogo logo;

    @ToolUi.Cluster("Logos & Icons")
    @ToolUi.DisplayName("News Publisher Logo")
    @CascadingPageElement
    private CascadingLogo ampLogo;

    @ToolUi.Cluster(LAYOUT_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingHat hat;

    @ToolUi.Cluster(LAYOUT_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingNavigation navigation;

    @ToolUi.Cluster(LAYOUT_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingFooter footer;

    @ToolUi.Cluster(LAYOUT_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingModuleList above;

    @ToolUi.Cluster(LAYOUT_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingModuleList aside;

    @ToolUi.Cluster(LAYOUT_CLUSTER_NAME)
    @CascadingPageElement
    private CascadingModuleList below;

    public Cascading<Logo> getCascadingLogo() {
        return logo;
    }

    public Logo getLogo(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingLogo);
    }

    public Cascading<Logo> getCascadingAmpLogo() {
        return ampLogo;
    }

    public Logo getAmpLogo(Site site) {
        return as(CascadingPageElementsModification.class)
                .get(site, CascadingPageData.class,
                        CascadingPageData::getCascadingAmpLogo);
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

    public CascadingModuleList getCascadingAbove() {
        return above;
    }

    public List<ModulePlacement> getAbove(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingAbove);
    }

    public CascadingModuleList getCascadingBelow() {
        return below;
    }

    public List<ModulePlacement> getBelow(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingBelow);
    }

    public CascadingModuleList getCascadingAside() {
        return aside;
    }

    public List<ModulePlacement> getAside(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, CascadingPageData.class,
                CascadingPageData::getCascadingAside);
    }

    public void setLogo(CascadingLogo logo) {
        this.logo = logo;
    }

    public void setAmpLogo(CascadingLogo ampLogo) {
        this.ampLogo = ampLogo;
    }

    public void setFooter(CascadingFooter footer) {
        this.footer = footer;
    }

    public void setAbove(CascadingModuleList above) {
        this.above = above;
    }

    public void setBelow(CascadingModuleList below) {
        this.below = below;
    }

    public void setAside(CascadingModuleList aside) {
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
        return ToolLocalization.text(null, "option.inherit", "Inherit");
    }
}
