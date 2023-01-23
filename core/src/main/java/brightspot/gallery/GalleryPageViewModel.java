package brightspot.gallery;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import brightspot.author.AuthoringPageViewModel;
import brightspot.imageitemstream.ImageItemStream;
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
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.web.annotation.WebParameter;
import com.psddev.styleguide.PlainText;
import com.psddev.styleguide.gallery.GalleryPageView;
import com.psddev.styleguide.gallery.GalleryPageViewGalleryBodyField;
import com.psddev.styleguide.gallery.GalleryPageViewPaginationField;
import com.psddev.styleguide.gallery.GalleryPageViewSlidesField;
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

@JsonLdType("ImageGallery")
public class GalleryPageViewModel extends AbstractContentPageViewModel<Gallery> implements
        GalleryPageView,
        PageEntryView {

    private static final String DATE_FORMAT_KEY = "dateFormat";

    @CurrentLocale
    private Locale locale;

    @CurrentPageViewModel(AuthoringPageViewModel.class)
    AuthoringPageViewModel authoringPage;

    @CurrentSite
    private Site site;

    // TODO: only for testing - improve this
    @WebParameter("page")
    protected int pageIndex;

    protected long offset;

    protected int limit;

    @Override
    protected void onCreate(ViewResponse response) {

        ImageItemStream itemStream = model.getItemStream();
        if (itemStream == null) {
            return;
        }

        limit = itemStream.getItemsPerPage(site, this);

        if (pageIndex > 0) {
            offset = itemStream.getItemsPerPage(site, this) * (pageIndex - 1L);
            return;
        }

        pageIndex = 1;
    }

    @Override
    public CharSequence getDateModified() {
        // Plain text
        return DateTimeUtils.format(model.getUpdateDate(), GalleryPageView.class, DATE_FORMAT_KEY, site,
                locale, PageViewModel.DEFAULT_DATE_FORMAT
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

    @Override
    public CharSequence getDatePublished() {
        // Plain text
        return DateTimeUtils.format(model.getPublishDate(), GalleryPageView.class, DATE_FORMAT_KEY, site,
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
                Gallery::getTitle,
                e -> createView(CreativeWorkPageViewHeadlineField.class, e));
    }

    @Override
    public CharSequence getSource() {

        // TODO need HasSource model 2021-05-18
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
                Gallery::getDescription,
                e -> createView(CreativeWorkPageViewSubHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends GalleryPageViewGalleryBodyField> getGalleryBody() {
        if (model.getBody() != null) {
            return RichTextUtils.buildHtml(
                model,
                Gallery::getBody,
                e -> createView(GalleryPageViewGalleryBodyField.class, e));
        }
        return null;
    }

    @Override
    public Iterable<? extends GalleryPageViewPaginationField> getPagination() {
        ImageItemStream itemStream = model.getItemStream();

        if (itemStream == null) {
            return null;
        }

        List<LinkView> pagination = new ArrayList<>();

        if (pageIndex > 1) {
            pagination.add(new LinkView.Builder()
                .body(Collections.singleton(PlainText.of("Previous Page")))
                .href("?page=" + (pageIndex - 1))
                .build());
        }

        if (itemStream.hasItems(site, model, offset + limit, limit)) {
            pagination.add(new LinkView.Builder()
                .body(Collections.singleton(PlainText.of("Next Page")))
                .href("?page=" + (pageIndex + 1))
                .build());
        }

        return pagination;
    }

    @Override
    public Iterable<? extends GalleryPageViewSlidesField> getSlides() {
        return createViews(
            GalleryPageViewSlidesField.class,
            Optional.ofNullable(model.getItemStream())
                .map(galleryItemStream -> galleryItemStream.getItems(site, model, offset, limit))
                .orElse(null));
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
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
        return RichTextUtils.richTextToPlainText(model.getDescription());
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
