package brightspot.blog;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import brightspot.author.AuthoringPageViewModel;
import brightspot.l10n.CurrentLocale;
import brightspot.page.AbstractContentPageViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import brightspot.update.LastUpdatedProvider;
import brightspot.util.DateTimeUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.article.ArticlePageViewArticleBodyField;
import com.psddev.styleguide.article.ArticlePageViewLeadField;
import com.psddev.styleguide.blog.blogpost.BlogPostPageView;
import com.psddev.styleguide.gallery.GalleryPageView;
import com.psddev.styleguide.link.LinkView;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorBiographyField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorsField;
import com.psddev.styleguide.page.CreativeWorkPageViewHeadlineField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorLogoField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewSubHeadlineField;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.page.promo.PagePromoView;

public class BlogPostPageViewModel extends AbstractContentPageViewModel<BlogPostPage>
        implements BlogPostPageView, PageEntryView {

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return null;
    }

    private static final String DATE_FORMAT_KEY = "dateFormat";

    public static final String DEFAULT_DATE_FORMAT = "MMMM d, yyyy 'at' h:mm a z";

    @CurrentPageViewModel(PageViewModel.class)
    protected PageViewModel page;

    @CurrentSite
    protected Site currentSite;

    @CurrentLocale
    private Locale locale;

    @CurrentPageViewModel(AuthoringPageViewModel.class)
    AuthoringPageViewModel authoringPage;

    @Override
    public Iterable<? extends ArticlePageViewArticleBodyField> getArticleBody() {
        return RichTextUtils.buildHtml(
                model,
                BlogPostPage::getBody,
                e -> createView(ArticlePageViewArticleBodyField.class, e));
    }

    @Override
    public Iterable<? extends ArticlePageViewLeadField> getLead() {
        return createViews(ArticlePageViewLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewAuthorsField> getAuthors() {
        return authoringPage.getAuthors(CreativeWorkPageViewAuthorsField.class);
    }

    @Override
    public CharSequence getDateModified() {
        return DateTimeUtils.format(model.getUpdateDate(), BlogPostPageView.class, DATE_FORMAT_KEY, currentSite,
                locale, DEFAULT_DATE_FORMAT);
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

    @Override
    public CharSequence getDatePublished() {
        // Plain text
        return DateTimeUtils.format(model.getPublishDate(), GalleryPageView.class, DATE_FORMAT_KEY, currentSite,
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
                BlogPostPage::getHeadline,
                e -> createView(CreativeWorkPageViewHeadlineField.class, e));
    }

    @Override
    public CharSequence getSource() {
        return null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSponsorLogoField> getSponsorLogo() {
        return null;
    }

    @Override
    public CharSequence getSponsorMeaningTarget() {
        return null;
    }

    @Override
    public CharSequence getSponsorMeaningUrl() {
        return null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSponsorNameField> getSponsorName() {
        return null;
    }

    @Override
    public CharSequence getSponsorTarget() {
        return null;
    }

    @Override
    public CharSequence getSponsorUrl() {
        return null;
    }

    @Override
    public Boolean getSponsored() {
        return null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSubHeadlineField> getSubHeadline() {
        return RichTextUtils.buildInlineHtml(
                model,
                BlogPostPage::getSubheadline,
                e -> createView(CreativeWorkPageViewSubHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        // TODO this should not be present on view
        return null;
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
