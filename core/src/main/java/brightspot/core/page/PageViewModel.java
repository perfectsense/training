package brightspot.core.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.amp.page.DefaultAmpPageViewModel;
import brightspot.community.page.DefaultCommunityPageViewModel;
import brightspot.core.banner.Banner;
import brightspot.core.footer.PageFooter;
import brightspot.core.hierarchy.HierarchicalData;
import brightspot.core.navigation.NavigationSearch;
import brightspot.core.person.Authorable;
import brightspot.core.pkg.PackageCascadingData;
import brightspot.core.requestextras.CurrentRequestExtras;
import brightspot.core.requestextras.RequestExtras;
import brightspot.core.requestextras.headelement.CustomHeadElements;
import brightspot.core.requestextras.headelement.HeadElement;
import brightspot.core.requestextras.headelement.ScriptElement;
import brightspot.core.requestextras.headelement.StyleElement;
import brightspot.core.section.SectionCascadingData;
import brightspot.core.site.FrontEndSettings;
import brightspot.core.site.ManifestFilter;
import brightspot.core.tag.TaggableData;
import brightspot.core.tool.DirectoryItemUtils;
import brightspot.core.tool.RichTextUtils;
import brightspot.corporate.page.DefaultCorporatePageViewModel;
import brightspot.news.revision.RevisionText;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.image.ImageSize;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.MainObject;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.feed.FeedSource;
import com.psddev.feed.FeedUtils;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.core.page.FaviconView;
import com.psddev.styleguide.core.page.SeoRobotsMetaView;
import com.psddev.styleguide.facebook.FacebookMetasView;
import com.psddev.styleguide.facebook.OpenGraphMetaView;
import com.psddev.styleguide.twitter.card.TwitterSummaryLargeImageCardView;

public class PageViewModel extends ViewModel<Recordable> {

    public static final String DEFAULT_DATE_FORMAT = "MMMM d, yyyy hh:mm aa";

    @MainObject
    protected Object mainObject;

    @CurrentSite
    protected Site currentSite;

    @CurrentRequestExtras
    protected RequestExtras requestExtras;

    @CurrentPageViewModel(DefaultCorporatePageViewModel.class)
    protected DefaultCorporatePageViewModel corporatePage;

    @CurrentPageViewModel(DefaultCommunityPageViewModel.class)
    protected DefaultCommunityPageViewModel communityPage;

    @CurrentPageViewModel(DefaultAmpPageViewModel.class)
    protected DefaultAmpPageViewModel ampPage;

    public Site getSite() {
        return currentSite;
    }

    public Object getMainObject() {
        return mainObject;
    }

    public RequestExtras getRequestExtras() {
        return requestExtras;
    }

    public <T> Iterable<T> getMeta(Class<T> viewClass) {
        return Stream.of(
            createView(SeoRobotsMetaView.class, model),
            createView(OpenGraphMetaView.class, model),
            createView(TwitterSummaryLargeImageCardView.class, model),
            createView(FacebookMetasView.class, model))
            .filter(viewClass::isInstance)
            .map(viewClass::cast)
            .collect(Collectors.toList());
    }

    public CharSequence getKeywords() {
        // Grab the first 10 keywords, per google recommendation
        return Optional
            .ofNullable(model.as(Seo.ObjectModification.class).findKeywords())
            .map(keywords -> keywords.stream().limit(10)
                .collect(Collectors.joining(",", "", "")))
            .orElse(null);
    }

    public CharSequence getDescription() {
        // Plain text
        return model.as(Seo.ObjectModification.class).findDescription();
    }

    public <T> Iterable<T> getExtraScripts(Class<T> viewClass) {
        // Default template includes All.min.js (ExternalScriptView async=true)

        Collection<T> scriptViews = new ArrayList<>();

        // Find enabled head scripts.
        Collection<HeadScripts> headScriptsList = ((ArrayList<HeadScripts>) PageElementSupplier.get(
            HeadScripts.class,
            currentSite,
            model))
            .stream()
            .filter(headScripts -> !headScripts.asIntegrationItemsData().isDisabled())
            .collect(Collectors.toList());

        // Create views for the head scripts.
        headScriptsList.forEach(headScripts -> {
            scriptViews.addAll((Collection<? extends T>) createViews(viewClass, headScripts));
        });

        for (CustomHeadElements extra : requestExtras.getByClass(CustomHeadElements.class)) {
            for (HeadElement element : extra.getElements()) {
                if (element instanceof ScriptElement) {
                    for (T view : createViews(viewClass, element)) {
                        scriptViews.add(view);
                    }
                }
            }
        }

        return scriptViews;
    }

