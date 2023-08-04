package brightspot.blog;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import brightspot.author.AuthoringPageViewModel;
import brightspot.l10n.CurrentLocale;
import brightspot.link.Link;
import brightspot.link.Target;
import brightspot.page.AbstractContentPageViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import brightspot.sponsoredcontent.ContentSponsor;
import brightspot.sponsoredcontent.Sponsor;
import brightspot.sponsoredcontent.SponsoredContentSiteSettings;
import brightspot.update.LastUpdatedProvider;
import brightspot.util.DateTimeUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.article.ArticlePageViewArticleBodyField;
import com.psddev.styleguide.article.ArticlePageViewLeadField;
import com.psddev.styleguide.blog.blogpost.BlogPostPageView;
import com.psddev.styleguide.gallery.GalleryPageView;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorsField;
import com.psddev.styleguide.page.CreativeWorkPageViewHeadlineField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorLogoField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewSubHeadlineField;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

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
            currentSite,
            s -> Optional.ofNullable(s.as(SponsoredContentSiteSettings.class).getSponsoredContentMeaningLink())
                .map(Link::getTarget)
                .map(Target::getValue)
                .orElse(null));
    }

    @Override
    public CharSequence getSponsorMeaningUrl() {
        return SiteSettings.get(
            currentSite,
            s -> Optional.ofNullable(s.as(SponsoredContentSiteSettings.class).getSponsoredContentMeaningLink())
                .map(link -> link.getLinkUrl(currentSite))
                .orElse(null));
    }

    @Override
    public CharSequence getSponsorDisplayText() {
        return null;
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
            .map(link -> link.getLinkUrl(currentSite))
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
            BlogPostPage::getSubheadline,
            e -> createView(CreativeWorkPageViewSubHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        // TODO this should not be present on view
        return null;
    }
}
