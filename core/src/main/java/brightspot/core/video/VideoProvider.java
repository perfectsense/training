package brightspot.core.video;

import java.util.List;

import brightspot.core.timed.TimedContentAssetProvider;
import brightspot.core.timed.TimedContentMetaData;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Recordable.Embedded
public abstract class VideoProvider extends Record implements TimedContentAssetProvider {

    public static final String INTERNAL_NAME = "brightspot.core.video.VideoProvider";

    private transient VideoMetaData videoMetaData;

    public final VideoMetaData getVideoMetaData() {
        return videoMetaData;
    }

    public final void setVideoMetaData(VideoMetaData video) {
        this.videoMetaData = video;
    }

    /**
     * Gets the provider specific unique ID of the video.
     *
     * @return Nullable.
     */
    public abstract String getVideoId();

    /**
     * Gets the fallback title for the video.
     *
     * @return Nullable.
     */
    protected String getVideoTitleFallback() {
        return null;
    }

    /**
     * Gets the fallback description for the video.
     *
     * @return Nullable.
     */
    protected String getVideoDescriptionFallback() {
        return null;
    }

    /**
     * Gets the fallback thumbnail for the video.
     *
     * @return Nullable.
     */
    public StorageItem getVideoThumbnailFallback() {
        return null;
    }

    /**
     * Gets the duration (in milliseconds) of the video.
     *
     * @return Nullable
     */
    public abstract Long getVideoDuration();

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
    protected String getEmbeddableStreamUrl() {
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

    /**
     * Gets the width (in pixels) of the video. In the case of videos with multiple renditions, return the original
     * video width or the width of the largest rendition.
     *
     * @return Nullable.
     */
    public abstract Integer getOriginalVideoWidth();

    /**
     * Gets the height (in pixels) of the video. In the case of videos with multiple renditions, return the original
     * video height or the height of the largest rendition.
     *
     * @return Nullable.
     */
    public abstract Integer getOriginalVideoHeight();

    @Override
    public String getAssetId() {
        return getVideoId();
    }

    @Override
    public TimedContentMetaData getTimedContentMetadata() {
        return getVideoMetaData();
    }

    @Override
    public void setTimedContentMetadata(TimedContentMetaData timedContentMetadata) {
        if (timedContentMetadata instanceof VideoMetaData) {
            this.setVideoMetaData((VideoMetaData) timedContentMetadata);
        }
    }

    @Override
    public Long getTimedContentDuration() {
        return getVideoDuration();
    }

    @Override
    public StorageItem getTimedContentThumbnailFallback() {
        return getVideoThumbnailFallback();
    }

    /**
     * @return Nullable.
     */
    protected List<StorageItem> getDownloadableFiles() {
        return null;
    }
}
