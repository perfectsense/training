package brightspot.recipe;

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
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorBiographyField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewContributorsField;
import com.psddev.styleguide.page.CreativeWorkPageViewHeadlineField;
import com.psddev.styleguide.page.CreativeWorkPageViewPeopleField;
import com.psddev.styleguide.page.CreativeWorkPageViewSubHeadlineField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.recipe.RecipeArticlePageView;
import com.psddev.styleguide.recipe.RecipeArticlePageViewArticleBodyField;
import com.psddev.styleguide.recipe.RecipeArticlePageViewLeadField;
import com.psddev.styleguide.recipe.RecipeArticlePageViewRecipeField;

@JsonLdType("Article")
public class RecipeArticlePageViewModel extends AbstractContentPageViewModel<RecipeArticle> implements
    PageEntryView,
    RecipeArticlePageView {

    private static final String DATE_FORMAT_KEY = "dateFormat";

    @CurrentLocale
    private Locale locale;

    @CurrentPageViewModel(AuthoringPageViewModel.class)
    private AuthoringPageViewModel authoringPage;

    // --- CreativeWorkPageView support ---

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
    public Iterable<? extends CreativeWorkPageViewHeadlineField> getHeadline() {
        return RichTextUtils.buildInlineHtml(
            model,
            RecipeArticle::getHeadline,
            e -> createView(CreativeWorkPageViewHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewPeopleField> getPeople() {
        return authoringPage.getAuthors(CreativeWorkPageViewPeopleField.class);
    }

    @Override
    public CharSequence getSource() {
        return null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSubHeadlineField> getSubHeadline() {
        return RichTextUtils.buildInlineHtml(
            model,
            RecipeArticle::getSubheadline,
            e -> createView(CreativeWorkPageViewSubHeadlineField.class, e));
    }

    // --- PageView support ---

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }

    // --- RecipeArticlePageView support ---

    @Override
    public Iterable<? extends RecipeArticlePageViewArticleBodyField> getArticleBody() {
        return RichTextUtils.buildHtml(
                model,
                RecipeArticle::getBody,
                e -> createView(RecipeArticlePageViewArticleBodyField.class, e));
    }

    @Override
    public Iterable<? extends RecipeArticlePageViewLeadField> getLead() {
        return createViews(RecipeArticlePageViewLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends RecipeArticlePageViewRecipeField> getRecipe() {
        return createViews(RecipeArticlePageViewRecipeField.class, model.getRecipe());
    }

    // --- JSON-LD support ---

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

    @JsonLdNode("description")
    public CharSequence getDescriptionJsonLd() {
        return RichTextUtils.richTextToPlainText(model.getSubheadline());
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

    @JsonLdNode("author")
    public Iterable<? extends PersonSchemaViewModel> getPersonData() {
        return model.getAuthors().stream()
            .map(a -> createView(PersonSchemaViewModel.class, a))
            .collect(Collectors.toList());
    }

    @JsonLdNode("headline")
    public CharSequence getTruncatedHeadline() {
        String seoTitle = model.getSeoTitle();
        if (seoTitle != null && seoTitle.length() >= 110) {
            seoTitle = seoTitle.substring(0, 109);
        }
        return seoTitle;
    }
}
