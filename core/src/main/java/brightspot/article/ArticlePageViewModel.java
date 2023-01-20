package brightspot.article;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.ad.injection.rte.RichTextAdInjectionPreprocessor;
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
import brightspot.util.SmartQuotesRichTextPreprocessor;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.page.MainContent;
import com.psddev.cms.rte.EditorialMarkupRichTextPreprocessor;
import com.psddev.cms.rte.LineBreakRichTextPreprocessor;
import com.psddev.cms.rte.RichTextViewBuilder;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.article.ArticlePageView;
import com.psddev.styleguide.article.ArticlePageViewArticleBodyField;
import com.psddev.styleguide.article.ArticlePageViewLeadField;
import com.psddev.styleguide.link.LinkView;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorBiographyField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorsField;
import com.psddev.styleguide.page.CreativeWorkPageViewHeadlineField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorLogoField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewSubHeadlineField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.page.promo.PagePromoView;

@JsonLdType("Article")
public class ArticlePageViewModel extends AbstractContentPageViewModel<Article>
        implements ArticlePageView, PageEntryView {

    private static final String DATE_FORMAT_KEY = "dateFormat";

    @CurrentLocale
    private Locale locale;

    @CurrentPageViewModel(AuthoringPageViewModel.class)
    AuthoringPageViewModel authoringPage;

    @CurrentSite
    private Site site;

    @MainContent
    private Object mainObject;

    @Override
    public Iterable<? extends ArticlePageViewArticleBodyField> getArticleBody() {
        return Optional.ofNullable(model)
                .map(Article::getBody)
                .map(body -> new RichTextViewBuilder<ArticlePageViewArticleBodyField>(model, Article::getBody)
                        .addPreprocessor(new EditorialMarkupRichTextPreprocessor())
                        .addPreprocessor(new LineBreakRichTextPreprocessor())
                        .addPreprocessor(new SmartQuotesRichTextPreprocessor())
                        .addPreprocessor(new RichTextAdInjectionPreprocessor(
                                model.getState().getDatabase(), site, mainObject))
                        .elementToView(e -> createView(ArticlePageViewArticleBodyField.class, e))
                        .build())
                .orElse(null);
    }

    @Override
    public Iterable<? extends ArticlePageViewLeadField> getLead() {
        return createViews(ArticlePageViewLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        // TODO this shouldn't even be present on ArticlePageView
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
        return DateTimeUtils.format(LastUpdatedProvider.getMostRecentUpdateDate(model), ArticlePageView.class,
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
                Article::getHeadline,
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
                Article::getSubheadline,
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

    /* DEPRECATED KEYS BELOW */

    @Override
    public Iterable<? extends CreativeWorkPageViewAuthorBiographyField> getAuthorBiography() {
        return authoringPage.getAuthorBiography(CreativeWorkPageViewAuthorBiographyField.class);
    }

    @Override
    public Map<String, ?> getAuthorImage() {
        return authoringPage.getAuthorImageAttributes();
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewAuthorNameField> getAuthorName() {
        return authoringPage.getAuthorName(CreativeWorkPageViewAuthorNameField.class);
    }

    @Override
    public CharSequence getAuthorUrl() {
        return authoringPage.getAuthorUrl();
    }

    @Override
    public Iterable<? extends LinkView> getContributors() {
        return authoringPage.getContributors(LinkView.class);
    }

    @Override
    public Iterable<? extends PagePromoView> getPeople() {
        return authoringPage.getAuthors(PagePromoView.class);
    }
}
