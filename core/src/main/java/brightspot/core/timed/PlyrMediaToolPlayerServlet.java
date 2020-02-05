package brightspot.core.timed;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.ServletException;

import com.psddev.cms.tool.PageServlet;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.util.RoutingFilter;
import com.psddev.dari.util.StringUtils;

/**
 * The Plyr media player from plyr.io. https://github.com/sampotts/plyr
 */

@RoutingFilter.Path(value = PlyrMediaToolPlayerServlet.PAGE_URL)
public class PlyrMediaToolPlayerServlet extends PageServlet {

    public static final String YOUTUBE_MEDIA_TYPE = "youtube";
    public static final String VIMEO_MEDIA_TYPE = "vimeo";
    public static final String HLS_STREAM_TYPE = "application/x-mpegurl";

    static final String PAGE_URL = "/_express/timed/plyr-media-tool-player";

    private static final String[] HLS_STREAM_TYPES = { HLS_STREAM_TYPE, "application/vnd.apple.mpegurl" };
    private static final String[] HLS_STREAM_EXTENSIONS = { "m3u8" };

    private static final String PLYR_MEDIA_TYPE_PARAMETER = "plrMediaType";
    private static final String PLYR_MEDIA_REF_PARAMETER = "plrMediaRef";

    /**
     * Get the URL to access this servlet with the appropriate parameters.
     *
     * @param plyrMediaType Either a mime type like {@code video/mp4} or if it's a known media provider like YouTube or
     * Vimeo, then just the provider type {@link #YOUTUBE_MEDIA_TYPE} or {@link #VIMEO_MEDIA_TYPE}. For HLS video
     * streams, the type should be {@link #HLS_STREAM_TYPE}.
     * @param plyrMediaRef Either a URL to a web-accessible media file or the ID (or URL) of the media from a known
     * provider (YouTube / Vimeo).
     * @return The url to access the Plyr media player.
     */
    public static String getPageUrl(String plyrMediaType, String plyrMediaRef) {
        Objects.requireNonNull(plyrMediaType);
        Objects.requireNonNull(plyrMediaRef);
        return StringUtils.addQueryParameters(PAGE_URL,
            PLYR_MEDIA_TYPE_PARAMETER, plyrMediaType,
            PLYR_MEDIA_REF_PARAMETER, plyrMediaRef);
    }

    @Override
    protected String getPermissionId() {
        return null;
    }

    @Override
    protected void doService(final ToolPageContext page) throws IOException, ServletException {

        // the vide type, i.e. youtube, vimeo, video/mp4, application/x-mpegurl (hls)
        String plyrMediaType = page.param(String.class, PLYR_MEDIA_TYPE_PARAMETER);

        // the video identifier, i.e. Xw1PXRNGcG4 (YouTube), http://www.example.com/videos/path/to/videoHls.m3u8
        String plyrMediaRef = page.param(String.class, PLYR_MEDIA_REF_PARAMETER);

        // error checking
        Map<String, String> requiredParams = new LinkedHashMap<>();
        requiredParams.put(PLYR_MEDIA_TYPE_PARAMETER, plyrMediaType);
        requiredParams.put(PLYR_MEDIA_REF_PARAMETER, plyrMediaRef);

        if (requiredParams.values().stream().anyMatch(Objects::isNull)) {
            String missingRequiredParams = requiredParams.entrySet().stream()
                .filter(entry -> entry.getValue() == null)
                .map(Map.Entry::getKey)
                .map(key -> "[" + key + "]")
                .collect(Collectors.joining(", "));

            throw new IllegalArgumentException("Missing required parameters " + missingRequiredParams + "!");
        }

        writePlayerHtml(page, plyrMediaType, plyrMediaRef);
    }

    private void writePlayerHtml(ToolPageContext page, String plyrMediaType, String plyrMediaRef) throws IOException {

        // true if it has a mimeType and isn't just a media provider like youtube or vimeo
        boolean isUrlMedia =
            isMediaTypeAudioMimeType(plyrMediaType) || isMediaTypeVideoMimeType(plyrMediaType) || isMediaTypeHlsStream(
                plyrMediaRef,
                plyrMediaType);

        // true if it's an HLS stream (assumes it's also video for now)
        boolean isHlsStream = isMediaTypeHlsStream(plyrMediaRef, plyrMediaType);

        // true if it's video from a URL, and not video from youtube/vimeo
        boolean isUrlVideo =
            isMediaTypeVideoMimeType(plyrMediaType) || isMediaTypeHlsStream(plyrMediaRef, plyrMediaType);

        // "div" for youtube/vimeo IDs, "video" for video / hls, "audio" for audio
        String mediaElement = !isUrlMedia ? "div" : isUrlVideo ? "video" : "audio";

        String id = page.createId();

        page.writeStart("div", "id", id, "class", "plyr-container");
        {
            // https://github.com/selz/plyr#quick-setup
            page.writeStart(
                mediaElement,
                "class",
                "plyr-player",
                "data-hls-url",
                isHlsStream ? plyrMediaRef : null,
                "controls",
                isUrlMedia ? "" : null,
                "poster",
                isUrlVideo ? "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7" : null,
                "muted",
                isUrlMedia ? "" : null,
                "data-type",
                !isUrlMedia ? plyrMediaType : null,
                "data-video-id",
                !isUrlMedia ? plyrMediaRef : null);
            {
                if (isUrlMedia && !isHlsStream) {
                    page.writeTag("source", "src", plyrMediaRef, "type", plyrMediaType);
                }
            }
            page.writeEnd();
        }
        page.writeEnd();
    }

    private boolean isMediaTypeVideoMimeType(String plyrMediaType) {
        return plyrMediaType.startsWith("video/");
    }

    private boolean isMediaTypeAudioMimeType(String plyrMediaType) {
        return plyrMediaType.startsWith("audio/");
    }

    private boolean isMediaTypeHlsStream(String plyrMediaRef, String plyrMediaType) {

        // first check the mime type
        if (plyrMediaType != null && Arrays.asList(HLS_STREAM_TYPES).contains(plyrMediaType.toLowerCase())) {
            return true;

        } else {
            // next try to check the file extension

            // this is a naive implementation, since it doesn't handle
            // query parameters and such but implementers should really
            // utilize the mime type to declare the type of video.

            int lastDotAt = plyrMediaRef.lastIndexOf('.');

            if (lastDotAt > 0) {
                String extension = plyrMediaRef.substring(lastDotAt).toLowerCase();

                if (Arrays.asList(HLS_STREAM_EXTENSIONS).contains(extension)) {
                    return true;
                }
            }

            return false;
        }
    }
}
