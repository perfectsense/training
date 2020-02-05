package brightspot.core.gallery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import brightspot.core.listmodule.ItemStream;
import brightspot.core.page.AbstractCreativeWorkPageViewModel;
import brightspot.core.page.PageViewModel;
import brightspot.core.tool.DateTimeUtils;
import brightspot.core.tool.RichTextUtils;
import brightspot.core.update.LastUpdatedProvider;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpParameter;
import com.psddev.styleguide.core.gallery.GalleryPageView;
import com.psddev.styleguide.core.gallery.GalleryPageViewEndCardField;
import com.psddev.styleguide.core.gallery.GalleryPageViewGalleryBodyField;
import com.psddev.styleguide.core.gallery.GalleryPageViewPaginationField;
import com.psddev.styleguide.core.gallery.GalleryPageViewSlidesField;
import com.psddev.styleguide.core.link.LinkView;

public class GalleryPageViewModel extends AbstractCreativeWorkPageViewModel<Gallery>
    implements GalleryPageView, PageEntryView {

    private static final String DATE_FORMAT_KEY = "dateFormat";

    @CurrentSite
    private Site site;

    // TODO: only for testing - improve this
    @HttpParameter("page")
    protected int pageIndex;

    protected long offset;

    protected int limit;

    @Override
    protected void onCreate(ViewResponse response) {

        ItemStream itemStream = model.getItemStream();
        if (itemStream == null) {
            return;
        }

        limit = model.getItemStream().getItemsPerPage(site, this);

        if (pageIndex > 0) {
            offset = itemStream.getItemsPerPage(site, this) * (pageIndex - 1);
            return;
        }

        pageIndex = 1;
    }

    @Override
    public CharSequence getDateExpired() {
        return null;
    }

    @Override
    public CharSequence getDateExpiredISO() {
        return null;
    }

    @Override
    public CharSequence getDateModified() {
        // Plain text
        return DateTimeUtils.format(model.getUpdateDate(), GalleryPageView.class, DATE_FORMAT_KEY, site,
            PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getDateModifiedISO() {
        Date updateDate = LastUpdatedProvider.getMostRecentUpdateDate(model);
        return updateDate != null ? updateDate.toInstant().toString() : null;
    }

    @Override
    public CharSequence getDatePublished() {
        // Plain text
        return DateTimeUtils.format(model.getPublishDate(), GalleryPageView.class, DATE_FORMAT_KEY, site,
            PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getDatePublishedISO() {
        Date publishDate = model.getPublishDate();
        return publishDate != null ? publishDate.toInstant().toString() : null;
    }

    @Override
    public CharSequence getHeadline() {
        // Plain text
        return model.getHeadline();
    }

    @Override
    public CharSequence getSource() {
        return null;
    }

    @Override
    public CharSequence getSubHeadline() {
        // Plain text
        return model.getSubHeadline();
    }

    @Override
    public Iterable<? extends GalleryPageViewEndCardField> getEndCard() {
        return createViews(GalleryPageViewEndCardField.class, model.getEndCard());
    }

    @Override
    public Iterable<? extends GalleryPageViewGalleryBodyField> getGalleryBody() {
        if (model.getBody() != null) {
            return RichTextUtils.buildHtml(
                model.getState().getDatabase(),
                model.getBody(),
                e -> createView(GalleryPageViewGalleryBodyField.class, e));
        }
        return null;
    }

    @Override
    public Iterable<? extends GalleryPageViewPaginationField> getPagination() {
        GalleryItemStream itemStream = model.getItemStream();

        if (itemStream == null) {
            return null;
        }

        List<LinkView> pagination = new ArrayList<>();

        if (pageIndex > 1) {
            pagination.add(new LinkView.Builder()
                .body("Previous Page")
                .href("?page=" + (pageIndex - 1))
                .build());
        }

        if (itemStream.hasMoreThan(site, model, offset + limit)) {
            pagination.add(new LinkView.Builder()
                .body("Next Page")
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
    public CharSequence getPageSubHeading() {
        return null;
    }
}
