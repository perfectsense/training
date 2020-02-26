package brightspot.core.video;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import brightspot.core.page.AbstractCreativeWorkPageViewModel;
import brightspot.core.page.PageViewModel;
import brightspot.core.playlist.Playlist;
import brightspot.core.playlist.PlaylistItem;
import brightspot.core.playlist.PlaylistItemStream;
import brightspot.core.timedcontentitemstream.TimedContentItem;
import brightspot.core.tool.DateTimeUtils;
import brightspot.core.update.LastUpdatedProvider;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.HttpParameter;
import com.psddev.dari.db.Query;
import com.psddev.styleguide.core.page.MediaObjectPageViewCollectionsField;
import com.psddev.styleguide.core.page.MediaObjectPageViewMediaField;
import com.psddev.styleguide.core.video.VideoPageView;
import com.psddev.styleguide.core.video.VideoPageViewPaginationField;
import com.psddev.styleguide.core.video.VideoPageViewPlayerField;
import com.psddev.styleguide.core.video.VideoPageViewPlaylistField;

public class VideoPageViewModel extends AbstractCreativeWorkPageViewModel<Video>
    implements VideoPageView, PageEntryView {

    @HttpParameter("playlistId")
    protected UUID playlistId;

    private String playerId = null;

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);
    }

    public CharSequence getDateExpired() {
        return null;
    }

    @Override
    public CharSequence getDateExpiredISO() {
        return null;
    }

    @Override
    public CharSequence getDatePublished() {
        // Plain text
        return DateTimeUtils.format(model.getPublishDate(), VideoPageView.class, DATE_FORMAT_KEY, getSite(),
            PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getDateModified() {
        // Plain text
        return DateTimeUtils.format(LastUpdatedProvider.getMostRecentUpdateDate(model), VideoPageView.class,
            DATE_FORMAT_KEY, getSite(), PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getDatePublishedISO() {
        Date publishDate = model.getPublishDate();
        return publishDate != null ? publishDate.toInstant().toString() : null;
    }

    @Override
    public CharSequence getDateModifiedISO() {
        Date updateDate = LastUpdatedProvider.getMostRecentUpdateDate(model);
        return updateDate != null ? updateDate.toInstant().toString() : null;
    }

    @Override
    public Iterable<? extends VideoPageViewPlaylistField> getPlaylist() {

        Site site = getSite();
        Playlist playlist = null;

        // Try to acquire a Playlist using a query parameter representing a Playlist context
        if (playlistId != null) {

            Playlist requestedPlaylist = Query.from(Playlist.class)
                .where("_id = ?", playlistId)
                .and("getItems = ?", model)
                .first();

            // only surface a playlist by ID if it contains the video
            playlist = requestedPlaylist;
        }
        // Fall back to a default Playlist defined in VideoCascadingData
        if (playlist == null) {

            playlist = model.as(VideoCascadingData.class).getDefaultPlaylist(site);
        }

        // Fall back to the most recently published Playlist containing this Video
        if (playlist == null) {

            playlist = Query.from(Playlist.class)
                .where("getItems = ?", model)
                .sortDescending(Content.PUBLISH_DATE_FIELD)
                .first();
        }

        if (playlist != null) {

            List<PlaylistItem> items = new ArrayList<>();

            PlaylistItem first = new PlaylistItem();
            first.setTimedContent(model);

            PlaylistItemStream itemStream = playlist.getItemStream();

            if (itemStream != null) {
                for (TimedContentItem timedContent : itemStream.getItems(site, playlist, 0,
                    itemStream.getItemsPerPage(site, model))) {
                    PlaylistItem playlistItem = new PlaylistItem();
                    playlistItem.setTimedContent(timedContent.getTimedContentItemContent());

                    if (!model.equals(timedContent)) {
                        items.add(playlistItem);
                    }
                }
            }

            items.add(0, first);

            return createViews(VideoPageViewPlaylistField.class, items);
        }

        return null;
    }

    @Override
    public Iterable<? extends VideoPageViewPlayerField> getPlayer() {
        Iterable<VideoPageViewPlayerField> playerViews = createViews(
            VideoPageViewPlayerField.class,
            model.getVideoProvider());

        playerViews.forEach(playerView -> {
            if (playerView instanceof AbstractPlayerViewModel) {
                ((AbstractPlayerViewModel) playerView).setPlayerId(getPlayerId());
            }
        });

        return playerViews;
    }

    @Override
    public CharSequence getPermalink() {
        return getCanonicalLink();
    }

    public String getPlayerId() {
        if (playerId == null) {
            playerId = "f" + UUID.randomUUID().toString().replace("-", "");
        }

        return playerId;
    }

    @Override
    public Iterable<? extends MediaObjectPageViewMediaField> getMedia() {
        return createViews(MediaObjectPageViewMediaField.class, model.getThumbnail());
    }

    @Override
    public Iterable<? extends VideoPageViewPaginationField> getPagination() {
        return null;
    }

    @Override
    public CharSequence getCaption() {
        return model.getPromotableDescription();
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

    // MediaObjectPageView

    @Override
    public CharSequence getBitrate() {
        return null;
    }

    @Override
    public Iterable<? extends MediaObjectPageViewCollectionsField> getCollections() {
        return null;
    }

    @Override
    public CharSequence getDuration() {
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
}