    public <T> Iterable<T> getExtraBodyItems(Class<T> viewClass) {
        Collection<T> bodyItems = new ArrayList<>();

        // Find enabled extra body items.
        Collection<ExtraBodyItems> extraBodyItemsList = ((ArrayList<ExtraBodyItems>) PageElementSupplier.get(
            ExtraBodyItems.class,
            currentSite,
            model))
            .stream()
            .filter(extraBodyItems -> !extraBodyItems.asIntegrationItemsData().isDisabled())
            .collect(Collectors.toList());

        // Create views for the extra body items.
        extraBodyItemsList.forEach(extraBodyItems -> {
            bodyItems.addAll((Collection<? extends T>) createViews(viewClass, extraBodyItems));
        });

        return bodyItems;
    }

    public <T> Iterable<T> getExtraBodyAttributes(Class<T> viewClass) {
        Collection<T> bodyAttributes = new ArrayList<>();

        // Find enabled extra body attributes.
        Collection<ExtraBodyAttributes> extraBodyAttributesList = ((ArrayList<ExtraBodyAttributes>) PageElementSupplier.get(
            ExtraBodyAttributes.class,
            currentSite,
            model))
            .stream()
            .filter(extraBodyAttributes -> !extraBodyAttributes.asIntegrationItemsData().isDisabled())
            .collect(Collectors.toList());

        // Create views for the extra body attributes.
        extraBodyAttributesList.forEach(extraBodyAttributes -> {
            bodyAttributes.addAll((Collection<? extends T>) createViews(viewClass, extraBodyAttributes));
        });

        return bodyAttributes;
    }

    public <T> Iterable<T> getHeadMetas(Class<T> viewClass) {
        return null;
    }

    public CharSequence getLanguage() {
        return Optional.ofNullable(FrontEndSettings.get(currentSite, f -> f))
            .map(FrontEndSettings::getLocale)
            .map(Locale::getLanguage)
            .orElse(null);
    }

    public CharSequence getTitle() {
        // Plain text
        return model.as(Seo.ObjectModification.class).findTitle();
    }

    public CharSequence getCanonicalLink() {
        return DirectoryItemUtils.getCanonicalUrl(currentSite, model);
    }

    public CharSequence getCharset() {
        // Default template specifies "UTF-8"
        return null;
    }

    public CharSequence getFeedLink() {
        if (model.getState().isInstantiableTo(FeedSource.class)) {
            return RichTextUtils.buildInlineHtml(
                model.getState().getDatabase(),
                FeedUtils.getFeedAutoDetectLink(model.as(FeedSource.class), currentSite),
                this::createView);
        }
        return null;
    }

    public CharSequence getJsonLinkedData() {
        return null;
    }

    public CharSequence getViewport() {
        // Default template specifies "width=device-width, initial-scale=1"
        return null;
    }

    public <T> Iterable<T> getStyles(Class<T> viewClass) {
        // Default template includes All.min.css (ExternalStylesheet)

        List<T> styles = new ArrayList<>();
        for (CustomHeadElements extra : requestExtras.getByClass(CustomHeadElements.class)) {
            for (HeadElement element : extra.getElements()) {
                if (element instanceof StyleElement) {
                    for (T view : createViews(viewClass, element)) {
                        styles.add(view);
                    }
                }
            }
        }
        return styles;
    }

