package brightspot.event;

import java.util.Date;
import java.util.Optional;

import brightspot.page.AbstractPageViewModel;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.PreviewEntryView;
import com.psddev.styleguide.event.EventView;
import com.psddev.styleguide.event.EventViewAddressField;
import com.psddev.styleguide.event.EventViewImageField;
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
import com.psddev.styleguide.page.PageViewExtraLinksField;
import com.psddev.styleguide.page.PageViewExtraScriptsField;
import com.psddev.styleguide.page.PageViewExtraStylesField;
import com.psddev.styleguide.page.PageViewFaviconsField;
import com.psddev.styleguide.page.PageViewFooterContentField;
import com.psddev.styleguide.page.PageViewFooterLogoField;
import com.psddev.styleguide.page.PageViewFooterNavigationField;
import com.psddev.styleguide.page.PageViewHatField;
import com.psddev.styleguide.page.PageViewLanguagesField;
import com.psddev.styleguide.page.PageViewLogoField;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewMetaField;
import com.psddev.styleguide.page.PageViewNavigationField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.page.PageViewSectionNavigationField;
import com.psddev.styleguide.page.PageViewSocialField;

public class EventPageViewModel extends AbstractPageViewModel<Event> implements EventView, PreviewEntryView,
    PageEntryView {

    @Override
    public CharSequence getStartDate() {

        return Optional.ofNullable(model.getStartDate())
            .map(Date::toString)
            .orElse(null);
    }

    @Override
    public CharSequence getEndDate() {
        return Optional.ofNullable(model.getEndDate())
            .map(Date::toString)
            .orElse(null);
    }

    @Override
    public Iterable<? extends EventViewImageField> getImage() {
        return createViews(EventViewImageField.class, model.getImage());
    }

    @Override
    public Iterable<? extends EventViewAddressField> getAddress() {
        return createViews(EventViewAddressField.class, model.getAddress());
    }

    @Override
    public CharSequence getDetails() {
        return model.getDetails();
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
    public CharSequence getCanonicalLink() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewCommentingField> getCommenting() {
        return null;
    }

    @Override
    public CharSequence getContentId() {
        return null;
    }

    @Override
    public CharSequence getDescription() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewDisclaimerField> getDisclaimer() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewEntitlementsField> getEntitlements() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewExtraBodyItemsField> getExtraBodyItems() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewExtraLinksField> getExtraLinks() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewExtraScriptsField> getExtraScripts() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewExtraStylesField> getExtraStyles() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewFaviconsField> getFavicons() {
        return null;
    }

    @Override
    public CharSequence getFeedLink() {
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
    public CharSequence getJsonLinkedData() {
        return null;
    }

    @Override
    public CharSequence getLanguage() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewLanguagesField> getLanguages() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewLogoField> getLogo() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return null;
    }

    @Override
    public CharSequence getManifestLink() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewMetaField> getMeta() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewNavigationField> getNavigation() {
        return null;
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
    public CharSequence getTitle() {
        return null;
    }
}