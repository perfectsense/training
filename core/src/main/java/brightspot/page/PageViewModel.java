package brightspot.page;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.banner.Banner;
import brightspot.breadcrumbs.HasBreadcrumbs;
import brightspot.commenting.CommentingSiteSettings;
import brightspot.footer.Footer;
import brightspot.footer.PageFooter;
import brightspot.head.HeadScripts;
import brightspot.image.ImageSchemaData;
import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.l10n.LocaleProvider;
import brightspot.languagemenu.LanguageMenuSiteSettings;
import brightspot.logo.ImageLogo;
import brightspot.logo.Logo;
import brightspot.permalink.Permalink;
import brightspot.promo.page.PagePromotable;
import brightspot.requestextras.RequestExtras;
import brightspot.requestextras.headelement.CustomHeadElements;
import brightspot.requestextras.headelement.HeadElement;
import brightspot.requestextras.headelement.LinkElement;
import brightspot.requestextras.headelement.MetaElement;
import brightspot.requestextras.headelement.ScriptElement;
import brightspot.requestextras.headelement.StyleElement;
import brightspot.search.NavigationSearch;
import brightspot.search.NavigationSearchSettings;
import brightspot.section.SectionCascadingData;
import brightspot.seo.Seo;
import brightspot.tag.HasTags;
import brightspot.tagmanager.TagManagerSiteSettings;
import brightspot.util.RichTextUtils;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.image.ImageSize;
import com.psddev.cms.l10n.LocaleUtils;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.page.MainContent;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.StorageItem;
import com.psddev.feed.FeedSource;
import com.psddev.feed.FeedUtils;
import com.psddev.localization.LocalizationData;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.facebook.FacebookMetasView;
import com.psddev.styleguide.facebook.OpenGraphMetaView;
import com.psddev.styleguide.page.FaviconView;
import com.psddev.styleguide.page.SeoRobotsMetaView;
import com.psddev.styleguide.twitter.card.TwitterSummaryLargeImageCardView;

public class PageViewModel extends ViewModel<Recordable> {

    public static final String DEFAULT_DATE_FORMAT = "MMMM d, yyyy hh:mm aa";

    public static final int DECIMAL_PLACES_FOR_SECONDS = 3;
    // Copied from ISO_INSTANT except for specifying a specific number of decimal places to use
    public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendInstant(DECIMAL_PLACES_FOR_SECONDS)
        .parseStrict()
        .toFormatter();

    public static final ImageSize PROMO_IMAGE_DATA_SIZE = ImageSize.builder()
        .displayName("LD+JSON Promo Image")
        .internalName("LD+JSON Promo Image")
        .maximumWidth(1486)
        .maximumHeight(1486)
        .build();

    public static final ImageSize CROPPED_PROMO_IMAGE_SIZE = ImageSize.builder()
        .displayName("LD+JSON Promo Image")
        .internalName("LD+JSON Promo Image")
        .width(1200)
        .height(675)
        .build();

    @MainContent
    protected Object mainObject;

    @CurrentSite
    protected Site currentSite;

    @CurrentRequestExtras
    protected RequestExtras requestExtras;

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
        Collection<T> metaViews = new ArrayList<>();
        metaViews.addAll(Stream.of(
                createView(SeoRobotsMetaView.class, model),
                createView(OpenGraphMetaView.class, model),
                createView(TwitterSummaryLargeImageCardView.class, model),
                createView(FacebookMetasView.class, model))
            .filter(viewClass::isInstance)
            .map(viewClass::cast)
            .collect(Collectors.toList()));

