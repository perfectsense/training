package brightspot.article;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.author.AuthoringPageViewModel;
import brightspot.image.ImageSchemaData;
import brightspot.l10n.CurrentLocale;
import brightspot.page.AbstractContentPageViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import brightspot.permalink.Permalink;
import brightspot.seo.PersonSchemaViewModel;
import brightspot.update.LastUpdatedProvider;
import brightspot.util.DateTimeUtils;
import brightspot.util.RichTextUtils;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.article.ArticlePageView;
import com.psddev.styleguide.article.ArticlePageViewArticleBodyField;
import com.psddev.styleguide.article.ArticlePageViewLeadField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorBiographyField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewContributorsField;
import com.psddev.styleguide.page.CreativeWorkPageViewHeadlineField;
import com.psddev.styleguide.page.CreativeWorkPageViewPeopleField;
import com.psddev.styleguide.page.CreativeWorkPageViewSubHeadlineField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

@JsonLdType("Article")
public class ArticlePageViewModel extends AbstractContentPageViewModel<Article>
    implements ArticlePageView, PageEntryView {

    private static final String DATE_FORMAT_KEY = "dateFormat";

    @CurrentLocale
    private Locale locale;

    @CurrentPageViewModel(AuthoringPageViewModel.class)
    AuthoringPageViewModel authoringPage;

    @Override
    public Iterable<? extends ArticlePageViewArticleBodyField> getArticleBody() {
        return RichTextUtils.buildHtml(
                model,
                Article::getBody,
                e -> createView(ArticlePageViewArticleBodyField.class, e));
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
        return ImmutableMap.of(
            "@type", "WebPage",
            "@id", Permalink.getPermalink(page.getSite(), model)
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
        return Optional.ofNullable(ObjectUtils.firstNonNull(LastUpdatedProvider.getMostRecentUpdateDate(model), model.getPublishDate()))
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
    public Iterable<? extends CreativeWorkPageViewContributorsField> getContributors() {
        return authoringPage.getContributors(CreativeWorkPageViewContributorsField.class);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewPeopleField> getPeople() {
        return authoringPage.getAuthors(CreativeWorkPageViewPeopleField.class);
    }
}
