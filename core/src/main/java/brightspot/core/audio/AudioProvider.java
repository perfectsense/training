package brightspot.core.audio;

import java.util.List;

import brightspot.core.timed.TimedContentAssetProvider;
import brightspot.core.timed.TimedContentMetaData;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

/**
 * An abstract class, similar to {@link brightspot.core.video.VideoProvider}. Base implementation available in {@link
 * Html5AudioProvider}
 */
@Recordable.Embedded
public abstract class AudioProvider extends Record implements TimedContentAssetProvider {

    public static final String INTERNAL_NAME = "brightspot.core.audio.AudioProvider";

    public abstract List<AudioStorageItemWrapper> getItems();

    @ToolUi.Hidden
    private TimedContentMetaData timedContentMetaData;

    /**
     * Gets the provider specific unique ID of the audio.
     *
     * @return Nullable.
     */
    protected abstract String getAudioId();

    /**
     * Gets the fallback title for the audio.
     *
     * @return Nullable.
     */
    protected String getAudioTitleFallback() {
        return null;
    }

    /**
     * Gets the fallback description for the audio.
     *
     * @return Nullable.
     */
    protected String getAudioDescriptionFallback() {
        return null;
    }

    /**
     * Gets the fallback thumbnail for the audio.
     *
     * @return Nullable.
     */
    protected StorageItem getAudioThumbnailFallback() {
        return null;
    }

    /**
     * Gets the duration (in milliseconds) of the audio.
     *
     * @return Nullable
     */
    protected abstract Long getAudioDuration();

    /**
     * Gets the OEmbed Spec provider_name value.
     *
     * @return Nullable
     */
    // TODO: Make abstract?
    protected String getOembedProviderName() {
        return null;
    }

    /**
     * Gets the URL to an embeddable stream.
     *
     * @return Nullable
     */
    // TODO: Make abstract?
    public String getEmbeddableStreamUrl() {
        return null;
    }

    /**
     * Gets the mime type of an embeddable stream.
     *
     * @return Nullable
     */
    // TODO: Make abstract?
    protected String getEmbeddableStreamMimeType() {
        return null;
    }

    @Override
    public String getAssetId() {
        return getAudioId();
    }

    @Override
    public TimedContentMetaData getTimedContentMetadata() {
        return timedContentMetaData;
    }

    @Override
    public void setTimedContentMetadata(TimedContentMetaData timedContentMetadata) {
        this.timedContentMetaData = timedContentMetadata;
    }

    @Override
    public Long getTimedContentDuration() {
        return getAudioDuration();
    }

    @Override
    public StorageItem getTimedContentThumbnailFallback() {
        return null;
    }
}