        for (CustomHeadElements extra : requestExtras.getByClass(CustomHeadElements.class)) {
            for (HeadElement element : extra.getElements()) {
                if (element instanceof MetaElement) {
                    for (T view : createViews(viewClass, element)) {
                        metaViews.add(view);
                    }
                }
            }
        }
        return metaViews;
    }

    public CharSequence getDescription() {
        // Plain text
        return Optional.ofNullable(model.as(Seo.class))
            .map(Seo::getSeoDescription)
            .orElse(null);
    }

    public <T> Iterable<T> getExtraLinks(Class<T> viewClass) {
        Collection<T> linkViews = new ArrayList<>();

        for (CustomHeadElements extra : requestExtras.getByClass(CustomHeadElements.class)) {
            for (HeadElement element : extra.getElements()) {
                if (element instanceof LinkElement) {
                    for (T view : createViews(viewClass, element)) {
                        linkViews.add(view);
                    }
                }
            }
        }
        return linkViews;
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
        headScriptsList.forEach(headScripts ->
            scriptViews.addAll((Collection<? extends T>) createViews(viewClass, headScripts))
        );

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
        extraBodyItemsList.forEach(extraBodyItems ->
            bodyItems.addAll((Collection<? extends T>) createViews(viewClass, extraBodyItems))
        );

        return bodyItems;
    }

    public CharSequence getLanguage() {
        return Optional.ofNullable(LocaleProvider.getModelLocale(currentSite, model))
            .map(Locale::toLanguageTag)
            .orElse(null);
    }

    public CharSequence getTitle() {
        // Plain text
        return Optional.ofNullable(model.as(Seo.class))
            .map(seo -> seo.getSeoFullTitle(currentSite))
            .orElseGet(() -> Optional.ofNullable(currentSite)
                .map(Site::getSeoDisplayName)
                .orElse(null));
    }

    public CharSequence getCanonicalLink() {
        return Permalink.getPermalink(currentSite, model);
    }

    public CharSequence getFeedLink() {
        return Optional.ofNullable(model.as(FeedSource.class))
            .map(fs -> FeedUtils.getFeedAutoDetectLink(fs, currentSite))
            .map(RawHtml::of)
            .orElse(null);
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

        return createViews(viewClass, model.as(CascadingPageData.class).getNavigation(currentSite));
    }

    public <T> Iterable<T> getSectionNavigation(Class<T> viewClass) {

        return createViews(viewClass, model.as(SectionCascadingData.class).getSectionNavigation(currentSite));
    }

    public <T> Iterable<T> getLogo(Class<T> viewClass) {

        return createViews(viewClass, model.as(CascadingPageData.class).getLogo(currentSite));
    }

    public <T> Iterable<T> getHat(Class<T> viewClass) {

        return createViews(viewClass, model.as(CascadingPageData.class).getHat(currentSite));
    }

    public <T> Iterable<T> getLanguageMenu(Class<T> viewClass) {

        return createViews(
            viewClass,
            SiteSettings.get(
                currentSite,
                siteSettings -> siteSettings.as(LanguageMenuSiteSettings.class).getLanguageMenu()));
    }

    public CharSequence getSearchAction() {
        NavigationSearch search = SiteSettings.get(
            currentSite,
            siteSettings -> siteSettings.as(NavigationSearchSettings.class).getSearchPage());
        return search == null ? null : search.getSearchAction(currentSite);
    }

    public <T> Iterable<T> getFooterLogo(Class<T> viewClass) {

        Footer footer = model.as(CascadingPageData.class).getFooter(currentSite);

        if (!(footer instanceof PageFooter)) {
            return null;
        }

        return createViews(viewClass, ((PageFooter) footer).getLogo());
    }

    public <T> Iterable<T> getFooterNavigation(Class<T> viewClass) {

        Footer footer = model.as(CascadingPageData.class).getFooter(currentSite);

        if (!(footer instanceof PageFooter)) {
            return null;
        }

        return createViews(viewClass, ((PageFooter) footer).getNavigation());
    }

    public <T> Iterable<T> getFooterContent(Class<T> viewClass) {

        Footer footer = model.as(CascadingPageData.class).getFooter(currentSite);

        if (!(footer instanceof PageFooter)) {
            return null;
        }

        return createViews(viewClass, ((PageFooter) footer).getContent());
    }

    public <T> Iterable<T> getDisclaimer(Class<T> viewClass) {

        Footer footer = model.as(CascadingPageData.class).getFooter(currentSite);

        if (!(footer instanceof PageFooter)) {
            return null;
        }

        return RichTextUtils.buildHtml(((PageFooter) footer), PageFooter::getDisclaimer, e -> createView(viewClass, e));
    }

    public <T> Iterable<T> getAbove(Class<T> viewClass) {

        return createViews(viewClass, model.as(CascadingPageData.class).getAbove(currentSite));
    }

    public <T> Iterable<T> getBelow(Class<T> viewClass) {

        return createViews(viewClass, model.as(CascadingPageData.class).getBelow(currentSite));
    }

    public <T> Iterable<T> getAside(Class<T> viewClass) {

        return createViews(viewClass, model.as(CascadingPageData.class).getAside(currentSite));
    }

    public <T> Iterable<T> getActions(Class<T> viewClass) {

        return createViews(viewClass, model);
    }

    public <T> Iterable<T> getBreadcrumbs(Class<T> viewClass) {

        if (!(model instanceof HasBreadcrumbs)) {
            return null;
        }

        return createViews(viewClass, model.as(HasBreadcrumbs.class).getBreadcrumbs());
    }

    public <T> Iterable<T> getSocial(Class<T> viewClass) {

        return createViews(viewClass, SiteSettings.get(currentSite, s -> s));
    }

    public <T> Iterable<T> getTags(Class<T> viewClass) {

        if (!(model instanceof HasTags)) {
            return null;
        }

        return createViews(viewClass, model.as(HasTags.class).getVisibleTags());
    }

    public <T> Iterable<T> getTagManager(Class<T> viewClass) {
        return Optional.ofNullable(SiteSettings.get(
                currentSite,
                siteSettings -> siteSettings.as(TagManagerSiteSettings.class).getTagManager()))
            .map(tm -> createViews(viewClass, tm))
            .orElse(Collections.emptyList());
    }

    /**
     * Gets {@code href} to manifest.json file. The output of this file is produced by {@link ManifestFilter} which
     * retrieves the configuration from {@link ManifestSettings#getWebAppManifest()}.
     *
     * @return {@code href} to manifest.json
     */
    public CharSequence getManifestLink() {
        String manifest = SiteSettings.get(
            currentSite,
            siteSettings -> siteSettings.as(ManifestSettings.class).getWebAppManifest());

        if (manifest == null) {
            return null;
        }

        return ManifestFilter.PATH;
    }

    /**
     * Gets {@link FaviconView}s for the recommended favicon implementations. {@link FaviconFilter} handles favicon
     * requests, and automatically resizes {@link FaviconSettings#getFavicon()}.
     *
     * @return {@link FaviconView}s
     */
    public Iterable<FaviconView> getFavicons() {
        StorageItem favicon = SiteSettings.get(
            currentSite,
            siteSettings -> siteSettings.as(FaviconSettings.class).getFavicon());

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

    public <T> Iterable<T> getBanners(Class<T> viewClass) {
        List<Banner> activeBanners = Optional.ofNullable(model.as(CascadingPageData.class).getBanners(currentSite))
            .orElse(new ArrayList<>())
            .stream()
            .filter(Banner::isActive)
            .collect(Collectors.toList());

        return createViews(viewClass, activeBanners);
    }

    public CharSequence getContentId() {
        return State.getInstance(model).getId().toString();
    }

    // -- AmpPageViewModel bridge methods

    public <T> Iterable<T> getAmpIntegrations(Class<T> viewClass) {

        // TODO re-do when AMP support is included
        return null;
    }

    public <T> Iterable<T> getAmpCustomAnalytics(Class<T> viewClass) {

        // TODO re-do when AMP support is included
        return null;
    }

    public static String formatDateISO(Date date) {
        return Optional.ofNullable(date)
            .map(Date::toInstant)
            .map(PageViewModel::formatDateISO)
            .orElse(null);
    }

    public static String formatDateISO(long timestamp) {
        return Optional.of(timestamp)
            .map(Instant::ofEpochMilli)
            .map(PageViewModel::formatDateISO)
            .orElse(null);
    }

    public static String formatDateISO(Instant timestamp) {
        return Optional.ofNullable(timestamp)
            .map(instant -> ZonedDateTime.ofInstant(instant, ZoneOffset.UTC).format(DATE_FORMATTER))
            .orElse(null);
    }

    public Map<String, Object> getPublisherData() {
        return getPublisherData(model, currentSite);
    }

    /**
     * Note that this Map structure is also used to produce the fallback image for the main "image" key on LD+JSON
     * NewsArticle types.
     *
     * The "logo" key MUST NOT be changed!
     *
     * @return
     */
    public static Map<String, Object> getPublisherData(Recordable model, Site site) {

        StorageItem logoStorageItem = getPublisherLogoStorageItem(model, site);
        ImageSize logoSize = getPublisherLogoImageSize();

        Map<String, Object> resizedLogoJson = Optional.ofNullable(ImageSchemaData.from(logoStorageItem, logoSize))
            .map(ImageSchemaData::toMap)
            .orElse(null);

        Map<String, Object> publisherJson = new HashMap<>();
        publisherJson.put("@type", "Organization");
        publisherJson.put("name", site != null ? site.getName() : "");
        if (resizedLogoJson != null) {
            publisherJson.put("logo", resizedLogoJson);
        }

        return publisherJson;
    }

    public static StorageItem getPublisherLogoStorageItem(Recordable model, Site site) {

        Logo ampLogo = model.as(CascadingPageData.class).getAmpLogo(site);

        if (ampLogo == null) {
            ampLogo = model.as(CascadingPageData.class).getLogo(site);
        }

        if (!(ampLogo instanceof ImageLogo)) {
            return null;
        }

        return Optional.ofNullable(((ImageLogo) ampLogo).getImage())
            .map(WebImage::getFile)
            .orElse(null);
    }

    public static ImageSize getPublisherLogoImageSize() {
        return ImageSize.builder()
            .displayName("LD+JSON Publisher Logo")
            .internalName("LD+JSON Publisher Logo")
            .maximumWidth(600)
            .maximumHeight(60)
            .build();
    }

    public Iterable<ImageSchemaData> getImageData() {
        return getImageData(model, currentSite);
    }

    public static Iterable<ImageSchemaData> getImageData(Recordable model, Site site) {
        if (model == null) {
            return null;
        }

        StorageItem promoImage = Optional.of(model)
            .filter(recordable -> recordable.isInstantiableTo(PagePromotable.class))
            .map(recordable -> recordable.as(PagePromotable.class))
            .map(PagePromotable::getPagePromotableImage)
            .map(WebImageAsset::getWebImageAssetFile)
            .orElse(null);

        if (promoImage == null) {
            // Fallback to publisher logo

            return Collections.singletonList(ImageSchemaData.from(
                getPublisherLogoStorageItem(model, site),
                getPublisherLogoImageSize()));
        }

        return ImmutableList.of(
            ImageSchemaData.from(
                promoImage,
                PROMO_IMAGE_DATA_SIZE
            ),
            ImageSchemaData.from(
                promoImage,
                CROPPED_PROMO_IMAGE_SIZE
            )
        );
    }

    public <T> Iterable<T> getCommenting(Class<T> viewClass) {
        return createViews(viewClass, SiteSettings.get(
            currentSite,
            siteSettings -> siteSettings.as(CommentingSiteSettings.class).getCommentingServices()));
    }

    public <T> Iterable<T> languageVariantLinks(Class<T> viewClass) {
        List<PageHrefLangLink> items = LocaleUtils.getAllLocaleVariants(model)
            .stream()
            .filter(variant -> State.getInstance(variant).isVisible())
            .filter(Recordable.class::isInstance)
            .map(Recordable.class::cast)
            .map(obj -> obj.as(LocalizationData.class))
            .map(localizedObject -> {
                String languageCode = Optional.of(localizedObject)
                    .map(LocalizationData::getLocale)
                    .map(Locale::toLanguageTag)
                    .orElse(null);

                String url = Permalink.getPermalink(currentSite, localizedObject);

                return new PageHrefLangLink(languageCode, url);
            })
            .collect(Collectors.toList());

        return createViews(viewClass, items);
    }
}