    public <T> Iterable<T> getNavigation(Class<T> viewClass) {
        return createViews(
            viewClass,
            Optional.ofNullable(model.as(CascadingPageData.class))
                .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class)))
                .getNavigation(currentSite));
    }

    public <T> Iterable<T> getSectionNavigation(Class<T> viewClass) {
        return Optional.ofNullable(model.as(SectionCascadingData.class))
            .map(data -> data.getSectionNavigation(currentSite))
            .map(nav -> createViews(viewClass, nav))
            .orElse(null);
    }

    public <T> Iterable<T> getPackageNavigation(Class<T> viewClass) {
        return Optional.ofNullable(model.as(PackageCascadingData.class))
            .map(data -> data.getPackageNavigation(currentSite))
            .map(nav -> createViews(viewClass, nav))
            .orElse(null);
    }

    public <T> Iterable<T> getLogo(Class<T> viewClass) {
        return createViews(
            viewClass,
            Optional.ofNullable(model.as(CascadingPageData.class))
                .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class)))
                .getLogo(currentSite));
    }

    public <T> Iterable<T> getHat(Class<T> viewClass) {
        return createViews(viewClass, Optional.ofNullable(model.as(CascadingPageData.class))
            .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class)))
            .getHat(currentSite));
    }

    public CharSequence getSearchAction() {
        NavigationSearch search = FrontEndSettings.get(currentSite, FrontEndSettings::getSearchPage);
        return search == null ? null : search.getSearchAction(currentSite);
    }

    public <T> Iterable<T> getFooterLogo(Class<T> viewClass) {
        return Stream.of(Optional.ofNullable(model.as(CascadingPageData.class))
            .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class))))
            .map(data -> data.getFooter(currentSite))
            .filter(Objects::nonNull)
            .filter(PageFooter.class::isInstance)
            .map(PageFooter.class::cast)
            .map(PageFooter::getLogo)
            .map(logo -> createViews(viewClass, logo))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    public <T> Iterable<T> getFooterNavigation(Class<T> viewClass) {
        return Stream.of(Optional.ofNullable(model.as(CascadingPageData.class))
            .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class))))
            .map(data -> data.getFooter(currentSite))
            .filter(Objects::nonNull)
            .filter(PageFooter.class::isInstance)
            .map(PageFooter.class::cast)
            .map(PageFooter::getNavigation)
            .map(footerNavigation -> createViews(viewClass, footerNavigation))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    public <T> Iterable<T> getFooterContent(Class<T> viewClass) {
        return Stream.of(Optional.ofNullable(model.as(CascadingPageData.class))
            .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class))))
            .map(data -> data.getFooter(currentSite))
            .filter(Objects::nonNull)
            .filter(PageFooter.class::isInstance)
            .map(PageFooter.class::cast)
            .map(PageFooter::getContent)
            .filter(content -> !ObjectUtils.isBlank(content))
            .map(footerNavigation -> createViews(viewClass, footerNavigation))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    public CharSequence getDisclaimer() {
        return Stream.of(Optional.ofNullable(model.as(CascadingPageData.class))
            .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class))))
            .map(data -> data.getFooter(currentSite))
            .filter(Objects::nonNull)
            .filter(PageFooter.class::isInstance)
            .map(PageFooter.class::cast)
            .map(PageFooter::getDisclaimer)
            .map(disclaimer -> RichTextUtils.buildInlineHtml(
                model.getState().getDatabase(),
                disclaimer,
                this::createView))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    public <T> Iterable<T> getAbove(Class<T> viewClass) {
        return createViews(
            viewClass,
            Optional.ofNullable(model.as(CascadingPageData.class))
                .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class)))
                .getAbove(currentSite));
    }

    public <T> Iterable<T> getBelow(Class<T> viewClass) {
        return createViews(
            viewClass,
            Optional.ofNullable(model.as(CascadingPageData.class))
                .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class)))
                .getBelow(currentSite));
    }

    public <T> Iterable<T> getAside(Class<T> viewClass) {
        return createViews(
            viewClass,
            Optional.ofNullable(model.as(CascadingPageData.class))
                .orElse(SiteSettings.get(currentSite, s -> s.as(CascadingPageData.class)))
                .getAside(currentSite));
    }

    public <T> Iterable<T> getActions(Class<T> viewClass) {
        return createViews(viewClass, model);
    }

    public <T> Iterable<T> getBreadcrumbs(Class<T> viewClass) {
        return Optional.ofNullable(model.as(HierarchicalData.class))
            .map(HierarchicalData::getBreadcrumbs)
            .map(breadcrumbs -> createViews(viewClass, breadcrumbs))
            .orElse(null);
    }

    public <T> Iterable<T> getSocial(Class<T> viewClass) {

        if (viewClass == null) {
            return null;
        }

        return createViews(viewClass, SiteSettings.get(currentSite, s -> s));
    }

    public <T> Iterable<T> getTags(Class<T> viewClass) {
        return Optional.ofNullable(model.as(TaggableData.class))
            .map(data -> data.getTags().stream().filter(tag -> !tag.isHidden()).collect(Collectors.toList()))
            .map(tags -> createViews(viewClass, tags))
            .orElse(null);
    }

    /**
     * Gets {@code href} to manifest.json file. The output of this file is produced by {@link ManifestFilter} which
     * retrieves the configuration from {@link FrontEndSettings#getWebAppManifest()}.
     *
     * @return {@code href} to manifest.json
     */
    public CharSequence getManifestLink() {
        String manifest = FrontEndSettings.get(currentSite, FrontEndSettings::getWebAppManifest);

        if (manifest == null) {
            return null;
        }

        return ManifestFilter.PATH;
    }

    /**
     * Gets {@link FaviconView}s for the recommended favicon implementations. {@link brightspot.core.site.FaviconFilter}
     * handles favicon requests, and automatically resizes {@link FrontEndSettings#getFavicon()}.
     *
     * @return {@link FaviconView}s
     */
    public Iterable<FaviconView> getFavicons() {
        StorageItem favicon = FrontEndSettings.get(currentSite, FrontEndSettings::getFavicon);

        if (favicon == null) {
            return null;
        }

        // TODO: is there a better way to do this without a wrapper template/json?
        return Arrays.asList(
            new FaviconView.Builder()
                .href("/apple-touch-icon.png")
                .rel("apple-touch-icon")
                .sizes("180x180")
                .build(),
            new FaviconView.Builder()
                .href("/favicon-32x32.png")
                .rel("icon")
                .type("image/png")
                .build(),
            new FaviconView.Builder()
                .href("/favicon-16x16.png")
                .rel("icon")
                .type("image/png")
                .build());
    }

    public CharSequence getAuthorUrl() {
        return Authorable.getAuthorUrl(currentSite, model);
    }

    public Map<String, ?> getAuthorImage() {
        return Authorable.getAuthorImageFile(model)
            .map(ImageSize::getAttributes)
            .orElse(null);
    }

    public CharSequence getAuthorName() {
        return Authorable.getAuthorName(model);
    }

    public CharSequence getAuthorBiography() {
        return Optional.ofNullable(Authorable.getAuthorBiography(model))
            .map(t -> RichTextUtils.buildInlineHtml(model.getState().getDatabase(), t, this::createView))
            .orElse(null);
    }

    public <T> Iterable<T> getContributors(Class<T> viewClass) {
        return Authorable.getByline(model)
            .map(byline -> createViews(viewClass, byline.getBylineOthers()))
            .orElse(null);
    }

    public <T> Iterable<T> getPeople(Class<T> viewClass) {
        return Authorable.getByline(model)
            .map(byline -> createViews(viewClass, byline.getAuthors()))
            .orElse(null);
    }

    public <T> Iterable<T> getBanners(Class<T> viewClass) {
        return createViews(viewClass, PageElementSupplier.get(Banner.class, currentSite, model));
    }

    public CharSequence getUpdatesCorrections() {

        List<String> revisionTexts = new ArrayList<>();

        for (RevisionText revisionText : PageElementSupplier.get(RevisionText.class, currentSite, model)) {
            String text = revisionText.getRevisionText();
            if (!ObjectUtils.isBlank(text)) {
                revisionTexts.add(text);
            }
        }

        return RawHtml.of(StringUtils.join(revisionTexts, "<br>"));
    }

    public <T> Iterable<T> getComments(Class<T> viewClass) {
        return communityPage.getComments(viewClass);
    }

    public CharSequence getTimedContentTimeStamp() {
        return communityPage.getTimedContentTimeStamp();
    }

    // -- CorporatePageViewModel bridge methods

    public <T> Iterable<T> getCountries(Class<T> viewClass) {
        return corporatePage.getCountries(viewClass);
    }

    public <T> Iterable<T> getBrands(Class<T> viewClass) {
        return corporatePage.getBrands(viewClass);
    }

    public <T> Iterable<T> getProducts(Class<T> viewClass) {
        return corporatePage.getProducts(viewClass);
    }

    public <T> Iterable<T> getTopics(Class<T> viewClass) {
        return corporatePage.getTopics(viewClass);
    }

    public CharSequence getContentId() {
        return State.getInstance(model).getId().toString();
    }

    // -- AmpPageViewModel bridge methods

    public <T> Iterable<T> getAmpIntegrations(Class<T> viewClass) {
        return Optional.ofNullable(ampPage)
            .map(defaultAmpPageViewModel -> defaultAmpPageViewModel.getAmpIntegrations(viewClass))
            .orElse(null);
    }

    public <T> Iterable<T> getAmpCustomAnalytics(Class<T> viewClass) {
        return Optional.ofNullable(ampPage)
            .map(defaultAmpPageViewModel -> defaultAmpPageViewModel.getAmpCustomAnalytics(viewClass))
            .orElse(null);
    }
}
