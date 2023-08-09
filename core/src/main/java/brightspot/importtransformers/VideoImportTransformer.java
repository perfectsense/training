package brightspot.importtransformers;

import java.util.Set;

import brightspot.brightcove.BrightcoveIdVideo;
import brightspot.google.youtube.YouTubeUrlVideo;
import brightspot.importapi.ImportApiException;
import brightspot.importapi.ImportObjectModification;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.ImportingDatabase;
import brightspot.jwplayer.JwPlayerIdVideo;
import brightspot.jwplayer.db.JwPlayerSettings;
import brightspot.jwplayer.db.JwPlayerSiteSettings;
import brightspot.video.Video;
import brightspot.video.VideoData;
import brightspot.video.file.VideoFile;
import brightspot.video.file.VideoStorageItemWrapper;
import brightspot.vimeo.VimeoUrlVideo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.dari.db.CompoundPredicate;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import org.apache.commons.lang3.StringUtils;

public class VideoImportTransformer extends ImportTransformer<Video> {

    private static final String PLAYER_TYPE_FIELD = "playerType";
    private static final String SOURCE_ID_FIELD = "sourceId";
    private static final String SITE_ID_FIELD = "siteId";
    private static final String TITLE_FIELD = "title";

    @JsonProperty(PLAYER_TYPE_FIELD)
    private String playerType;

    @JsonProperty(SOURCE_ID_FIELD)
    private String sourceId;

    @JsonProperty(SITE_ID_FIELD)
    private String settingsSiteId;

    @JsonProperty(TITLE_FIELD)
    private String title;

