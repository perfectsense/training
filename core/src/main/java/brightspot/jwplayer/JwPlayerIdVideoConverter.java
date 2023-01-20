package brightspot.jwplayer;

import java.util.Collection;
import java.util.Collections;

import brightspot.jwplayer.db.JwPlayerVideo;
import com.psddev.cms.db.ExternalItemConverter;

/**
 * Creates a {@link JwPlayerIdVideo} from a {@link JwPlayerVideo}
 */
public class JwPlayerIdVideoConverter extends ExternalItemConverter<JwPlayerVideo, JwPlayerIdVideo> {

    @Override
    public Collection<? extends JwPlayerIdVideo> convert(JwPlayerVideo jwPlayerVideo) {
        JwPlayerIdVideo jwPlayerIdVideo = new JwPlayerIdVideo();
        jwPlayerIdVideo.setJwPlayerVideo(jwPlayerVideo);
        jwPlayerIdVideo.setMediaId(jwPlayerVideo.getMediaId());
        jwPlayerIdVideo.setJwPlayerSettings(jwPlayerVideo.getAccountSettings());

        return Collections.singleton(jwPlayerIdVideo);
    }
}
