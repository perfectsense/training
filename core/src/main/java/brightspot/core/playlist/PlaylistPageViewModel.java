package brightspot.core.playlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import brightspot.core.listmodule.ItemStream;
import brightspot.core.page.AbstractContentPageViewModel;
import brightspot.core.page.PageViewModel;
import brightspot.core.timed.TimedContent;
import brightspot.core.timedcontentitemstream.TimedContentItem;
import brightspot.core.tool.DateTimeUtils;
import brightspot.core.tool.DirectoryItemUtils;
import brightspot.core.update.LastUpdatedProvider;
import brightspot.core.video.AbstractPlayerViewModel;
import brightspot.core.video.Video;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.HttpParameter;
import com.psddev.styleguide.core.link.LinkView;
import com.psddev.styleguide.core.page.CreativeWorkPageViewContributorsField;
import com.psddev.styleguide.core.page.CreativeWorkPageViewPeopleField;
import com.psddev.styleguide.core.page.MediaObjectPageViewCollectionsField;
import com.psddev.styleguide.core.page.MediaObjectPageViewMediaField;
import com.psddev.styleguide.core.video.VideoPageView;
import com.psddev.styleguide.core.video.VideoPageViewPaginationField;
import com.psddev.styleguide.core.video.VideoPageViewPlayerField;
import com.psddev.styleguide.core.video.VideoPageViewPlaylistField;

public class PlaylistPageViewModel extends AbstractContentPageViewModel<Playlist>
    implements VideoPageView, PageEntryView {

    protected String playerId;

    protected Optional<Video> firstVideo;

    @HttpParameter("page")
    protected int pageIndex;

    protected long offset;

    protected int limit;

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);

        firstVideo = Optional.ofNullable(model.getItemStream())
            .map(playlistItemStream -> playlistItemStream.getItems(getSite(), model, 0,
                playlistItemStream.getItemsPerPage(getSite(), model)))
            .orElse(Collections.emptyList())
            .stream()
            .map(TimedContentItem::getTimedContentItemContent)
            .filter(Video.class::isInstance)
            .map(Video.class::cast)
            .findFirst();

        ItemStream itemStream = model.getItemStream();
        if (itemStream == null) {
            return;
        }

        limit = model.getItemStream().getItemsPerPage(getSite(), this);

        if (pageIndex > 0) {
            offset = itemStream.getItemsPerPage(getSite(), this) * ((long) (pageIndex - 1));
            return;
        }

        pageIndex = 1;
    }

    @Override
    public Iterable<? extends VideoPageViewPlaylistField> getPlaylist() {
        return createViews(
            VideoPageViewPlaylistField.class,
            Optional.ofNullable(model.getItemStream().getItems(getSite(), model, offset, limit))
                .map(list -> list.stream()
                    .filter(timedContent -> timedContent instanceof TimedContent
                        || timedContent instanceof PlaylistItem)
                    .map(timedContent -> {
                        if (timedContent instanceof PlaylistItem) {
                            return timedContent;
                        }
                        PlaylistItem playlistItem = new PlaylistItem();
                        playlistItem.setTimedContent((TimedContent) timedContent);
                        return playlistItem;
                    }).collect(Collectors.toList()))
                .orElse(null));
    }

    @Override
    public CharSequence getDatePublishedISO() {
        Date publishDate = model.getPublishDate();
        return publishDate != null ? publishDate.toInstant().toString() : null;
    }

    @Override
    public CharSequence getSubHeadline() {
        return firstVideo
            .map(Video::getSubHeadline)
            .orElse(null);
    }

    @Override
    public CharSequence getHeadline() {
        return firstVideo
            .map(Video::getHeadline)
            .orElse(null);
    }

    @Override
    public Iterable<? extends VideoPageViewPlayerField> getPlayer() {
        Iterable<VideoPageViewPlayerField> playerViews = createViews(
            VideoPageViewPlayerField.class,
            firstVideo
                .map(Video::getVideoProvider)
                .orElse(null));

        playerViews.forEach(playerView -> {
            if (playerView instanceof AbstractPlayerViewModel) {
                ((AbstractPlayerViewModel) playerView).setPlayerId(getPlayerId());
            }
        });

        return playerViews;
    }

    @Override
    public CharSequence getDateModified() {
        // Plain text
        return DateTimeUtils.format(LastUpdatedProvider.getMostRecentUpdateDate(model), VideoPageView.class,
            DATE_FORMAT_KEY, getSite(), PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getDatePublished() {
        return DateTimeUtils.format(model.getPublishDate(), VideoPageView.class, DATE_FORMAT_KEY, getSite(),
            PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getPermalink() {
        return DirectoryItemUtils.getCanonicalUrl(getSite(), model);
    }

    @Override
    public CharSequence getDateModifiedISO() {
        Date updateDate = LastUpdatedProvider.getMostRecentUpdateDate(model);
        return updateDate != null ? updateDate.toInstant().toString() : null;
    }

    @Override
    public CharSequence getDuration() {
        return firstVideo
            .map(Video::getDuration)
            .map(String::valueOf)
            .orElse(null);
    }

    protected String getPlayerId() {
        if (playerId == null) {
            playerId = "f" + UUID.randomUUID().toString().replace("-", "");
        }

        return playerId;
    }

    @Override
    public CharSequence getContentId() {
        return firstVideo
            .map(e -> e.getState().getId().toString())
            .orElse(null);
    }

    @Override
    public Iterable<? extends VideoPageViewPaginationField> getPagination() {
        PlaylistItemStream itemStream = model.getItemStream();

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

        if (itemStream.hasMoreThan(getSite(), model, offset + limit)) {
            pagination.add(new LinkView.Builder()
                .body("Next Page")
                .href("?page=" + (pageIndex + 1))
                .build());
        }

        return pagination;
    }

    // MediaObjectPageView

    @Override
    public CharSequence getCaption() {
        return null;
    }

    @Override
    public CharSequence getBitrate() {
        return null;
    }

    @Override
    public Iterable<? extends MediaObjectPageViewCollectionsField> getCollections() {
        return null;
    }

    @Override
    public CharSequence getEmbedUrl() {
        return null;
    }

    @Override
    public CharSequence getFileFormat() {
        return null;
    }

    @Override
    public CharSequence getFileSize() {
        return null;
    }

    @Override
    public CharSequence getFileUrl() {
        return null;
    }

    @Override
    public CharSequence getHeight() {
        return null;
    }

    @Override
    public Iterable<? extends MediaObjectPageViewMediaField> getMedia() {
        return null;
    }

    @Override
    public CharSequence getPlayerType() {
        return null;
    }

    @Override
    public CharSequence getStatus() {
        return null;
    }

    @Override
    public CharSequence getWidth() {
        return null;
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
    public CharSequence getSource() {
        return null;
    }

    @Override
    public CharSequence getPageSubHeading() {
        return null;
    }

    @Override
    public CharSequence getTimedContentTimeStamp() {
        return page.getTimedContentTimeStamp();
    }

    // CreativeWorkPageView

    @Override
    public CharSequence getAuthorBiography() {
        return page.getAuthorBiography();
    }

    @Override
    public Map<String, ?> getAuthorImage() {
        return page.getAuthorImage();
    }

    @Override
    public CharSequence getAuthorName() {
        return page.getAuthorName();
    }

    @Override
    public CharSequence getAuthorUrl() {
        return page.getAuthorUrl();
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewContributorsField> getContributors() {
        return page.getContributors(CreativeWorkPageViewContributorsField.class);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewPeopleField> getPeople() {
        return page.getPeople(CreativeWorkPageViewPeopleField.class);
    }
}
