package brightspot.vimeo;

import java.util.Collection;
import java.util.Collections;

import brightspot.video.VideoData;
import com.psddev.cms.db.ExternalItemConverter;
import com.psddev.vimeo.VimeoVideo;

/**
 * Creates a Vimeo Url Video from a Vimeo Video
 */

public class VimeoUrlVideoConverter extends ExternalItemConverter<VimeoVideo, VimeoUrlVideo> {

    @Override
    public Collection<? extends VimeoUrlVideo> convert(VimeoVideo vimeoVideo) {
        VimeoUrlVideo vimeoUrlVideo = new VimeoUrlVideo();
        vimeoUrlVideo.setVimeoIdentifier(vimeoVideo.getVimeoId());
        VideoData video = vimeoUrlVideo.as(VideoData.class);
        video.setTitleOverride(vimeoVideo.getVideoTitle());
        video.setDescriptionOverride(vimeoVideo.getDescription());

        return Collections.singleton(vimeoUrlVideo);
    }
}
