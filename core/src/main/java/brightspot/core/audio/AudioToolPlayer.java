package brightspot.core.audio;

import brightspot.core.timed.PlyrMediaToolPlayerServlet;
import brightspot.core.timed.TimedContentToolPlayer;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.util.StorageItem;

/**
 * A player implementation for TimedContent centered around Audio playback.
 */
public class AudioToolPlayer implements TimedContentToolPlayer {

    private Audio audio;

    public AudioToolPlayer(Audio audio) {
        this.audio = audio;
    }

    @Override
    public StorageItem getTimedContentToolPlayerPreview(ToolPageContext page) {
        return audio.getPreviewStorageItem();
    }

    @Override
    public String getTimedContentPreviewFrameUrl(ToolPageContext page) {

        return buildPlyrMediaToolPlayerUrl();
    }

    @Override
    public String getTimedCompanionSelectionFrameUrl(ToolPageContext page) {

        return buildPlyrMediaToolPlayerUrl();
    }

    private String buildPlyrMediaToolPlayerUrl() {

        String plyrMediaType = getPlyrMediaType();
        String plyrMediaRef = getPlyrMediaRef();

        if (plyrMediaType != null && plyrMediaRef != null) {
            return PlyrMediaToolPlayerServlet.getPageUrl(plyrMediaType, plyrMediaRef);
        }
        return null;
    }

    private String getPlyrMediaType() {
        AudioProvider audioProvider = audio.getAudioProvider();
        if (audioProvider != null) {
            return audioProvider.getEmbeddableStreamMimeType();
        }
        return null;
    }

    private String getPlyrMediaRef() {
        AudioProvider audioProvider = audio.getAudioProvider();
        if (audioProvider != null) {
            return audioProvider.getEmbeddableStreamUrl();
        }
        return null;
    }
}
