package brightspot.article;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.ad.injection.AdInjectionSiteSettings;
import brightspot.ad.injection.view.BasicViewBasedAdInjectionProfile;
import brightspot.ad.injection.view.ViewBasedAdInjectionProfile;
import brightspot.author.AuthoringPageViewModel;
import brightspot.image.ImageSchemaData;
import brightspot.l10n.CurrentLocale;
import brightspot.link.Link;
import brightspot.link.Target;
import brightspot.page.AbstractContentPageViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import brightspot.permalink.Permalink;
import brightspot.seo.PersonSchemaViewModel;
import brightspot.sponsoredcontent.ContentSponsor;
import brightspot.sponsoredcontent.Sponsor;
import brightspot.sponsoredcontent.SponsoredContentSiteSettings;
import brightspot.update.LastUpdatedProvider;
import brightspot.util.DateTimeUtils;
import brightspot.util.RichTextUtils;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.page.MainContent;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.article.ArticlePageView;
import com.psddev.styleguide.listicle.ListiclePageView;
import com.psddev.styleguide.listicle.ListiclePageViewIntroField;
import com.psddev.styleguide.listicle.ListiclePageViewItemsField;
import com.psddev.styleguide.listicle.ListiclePageViewLeadField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorsField;
import com.psddev.styleguide.page.CreativeWorkPageViewHeadlineField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorLogoField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewSubHeadlineField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

