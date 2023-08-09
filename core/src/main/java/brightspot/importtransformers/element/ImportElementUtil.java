package brightspot.importtransformers.element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import brightspot.google.youtube.YouTubeUrlVideo;
import brightspot.image.WebImage;
import brightspot.image.WebImagePlacementData;
import brightspot.importapi.ImportObjectModification;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.element.ImportElement;
import brightspot.importtransformers.ImportTransformerUtil;
import brightspot.module.iframe.IframeEmbed;
import brightspot.rte.external.facebook.FacebookUrlExternalContentMapping;
import brightspot.rte.external.instagram.InstagramUrlExternalContentMapping;
import brightspot.rte.external.twitter.TweetUrl;
import brightspot.rte.iframe.IframeEmbedRichTextElement;
import brightspot.rte.image.ImageRichTextElement;
import brightspot.rte.video.VideoRichTextElement;
import brightspot.video.Video;
import brightspot.vimeo.VimeoUrlVideo;
import com.psddev.cms.db.Site;
import com.psddev.cms.rte.ExternalContentRichTextElement;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ImportElementUtil {

    private ImportElementUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportElementUtil.class);

    private static final String DATA_ATTR = "data-state";
    protected static final String EXTERNAL_CONTENT_RTE_TAG = "brightspot-cms-external-content";

    private static final Pattern VIMEO_URL_PATTERN = Pattern.compile(
        "^(https://)(?:www[.])?(player\\.)?(vimeo.com\\/)(video\\/)?(\\d+)(\\/)?(\\?.*)?$");
    private static final Pattern YOUTUBE_URL_PATTERN = Pattern.compile(
        "^(?:https?://)?(?:www[.])?(?:youtube[.]com/watch[?]|youtu[.]be/|youtube.com/embed/)([^/?#]+)");

    private static final List<String> YOU_TUBE_WATCH_HOSTS = Arrays.asList(
        "www.youtube.com",
        "youtube.com");

    private static final List<String> YOU_TUBE_SHORT_URL_HOSTS = Arrays.asList(
        "www.youtu.be",
        "youtu.be");

    private static final String YOU_TUBE_WATCH_VIDEO_ID_PARAMETER = "v";

    public static String createBody(List<ImportElement> elements, ImportTransformer<?> mainTransformer) {
        return Optional.ofNullable(elements)
            .map(b -> ImportElement.transform(b, mainTransformer))
            .orElse(StringUtils.EMPTY);
    }

    public static String processRichText(String richText, ImportTransformer<?> mainTransformer) {
        return Optional.ofNullable(richText)
            .map(text -> new ImportRichTextImporter(mainTransformer, null)
                .convert(text, mainTransformer.getFileBaseUrl()))
            .orElse(null);
    }

    public static String processInlineRichText(String richText, ImportTransformer<?> mainTransformer) {
        return Optional.ofNullable(richText)
            .map(text -> new ImportRichTextImporter(mainTransformer, null)
                .createBspBlockEmbeds(false)
                .convert(text, mainTransformer.getFileBaseUrl()))
            .orElse(null);
    }

    static String parseNumericString(String i) {
        if (StringUtils.isNotBlank(i)) {
            return null;
        }
        return Optional.ofNullable(i)
            .map(value -> value.replaceAll("\\D", ""))
            .filter(StringUtils::isNotBlank)
            .orElse(null);
    }

    static Integer parseInteger(String i) {
        if (StringUtils.isNotBlank(i)) {
            return null;
        }
        return Optional.ofNullable(parseNumericString(i))
            .filter(StringUtils::isNotBlank)
            .map(Integer::valueOf)
            .orElse(null);
    }

    // ------ WEB IMAGE SUPPORT ------ //

    protected static WebImage createImage(
        String imageSrc,
        Element imgElement,
        ImportTransformer<?> transformer,
        Site site) {
        if (StringUtils.isBlank(imageSrc)) {
            throw new IllegalArgumentException("Image url cannot be blank");
        }

        return ImportTransformer.findByExternalId(WebImage.class, imageSrc, url -> {

            String imageUrl = ImportTransformerUtil.prependFileBaseUrlIfNeeded(url, transformer.getFileBaseUrl());

            try {
                WebImage image = Query.from(WebImage.class)
                    .where(ImportObjectModification.SOURCE_URL_FIELD_NAME + " = " + imageUrl)
                    .and("cms.site.owner = ?", site)
                    .first();
                if (image != null) {
                    return image;
                }

                image = new WebImage();

                StorageItem file = Optional.ofNullable(transformer.createStorageItemFromUrl(imageUrl))
                    .map(f -> ImportTransformerUtil.validateImageFile(f, imageUrl))
                    .orElse(null);
                if (file == null) {
                    return null;
                }

                image.setFile(file);

                Optional.of(imgElement.attr("alt"))
                    .filter(StringUtils::isNotBlank)
                    .ifPresent(image::setAltTextOverride);

                Optional.of(imgElement.attr("height"))
                    .map(ImportElementUtil::parseInteger)
                    .ifPresent(image::setHeight);

                Optional.of(imgElement.attr("width"))
                    .map(ImportElementUtil::parseInteger)
                    .ifPresent(image::setWidth);

                Optional.of(imgElement.attr("caption"))
                    .filter(StringUtils::isNotBlank)
                    .ifPresent(image::setCaptionOverride);

                Optional.of(imgElement.attr("credit"))
                    .filter(StringUtils::isNotBlank)
                    .ifPresent(image::setCreditOverride);

                image.as(ImportObjectModification.class).setSourceUrl(imageUrl);
                return image;

            } catch (IOException ex) {
                LOGGER.error("Failed to fetch image {} due to {}", imageUrl, ex.getMessage());
                return null;
            }
        });
    }

    protected static Element createImgElement(WebImage image, String alt, String caption, String credit) {
        if (image == null) {
            return null;
        }

        ImageRichTextElement imageRTE = new ImageRichTextElement();
        imageRTE.setImage(image);

        Optional.ofNullable(alt)
            .filter(StringUtils::isNotBlank)
            .ifPresent(value -> imageRTE.as(WebImagePlacementData.class).setAltTextOverride(value));

        Optional.ofNullable(caption)
            .filter(StringUtils::isNotBlank)
            .ifPresent(value -> imageRTE.as(WebImagePlacementData.class).setCaptionOverride(value));

        Optional.ofNullable(credit)
            .filter(StringUtils::isNotBlank)
            .ifPresent(value -> imageRTE.as(WebImagePlacementData.class).setCreditOverride(value));

        Element bspImage = new Element(Tag.valueOf(ImageRichTextElement.TAG_NAME), "");
        bspImage.attr(DATA_ATTR, imageRTE.toAttributes().get(DATA_ATTR));
        bspImage.text(imageRTE.toBody());

        return bspImage;
    }

    // ------ IFRAME SUPPORT ------ //

    protected static IframeEmbed createIframe(String iframeSrc, Site site, String name, String height, String width) {

        if (StringUtils.isBlank(iframeSrc)) {
            return null;
        }

        return ImportTransformer.findByExternalId(IframeEmbed.class, iframeSrc, src -> {

            IframeEmbed iframeEmbed = Query.from(IframeEmbed.class)
                .where(ImportObjectModification.SOURCE_URL_FIELD_NAME + " = " + iframeSrc)
                .and("cms.site.owner = ?", site)
                .first();
            if (iframeEmbed != null) {
                return iframeEmbed;
            }

            iframeEmbed = new IframeEmbed();

            iframeEmbed.setUrl(iframeSrc);

            Optional.ofNullable(name)
                .filter(StringUtils::isNotBlank)
                .ifPresent(iframeEmbed::setIframeName);

            Optional.ofNullable(height)
                .map(ImportElementUtil::parseInteger)
                .ifPresent(iframeEmbed::setIframeHeight);

            Optional.ofNullable(width)
                .map(ImportElementUtil::parseNumericString)
                .ifPresent(iframeEmbed::setIframeWidth);

            iframeEmbed.setInternalName(ObjectUtils.firstNonBlank(iframeEmbed.getIframeName(), iframeSrc));

            iframeEmbed.as(ImportObjectModification.class).setSourceUrl(iframeSrc);

            return iframeEmbed;
        });
    }

    protected static Element createIframeElement(IframeEmbed iframeEmbed) {
        if (iframeEmbed == null) {
            return null;
        }

        IframeEmbedRichTextElement iframeRte = new IframeEmbedRichTextElement();
        iframeRte.setShared(iframeEmbed);

        Element bspElement = new Element(Tag.valueOf(IframeEmbedRichTextElement.TAG_NAME), "");
        bspElement.attr(DATA_ATTR, iframeRte.toAttributes().get(DATA_ATTR));
        bspElement.text(iframeRte.toBody());

        return bspElement;
    }

    // ------ EXTERNAL CONTENT SUPPORT ------ //

    protected static boolean isSupportedExternalContent(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        return FacebookUrlExternalContentMapping.URL_PATTERN.matcher(url).matches()
            || InstagramUrlExternalContentMapping.URL_PATTERN.matcher(url).matches()
            || TweetUrl.URL_PATTERN.matcher(url).matches();
    }

    protected static Element createExternalContentElement(String url) {

        ExternalContentRichTextElement externalContentRte = new ExternalContentRichTextElement();
        externalContentRte.setUrl(url);

        Element bspElement = new Element(Tag.valueOf(EXTERNAL_CONTENT_RTE_TAG), "");
        bspElement.attr(DATA_ATTR, externalContentRte.toAttributes().get(DATA_ATTR));
        bspElement.text(externalContentRte.toBody());

        return bspElement;
    }

    // ------ VIDEO SUPPORT ------ //

    protected static boolean isSupportedVideoPlayer(String src) {
        return VIMEO_URL_PATTERN.matcher(src).matches()
            || YOUTUBE_URL_PATTERN.matcher(src).matches();
    }

    protected static Video retrieveVideo(String externalId, Site site) {
        if (StringUtils.isBlank(externalId)) {
            return null;
        }
        return Query.from(Video.class).where(ImportObjectModification.EXTERNAL_ID_FIELD_NAME + " = ?", externalId)
            .and("cms.site.owner = ?", site)
            .first();
    }

    protected static Video createVideo(String videoUrl, Site site) {

        if (VIMEO_URL_PATTERN.matcher(videoUrl).matches()) {
            return createVimeoUrlVideo(videoUrl, site);

        } else if (YOUTUBE_URL_PATTERN.matcher(videoUrl).matches()) {
            return createYouTubeVideo(videoUrl, site);

        }
        return null;
    }

    protected static Element createVideoElement(Video video) {
        if (video == null) {
            return null;
        }

        VideoRichTextElement videoRte = new VideoRichTextElement();
        videoRte.setVideo(video);

        Element bspVideo = new Element(Tag.valueOf(VideoRichTextElement.TAG_NAME), "");
        bspVideo.attr(DATA_ATTR, videoRte.toAttributes().get(DATA_ATTR));
        bspVideo.text(videoRte.toBody());

        return bspVideo;
    }

    // ------ VIMEO URL VIDEO SUPPORT ------ //

    private static VimeoUrlVideo createVimeoUrlVideo(String vimeoUrl, Site site) {
        if (StringUtils.isBlank(vimeoUrl)) {
            return null;
        }

        String vimeoId = getVimeoVideoId(vimeoUrl);
        if (StringUtils.isBlank(vimeoId)) {
            return null;
        }

        return ImportTransformer.findByExternalId(VimeoUrlVideo.class, vimeoId, src -> {

            VimeoUrlVideo vimeoUrlVideo = Query.from(VimeoUrlVideo.class)
                .where(ImportObjectModification.SOURCE_URL_FIELD_NAME + " = " + vimeoId)
                .and("cms.site.owner = ?", site)
                .first();
            if (vimeoUrlVideo != null) {
                return vimeoUrlVideo;
            }

            vimeoUrlVideo = new VimeoUrlVideo();
            vimeoUrlVideo.setVimeoIdentifier(vimeoId);

            vimeoUrlVideo.asVideoData().setInternalName(vimeoId);

            vimeoUrlVideo.as(ImportObjectModification.class).setSourceUrl(vimeoId);

            return vimeoUrlVideo;
        });
    }

    private static String getVimeoVideoId(String videoUrl) {
        String videoId = null;

        if (videoUrl != null) {
            try {

                String path = StringUtils.removeEnd(new URL(videoUrl).getPath(), "/");

                int lastSlashAt = path.lastIndexOf('/');
                if (lastSlashAt >= 0) {
                    videoId = path.substring(lastSlashAt + 1);
                }

            } catch (MalformedURLException e) {
                // ignore and assume it's already an ID.
            }
        }
        return videoId;
    }

    // ------ YOUTUBE URL VIDEO SUPPORT ------ //

    private static YouTubeUrlVideo createYouTubeVideo(String youTubeUrl, Site site) {
        if (StringUtils.isBlank(youTubeUrl)) {
            return null;
        }

        String youTubeId = getYouTubeVideoId(youTubeUrl);
        if (StringUtils.isBlank(youTubeId)) {
            return null;
        }

        return ImportTransformer.findByExternalId(YouTubeUrlVideo.class, youTubeId, src -> {

            YouTubeUrlVideo youTubeVideo = Query.from(YouTubeUrlVideo.class)
                .where(ImportObjectModification.SOURCE_URL_FIELD_NAME + " = " + youTubeId)
                .and("cms.site.owner = ?", site)
                .first();
            if (youTubeVideo != null) {
                return youTubeVideo;
            }

            youTubeVideo = new YouTubeUrlVideo();
            youTubeVideo.setYoutubeIdentifier(youTubeId);

            youTubeVideo.as(ImportObjectModification.class).setSourceUrl(youTubeId);

            return youTubeVideo;
        });
    }

    private static String getYouTubeVideoId(String videoUrl) {
        String videoId = null;

        if (videoUrl != null) {
            URI url = null;
            try {
                url = URI.create(videoUrl);
            } catch (RuntimeException e) {
                // ignore
            }

            if (url != null) {
                String host = url.getHost();

                if (YOU_TUBE_WATCH_HOSTS.contains(host) && (url.getQuery() != null)) {
                    List<NameValuePair> params = URLEncodedUtils.parse(url, StandardCharsets.UTF_8);
                    videoId = params.stream()
                        .filter(p -> p.getName().equals(YOU_TUBE_WATCH_VIDEO_ID_PARAMETER))
                        .map(NameValuePair::getValue)
                        .findFirst()
                        .orElse(null);

                } else if (YOU_TUBE_SHORT_URL_HOSTS.contains(host) && (url.getPath() != null)) {
                    String[] segments = url.getPath().split("/");

                    if (segments.length >= 2) {
                        videoId = segments[1];

                        if (StringUtils.isBlank(videoId)) {
                            videoId = null;
                        }
                    }
                }
            }
        }
        return videoId;
    }
}