    @Override
    public Video transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNoneBlank(this.playerType, this.getSourceId()),
            "playerType or sourceId not provided for Video with externalId [" + this.getExternalId() + "]");

        PlayerType playerType = getPlayerType();
        if (playerType == null) {
            throw new ImportApiException(
                "Invalid playerType [" + this.playerType + "] provided for Video with externalId ["
                    + this.getExternalId() + "]");
        }

        Video video = null;

        switch (playerType) {
            case BRIGHTCOVE:
                BrightcoveIdVideo brightcoveIdVideo = new BrightcoveIdVideo();
                brightcoveIdVideo.setVideoId(this.getSourceId());
                video = brightcoveIdVideo;
                break;

            case FILE:
                video = createVideoFile();
                break;

            case JW:
                video = createJwPlayerIdVideo();
                break;

            case VIMEO:
                VimeoUrlVideo vimeoUrlVideo = new VimeoUrlVideo();
                vimeoUrlVideo.setVimeoIdentifier(this.getSourceId());
                video = vimeoUrlVideo;
                break;

            case YOUTUBE:
                YouTubeUrlVideo youTubeUrlVideo = new YouTubeUrlVideo();
                youTubeUrlVideo.setYoutubeIdentifier(this.getSourceId());
                video = youTubeUrlVideo;
                break;

            default:
                throw new ImportApiException(
                    "No handling available for playerType [" + playerType + "] provided for Video with externalId ["
                        + this.getExternalId() + "]");
        }

        video.as(ImportObjectModification.class).setSourceUrl(this.getSourceId());

        String internalName = ObjectUtils.firstNonBlank(this.getTitle(), this.getSourceId());
        video.as(VideoData.class).setInternalName(internalName);

        return video;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        if (StringUtils.isBlank(this.getSourceId())) {
            return null;
        }
        Predicate sourcePredicate = PredicateParser.Static.parse(
            ImportObjectModification.SOURCE_URL_FIELD_NAME + " = \"" + this.getSourceId() + "\"");

        PlayerType playerType = null;
        try {
            playerType = this.getPlayerType();
        } catch (Exception ex) {
            return sourcePredicate;
        }
        if (playerType == null) {
            return sourcePredicate;
        }

        Predicate typePredicate;

        switch (playerType) {
            case BRIGHTCOVE:
                typePredicate = PredicateParser.Static.parse(
                    "brightspot.brightcove.BrightcoveIdVideo/videoId = \"" + this.getSourceId() + "\"");
                break;
            case JW:
                typePredicate = PredicateParser.Static.parse(
                    "brightspot.jwplayer.JwPlayerIdVideo/mediaId = \"" + this.getSourceId() + "\"");
                break;
            case VIMEO:
                typePredicate = PredicateParser.Static.parse(
                    "brightspot.vimeo.VimeoUrlVideo/getVimeoId = \"" + this.getSourceId() + "\"");
                break;
            case YOUTUBE:
                typePredicate = PredicateParser.Static.parse(
                    "brightspot.google.youtube.YouTubeUrlVideo/getYouTubeId = \"" + this.getSourceId() + "\"");
                break;
            default:
                return sourcePredicate;
        }

        return CompoundPredicate.combine(PredicateParser.OR_OPERATOR, sourcePredicate, typePredicate);
    }

    @Override
    public String getExternalId() {
        return ObjectUtils.firstNonBlank(this.externalId, this.getIdAsString(), this.getSourceId());
    }

    public String getPlayerTypeString() {
        return playerType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getSettingsSiteId() {
        return settingsSiteId;
    }

    @Override
    public String getIdPrefix() {
        if (StringUtils.isBlank(this.idPrefix) && StringUtils.isNotBlank(this.playerType)) {
            return this.getTargetClass().getName() + "/" + this.playerType;
        }
        return super.getIdPrefix();
    }

    private VideoFile createVideoFile() {
        VideoFile videoFile = new VideoFile();

        String fileUrl = ImportTransformerUtil.prependFileBaseUrlIfNeeded(this.getSourceId(), this.getFileBaseUrl());

        StorageItem file = null;
        try {
            file = createStorageItemFromUrl(fileUrl);
        } catch (Exception ex) {
            throw new ImportApiException(
                "Exception creating VideoFile using URL [" + fileUrl + "] for Video with externalId ["
                    + this.getExternalId() + "]");
        }

        VideoStorageItemWrapper itemWrapper = new VideoStorageItemWrapper();
        itemWrapper.setFile(file);

        videoFile.getItems().add(itemWrapper);

        return videoFile;
    }

    private JwPlayerIdVideo createJwPlayerIdVideo() {

        Site site = ((ImportingDatabase) Database.Static.getDefault()).getSite();

        Set<JwPlayerSettings> settingsList = SiteSettings.get(site, siteSettings ->
            siteSettings.as(JwPlayerSiteSettings.class).getJwPlayerAccounts());
        if (ObjectUtils.isBlank(settingsList)) {
            throw new ImportApiException("No JW Player Settings Published for Site [" + site.getLabel() + "]");
        }

        String jsSettingsSiteId = this.getSettingsSiteId();

        JwPlayerSettings playerSettings = null;

        for (JwPlayerSettings settings : settingsList) {
            if (StringUtils.isBlank(jsSettingsSiteId) || settings.getSiteId().equals(jsSettingsSiteId)) {
                playerSettings = settings;
                break;
            }
        }
        if (playerSettings == null) {
            throw new ImportApiException(
                "No JW Player Settings matching provided Site ID [" + jsSettingsSiteId + "] for Site ["
                    + site.getLabel() + "]");
        }

        JwPlayerIdVideo jwPlayerIdVideo = new JwPlayerIdVideo();
        jwPlayerIdVideo.setMediaId(this.getSourceId());
        jwPlayerIdVideo.setJwPlayerSettings(playerSettings);

        jwPlayerIdVideo.setJwPlayerVideo(JwPlayerIdVideo.createVideo(this.getSourceId(), jsSettingsSiteId));

        return jwPlayerIdVideo;
    }

    private PlayerType getPlayerType() {
        if (StringUtils.isBlank(this.playerType)) {
            return null;
        }
        try {
            return PlayerType.valueOf(this.playerType.toUpperCase());
        } catch (Exception ex) {
            throw new ImportApiException(
                "Invalid playerType [" + this.playerType.toUpperCase() + "] provided for Video with externalId ["
                    + this.getExternalId() + "]");
        }
    }

    private enum PlayerType {
        BRIGHTCOVE,
        FILE,
        JW,
        YOUTUBE,
        VIMEO
    }
}
