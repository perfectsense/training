package brightspot.module;

import brightspot.page.AbstractPageViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import com.psddev.cms.view.PreviewEntryView;
import com.psddev.styleguide.page.PageViewAboveField;
import com.psddev.styleguide.page.PageViewActionsField;
import com.psddev.styleguide.page.PageViewAmpAnalyticsField;
import com.psddev.styleguide.page.PageViewAmpIntegrationsField;
import com.psddev.styleguide.page.PageViewAsideField;
import com.psddev.styleguide.page.PageViewBannerField;
import com.psddev.styleguide.page.PageViewBelowField;
import com.psddev.styleguide.page.PageViewCommentingField;
import com.psddev.styleguide.page.PageViewDisclaimerField;
import com.psddev.styleguide.page.PageViewEntitlementsField;
import com.psddev.styleguide.page.PageViewExtraBodyItemsField;
import com.psddev.styleguide.page.PageViewFooterContentField;
import com.psddev.styleguide.page.PageViewFooterLogoField;
import com.psddev.styleguide.page.PageViewFooterNavigationField;
import com.psddev.styleguide.page.PageViewHatField;
import com.psddev.styleguide.page.PageViewLogoField;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewNavigationField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.page.PageViewSectionNavigationField;
import com.psddev.styleguide.page.PageViewSocialField;

public class ModuleTypePreviewViewModel extends AbstractPageViewModel<SharedModule> implements
    PreviewEntryView {

    @CurrentPageViewModel(PageViewModel.class)
    protected PageViewModel page;

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return createViews(PageViewMainField.class, model);
    }

    @Override
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewAboveField> getAbove() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewActionsField> getActions() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewAmpAnalyticsField> getAmpAnalytics() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewAmpIntegrationsField> getAmpIntegrations() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewAsideField> getAside() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewBannerField> getBanner() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewBelowField> getBelow() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewCommentingField> getCommenting() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewDisclaimerField> getDisclaimer() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewExtraBodyItemsField> getExtraBodyItems() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewFooterContentField> getFooterContent() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewFooterLogoField> getFooterLogo() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewFooterNavigationField> getFooterNavigation() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewHatField> getHat() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewLogoField> getLogo() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewNavigationField> getNavigation() {
        return null;
    }

    @Override
    public CharSequence getSearchAction() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewSectionNavigationField> getSectionNavigation() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewSocialField> getSocial() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewEntitlementsField> getEntitlements() {
        return null;
    }
}
