package brightspot.page;

import java.util.Map;
import java.util.Optional;

import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.jsonld.JsonLd;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.dari.db.Recordable;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.page.PageView;
import com.psddev.styleguide.page.PageViewAboveField;
import com.psddev.styleguide.page.PageViewActionsField;
import com.psddev.styleguide.page.PageViewAmpAnalyticsField;
import com.psddev.styleguide.page.PageViewAmpIntegrationsField;
import com.psddev.styleguide.page.PageViewAsideField;
import com.psddev.styleguide.page.PageViewBannerField;
import com.psddev.styleguide.page.PageViewBelowField;
import com.psddev.styleguide.page.PageViewDisclaimerField;
import com.psddev.styleguide.page.PageViewExtraBodyItemsField;
import com.psddev.styleguide.page.PageViewExtraLinksField;
import com.psddev.styleguide.page.PageViewExtraScriptsField;
import com.psddev.styleguide.page.PageViewExtraStylesField;
import com.psddev.styleguide.page.PageViewFaviconsField;
import com.psddev.styleguide.page.PageViewFooterContentField;
import com.psddev.styleguide.page.PageViewFooterLogoField;
import com.psddev.styleguide.page.PageViewFooterNavigationField;
import com.psddev.styleguide.page.PageViewHatField;
import com.psddev.styleguide.page.PageViewLogoField;
import com.psddev.styleguide.page.PageViewMetaField;
import com.psddev.styleguide.page.PageViewNavigationField;
import com.psddev.styleguide.page.PageViewSectionNavigationField;
import com.psddev.styleguide.page.PageViewSocialField;
import com.psddev.styleguide.page.PageViewStylePackageField;

/**
 * Note:  This a port/refactor of the current page view model pattern in use across Express (and ultimately
 * Express-based projects), with the end goal of deprecating {@link PageViewModel}.
 */
public abstract class AbstractPageViewModel<M extends Recordable> extends ViewModel<M> implements PageView {

    @CurrentPageViewModel(PageViewModel.class)
    protected PageViewModel page;

    protected Object getMainObject() {
        return page.getMainObject();
    }

    protected Site getSite() {
        return page.getSite();
    }

    @Override
    public Iterable<? extends PageViewAboveField> getAbove() {
        return page.getAbove(PageViewAboveField.class);
    }

    @Override
    public Iterable<? extends PageViewActionsField> getActions() {
        return page.getActions(PageViewActionsField.class);
    }

    @Override
    public Iterable<? extends PageViewAmpAnalyticsField> getAmpAnalytics() {
        return page.getAmpCustomAnalytics(PageViewAmpAnalyticsField.class);
    }

    @Override
    public Iterable<? extends PageViewAmpIntegrationsField> getAmpIntegrations() {
        return page.getAmpIntegrations(PageViewAmpIntegrationsField.class);
    }

    @Override
    public Iterable<? extends PageViewAsideField> getAside() {
        return page.getAside(PageViewAsideField.class);
    }

    @Override
    public Iterable<? extends PageViewBannerField> getBanner() {
        return page.getBanners(PageViewBannerField.class);
    }

    @Override
    public Iterable<? extends PageViewBelowField> getBelow() {
        return page.getBelow(PageViewBelowField.class);
    }

    @JsonLdNode("url")
    @Override
    public CharSequence getCanonicalLink() {
        return page.getCanonicalLink();
    }

    @Override
    public Iterable<? extends PageViewStylePackageField> getStylePackage() {
        return page.getStylePackage(PageViewStylePackageField.class);
    }

    @Override
    public Iterable<? extends PageViewExtraLinksField> getExtraLinks() {
        return page.getExtraLinks(PageViewExtraLinksField.class);
    }

    @Override
    public CharSequence getContentId() {
        return page.getContentId();
    }

    @JsonLdNode("description")
    @Override
    public CharSequence getDescription() {
        return page.getDescription();
    }

    @Override
    public Iterable<? extends PageViewDisclaimerField> getDisclaimer() {
        return page.getDisclaimer(PageViewDisclaimerField.class);
    }

    @Override
    public Iterable<? extends PageViewExtraBodyItemsField> getExtraBodyItems() {
        return page.getExtraBodyItems(PageViewExtraBodyItemsField.class);
    }

    @Override
    public Iterable<? extends PageViewExtraScriptsField> getExtraScripts() {
        return page.getExtraScripts(PageViewExtraScriptsField.class);
    }

    @Override
    public Iterable<? extends PageViewExtraStylesField> getExtraStyles() {
        return page.getStyles(PageViewExtraStylesField.class);
    }

    @Override
    public Iterable<? extends PageViewFaviconsField> getFavicons() {
        return page.getFavicons();
    }

    @Override
    public CharSequence getFeedLink() {
        return page.getFeedLink();
    }

    @Override
    public Iterable<? extends PageViewFooterContentField> getFooterContent() {
        return page.getFooterContent(PageViewFooterContentField.class);
    }

    @Override
    public Iterable<? extends PageViewFooterLogoField> getFooterLogo() {
        return page.getFooterLogo(PageViewFooterLogoField.class);
    }

    @Override
    public Iterable<? extends PageViewFooterNavigationField> getFooterNavigation() {
        return page.getFooterNavigation(PageViewFooterNavigationField.class);
    }

    @Override
    public Iterable<? extends PageViewHatField> getHat() {
        return page.getHat(PageViewHatField.class);
    }

    @Override
    public CharSequence getJsonLinkedData() {
        return Optional.ofNullable(JsonLd.createHtmlScriptBody(this))
            .map(RawHtml::of)
            .orElse(null);
    }

    @Override
    public CharSequence getKeywords() {
        return page.getKeywords();
    }

    @Override
    public CharSequence getLanguage() {
        return page.getLanguage();
    }

    @Override
    public Iterable<? extends PageViewLogoField> getLogo() {
        return page.getLogo(PageViewLogoField.class);
    }

    @Override
    public CharSequence getManifestLink() {
        return page.getManifestLink();
    }

    @Override
    public Iterable<? extends PageViewMetaField> getMeta() {
        return page.getMeta(PageViewMetaField.class);
    }

    @Override
    public Iterable<? extends PageViewNavigationField> getNavigation() {
        return page.getNavigation(PageViewNavigationField.class);
    }

    @Override
    public CharSequence getSearchAction() {
        return page.getSearchAction();
    }

    @Override
    public Iterable<? extends PageViewSectionNavigationField> getSectionNavigation() {
        return page.getSectionNavigation(PageViewSectionNavigationField.class);
    }

    @Override
    public Iterable<? extends PageViewSocialField> getSocial() {
        return page.getSocial(PageViewSocialField.class);
    }

    @JsonLdNode("name")
    @Override
    public CharSequence getTitle() {
        return page.getTitle();
    }

    @JsonLdNode("url")
    public CharSequence getCanonicalUrl() {
        return page.getCanonicalLink();
    }

    @JsonLdNode("publisher")
    public Map<String, Object> getPublisherData() {
        return page.getPublisherData();
    }
}
