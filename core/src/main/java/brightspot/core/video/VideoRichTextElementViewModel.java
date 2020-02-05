package brightspot.core.video;

import java.util.Optional;

import brightspot.core.timed.TimedContentMetaData;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.video.VideoEnhancementView;
import com.psddev.styleguide.core.video.VideoEnhancementViewPlayerField;
import com.psddev.styleguide.core.video.VideoEnhancementViewPlaylistField;
import com.psddev.styleguide.core.video.VideoEnhancementViewThumbnailField;

public class VideoRichTextElementViewModel extends ViewModel<VideoRichTextElement> implements VideoEnhancementView {

    @CurrentSite
    protected Site currentSite;

    @Override
    public CharSequence getTitle() {
        return Optional.ofNullable(model.getVideo()).map(TimedContentMetaData::getHeadline).orElse(null);
    }

    @Override
    public Iterable<? extends VideoEnhancementViewPlayerField> getPlayer() {
        return createViews(
            VideoEnhancementViewPlayerField.class,
            Optional.ofNullable(model.getVideo()).map(VideoMetaData::getVideoProvider).orElse(null));
    }

    @Override
    public Iterable<? extends VideoEnhancementViewPlaylistField> getPlaylist() {
        return createViews(VideoEnhancementViewPlaylistField.class, model.getPlaylist());
    }

    @Override
    public Iterable<? extends VideoEnhancementViewThumbnailField> getThumbnail() {
        return Optional.ofNullable(model.getVideo())
            .map(e -> createViews(VideoEnhancementViewThumbnailField.class, e.getThumbnail()))
            .orElse(null);
    }
}