public class ListiclePageViewModel extends AbstractContentPageViewModel<Listicle> implements ListiclePageView,
    PageEntryView {

    private static final String DATE_FORMAT_KEY = "dateFormat";

    protected ViewBasedAdInjectionProfile adInjectionProfile;

    @CurrentLocale
    private Locale locale;

    @MainContent
    private Object mainObject;

    @CurrentSite
    private Site site;

    @CurrentPageViewModel(AuthoringPageViewModel.class)
    AuthoringPageViewModel authoringPage;

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);

        int index = 1;
        for (ListicleItem item : model.getItems()) {
            item.as(ListicleItemModification.class).setIndex(index);
            index++;
        }

        adInjectionProfile = AdInjectionSiteSettings.getAdInjectorForType(
            BasicViewBasedAdInjectionProfile.class,
            site,
            mainObject);
    }

    @Override
    public Iterable<? extends ListiclePageViewIntroField> getIntro() {
        return Optional.ofNullable(model.getIntro())
            .map(introText -> RichTextUtils.buildHtml(
                model,
                model -> introText,
                e -> createView(ListiclePageViewIntroField.class, e))
            )
            .orElse(null);
    }

    @Override
    public Iterable<? extends ListiclePageViewItemsField> getItems() {
        return Optional.ofNullable(model)
            .map(Listicle::getItems)
            .map(listicleItems -> createViews(ListiclePageViewItemsField.class, listicleItems))
            .map(this::injectAds)
            .orElse(null);
    }

    @Override
    public Iterable<? extends ListiclePageViewLeadField> getLead() {
        return createViews(ListiclePageViewLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        // TODO this shouldn't even be present on ListiclePageView
        return null;
    }

    @JsonLdNode("author")
    public Iterable<? extends PersonSchemaViewModel> getPersonData() {
        return model.getAuthors().stream()
            .map(a -> createView(PersonSchemaViewModel.class, a))
            .collect(Collectors.toList());
    }

    @JsonLdNode("image")
    public Iterable<ImageSchemaData> getImageData() {
        return page.getImageData();
    }

    @JsonLdNode("mainEntityOfPage")
    public Map<String, Object> getMainEntityOfPageData() {
        String permalink = Permalink.getPermalink(page.getSite(), model);
        if (permalink == null) {
            return null;
        }

        return ImmutableMap.of(
            "@type", "WebPage",
            "@id", permalink
        );
    }

    @JsonLdNode
    @Override
    public CharSequence getDateModified() {
        // Plain text
        return DateTimeUtils.format(
            LastUpdatedProvider.getMostRecentUpdateDate(model), ArticlePageView.class,
            DATE_FORMAT_KEY, page.getSite(), locale, PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @JsonLdNode("dateModified")
    @Override
    public CharSequence getDateModifiedISO() {
        return Optional.ofNullable(ObjectUtils.firstNonNull(
                LastUpdatedProvider.getMostRecentUpdateDate(model),
                model.getPublishDate()))
            .map(Date::toInstant)
            .map(Instant::toString)
            .orElse(null);
    }

    @JsonLdNode
    @Override
    public CharSequence getDatePublished() {
        // Plain text
        return DateTimeUtils.format(model.getPublishDate(), ArticlePageView.class, DATE_FORMAT_KEY, page.getSite(),
            locale, PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @JsonLdNode("datePublished")
    @Override
    public CharSequence getDatePublishedISO() {
        return Optional.ofNullable(model.getPublishDate())
            .map(Date::toInstant)
            .map(Instant::toString)
            .orElse(null);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewHeadlineField> getHeadline() {
        return RichTextUtils.buildInlineHtml(
            model,
            Listicle::getHeadline,
            e -> createView(CreativeWorkPageViewHeadlineField.class, e));
    }

    @Override
    public CharSequence getSource() {

        // TODO need HasSource model 2021-04-06
        return null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSponsorLogoField> getSponsorLogo() {
        return createViews(
            CreativeWorkPageViewSponsorLogoField.class,
            Optional.ofNullable(model.getSponsor())
                .map(ContentSponsor::getLogo)
                .orElse(null)
        );
    }

    @Override
    public CharSequence getSponsorMeaningTarget() {
        return SiteSettings.get(
            site,
            s -> Optional.ofNullable(s.as(SponsoredContentSiteSettings.class).getSponsoredContentMeaningLink())
                .map(Link::getTarget)
                .map(Target::getValue)
                .orElse(null));
    }

    @Override
    public CharSequence getSponsorMeaningUrl() {
        return SiteSettings.get(
            site,
            s -> Optional.ofNullable(s.as(SponsoredContentSiteSettings.class).getSponsoredContentMeaningLink())
                .map(link -> link.getLinkUrl(site))
                .orElse(null));
    }

    @Override
    public CharSequence getSponsorDisplayText() {
        return Optional.ofNullable(model.getSponsor())
            .filter(Sponsor.class::isInstance)
            .map(Sponsor.class::cast)
            .map(sponsor -> sponsor.getSponsorDisplayTextWithFallback(site))
            .orElse(null);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSponsorNameField> getSponsorName() {
        return Optional.ofNullable(model.getSponsor())
            .map(sponsor -> RichTextUtils.buildInlineHtml(
                sponsor,
                ContentSponsor::getDisplayName,
                e -> createView(CreativeWorkPageViewSponsorNameField.class, e)))
            .orElse(null);
    }

    @Override
    public CharSequence getSponsorTarget() {
        return Optional.ofNullable(model.getSponsor())
            .filter(Sponsor.class::isInstance)
            .map(Sponsor.class::cast)
            .map(Sponsor::getCallToAction)
            .map(Link::getTarget)
            .map(Target::getValue)
            .orElse(null);
    }

    @Override
    public CharSequence getSponsorUrl() {
        return Optional.ofNullable(model.getSponsor())
            .filter(Sponsor.class::isInstance)
            .map(Sponsor.class::cast)
            .map(Sponsor::getCallToAction)
            .map(link -> link.getLinkUrl(site))
            .orElse(null);
    }

    @Override
    public Boolean getSponsored() {
        return model.getSponsor() != null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSubHeadlineField> getSubHeadline() {
        return RichTextUtils.buildInlineHtml(
            model,
            Listicle::getSubheadline,
            e -> createView(CreativeWorkPageViewSubHeadlineField.class, e));
    }

    @JsonLdNode("headline")
    public CharSequence getTruncatedHeadline() {
        String seoTitle = model.getSeoTitle();
        if (seoTitle != null && seoTitle.length() >= 110) {
            seoTitle = seoTitle.substring(0, 109);
        }
        return seoTitle;
    }

    @JsonLdNode("description")
    public CharSequence getDescriptionJsonLd() {
        return RichTextUtils.richTextToPlainText(model.getSubheadline());
    }

    // Authoring Entity

    @Override
    public Iterable<? extends CreativeWorkPageViewAuthorsField> getAuthors() {
        return authoringPage.getAuthors(CreativeWorkPageViewAuthorsField.class);
    }

    private Iterable<ListiclePageViewItemsField> injectAds(
        Iterable<ListiclePageViewItemsField> views) {

        if (adInjectionProfile == null) {
            return views;
        }

        return adInjectionProfile.injectAds(
            site,
            model,
            views,
            1,
            model.getItems().size(),
            adModule -> createView(ListiclePageViewItemsField.class, adModule));
    }
}
