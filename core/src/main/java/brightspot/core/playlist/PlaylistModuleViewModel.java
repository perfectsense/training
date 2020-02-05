package brightspot.core.playlist;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.link.Link;
import brightspot.core.link.Target;
import brightspot.core.timed.TimedContent;
import brightspot.core.timedcontentitemstream.ExistingPlaylistItemStream;
import brightspot.core.timedcontentitemstream.TimedContentItem;
import brightspot.core.video.Video;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.video.VideoModuleView;
import com.psddev.styleguide.core.video.VideoModuleViewPlayerField;
import com.psddev.styleguide.core.video.VideoModuleViewPlaylistField;

public class PlaylistModuleViewModel extends ViewModel<PlaylistModule> implements VideoModuleView {

    @CurrentSite
    protected Site site;

    protected Optional<Video> firstVideo;

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);

        // get first video
        firstVideo = Optional.ofNullable(model
            .getPlaylist())
            .map(playlistItemStream -> playlistItemStream.getItems(site, model, 0, model.getMaxItems()))
            .orElse(Collections.emptyList())
            .stream()
            .map(TimedContentItem::getTimedContentItemContent)
            .filter(Video.class::isInstance)
            .map(Video.class::cast)
            .findFirst();

    }

    @Override
    public CharSequence getCtaTarget() {
        return Optional.ofNullable(model.getCallToAction())
            .map(Link::getTarget)
            .map(Target::getValue)
            .orElse(null);
    }

    @Override
    public CharSequence getCtaUrl() {
        return Optional.ofNullable(model.getCallToAction())
            .map(l -> l.getLinkUrl(site))
            .orElse(null);
    }

    @Override
    public Iterable<? extends VideoModuleViewPlayerField> getPlayer() {
        return createViews(
            VideoModuleViewPlayerField.class,
            firstVideo
                .map(Video::getVideoProvider)
                .orElse(null));
    }

    @Override
    public Iterable<? extends VideoModuleViewPlaylistField> getPlaylist() {
        return createViews(VideoModuleViewPlaylistField.class, Optional.ofNullable(model.getPlaylist())
            .map(playlistItemStream -> playlistItemStream.getItems(site, model, 0, model.getMaxItems()))
            .map(list -> list.stream()
                .filter(timedContent -> timedContent instanceof TimedContent || timedContent instanceof PlaylistItem)
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
    public CharSequence getTitle() {
        return Optional.ofNullable(model.getTitle())
            .orElse(Optional.ofNullable(model.getPlaylist())
                .filter(ExistingPlaylistItemStream.class::isInstance)
                .map(ExistingPlaylistItemStream.class::cast)
                .map(ExistingPlaylistItemStream::getPlaylist)
                .map(Playlist::getName)
                .orElse(null));
    }
}
