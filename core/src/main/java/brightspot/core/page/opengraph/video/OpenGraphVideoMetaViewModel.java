package brightspot.core.page.opengraph.video;

import java.util.Optional;

import brightspot.core.video.Video;
import brightspot.core.video.VideoProvider;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.facebook.OpenGraphVideoMetaView;

public class OpenGraphVideoMetaViewModel extends ViewModel<Video> implements OpenGraphVideoMetaView {

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getSecureUrl() {
        // TODO: When url is exposed on video, place here. Only other option is video providers but it's method is protected.
        return null;
    }

    @Override
    public CharSequence getWidth() {
        return Optional.ofNullable(model.getVideoProvider())
            .map(VideoProvider::getOriginalVideoWidth)
            .map(Object::toString)
            .orElse(null);
    }

    @Override
    // TODO: When mimetype is exposed on video, place here. Only other option is video provider but it's method is protected.
    public CharSequence getMimeType() {
        return "video/*";
    }

    @Override
    public CharSequence getUrl() {
        // TODO: When url is exposed on video, place here. Only other option is video providers but it's method is protected.
        return null;
    }

    @Override
    public CharSequence getHeight() {
        return Optional.ofNullable(model.getVideoProvider())
            .map(VideoProvider::getOriginalVideoHeight)
            .map(Object::toString)
            .orElse(null);
    }
}
