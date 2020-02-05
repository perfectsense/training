package brightspot.core.video;

import brightspot.core.timed.IframeToolPlayerServlet;
import brightspot.core.timed.PlyrMediaToolPlayerServlet;
import brightspot.core.timed.TimedContentToolPlayer;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;

/**
 * A player implementation for TimedContent centered around Video playback.
 */
public class VideoToolPlayer implements TimedContentToolPlayer {

    private static final String YOU_TUBE_OEMBED_PROVIDER_NAME = "YouTube";
    private static final String VIMEO_OEMBED_PROVIDER_NAME = "Vimeo";

    private VideoMetaData videoMetaData;

    public VideoToolPlayer(VideoMetaData videoMetaData) {
        this.videoMetaData = videoMetaData;
    }

    @Override
    public StorageItem getTimedContentToolPlayerPreview(ToolPageContext page) {
        return videoMetaData.getPreviewStorageItem();
    }

    @Override
    public String getTimedContentPreviewFrameUrl(ToolPageContext page) {

        // The Plyr plugin is broken for Vimeo so we just load the stream URL in an iframe.
        if (isVimeo()) {
            VideoProvider videoProvider = videoMetaData.getVideoProvider();
            String vimeoStreamUrl = videoProvider != null ? videoProvider.getEmbeddableStreamUrl() : null;
            return vimeoStreamUrl != null ? IframeToolPlayerServlet.getPageUrl(vimeoStreamUrl) : null;

        } else {
            return buildPlyrMediaToolPlayerUrl();
        }
    }

    @Override
    public String getTimedCompanionSelectionFrameUrl(ToolPageContext page) {

        // The Plyr plugin is broken for Vimeo so we don't allow timed selection from the player.
        if (isVimeo()) {
            return null;

        } else {
            return buildPlyrMediaToolPlayerUrl();
        }
    }

    private boolean isVimeo() {

        VideoProvider videoProvider = videoMetaData.getVideoProvider();

        if (videoProvider != null) {
            String oembedProvider = videoProvider.getOembedProviderName();

            if (VIMEO_OEMBED_PROVIDER_NAME.equalsIgnoreCase(oembedProvider)) {
                return true;

            }
        }

        return false;
    }

    private String buildPlyrMediaToolPlayerUrl() {

        String plyrMediaType = getPlyrMediaType();
        String plyrMediaRef = getPlyrMediaRef();

        if (plyrMediaType != null && plyrMediaRef != null) {
            return PlyrMediaToolPlayerServlet.getPageUrl(plyrMediaType, plyrMediaRef);

        } else {
            return null;
        }
    }

    private String getPlyrMediaType() {
        VideoProvider videoProvider = videoMetaData.getVideoProvider();

        if (videoProvider != null) {

            String oembedProvider = videoProvider.getOembedProviderName();

            if (YOU_TUBE_OEMBED_PROVIDER_NAME.equalsIgnoreCase(oembedProvider)) {
                return PlyrMediaToolPlayerServlet.YOUTUBE_MEDIA_TYPE;

            } else if (VIMEO_OEMBED_PROVIDER_NAME.equalsIgnoreCase(oembedProvider)) {
                return PlyrMediaToolPlayerServlet.VIMEO_MEDIA_TYPE;

            } else {
                return videoProvider.getEmbeddableStreamMimeType();
            }
        }

        return null;
    }

    private String getPlyrMediaRef() {

        VideoProvider videoProvider = videoMetaData.getVideoProvider();

        if (videoProvider != null) {
            String oembedProvider = videoProvider.getOembedProviderName();

            if (YOU_TUBE_OEMBED_PROVIDER_NAME.equalsIgnoreCase(oembedProvider)
                || VIMEO_OEMBED_PROVIDER_NAME.equalsIgnoreCase(oembedProvider)) {
                return ObjectUtils.firstNonNull(videoProvider.getVideoId(), videoProvider.getEmbeddableStreamUrl());

            } else {
                return videoProvider.getEmbeddableStreamUrl();
            }
        }

        return null;
    }
}
