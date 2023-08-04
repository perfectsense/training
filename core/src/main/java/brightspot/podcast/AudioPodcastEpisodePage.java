package brightspot.podcast;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import brightspot.audio.Audio;
import brightspot.audio.file.AudioFile;
import brightspot.audio.file.AudioStorageItemWrapper;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@ToolUi.IconName("settings_voice")
@Recordable.DisplayName("Podcast Episode")
@ToolUi.FieldDisplayOrder({
    "title",
    "description",
    "hasUrlSlug.urlSlug",
    "coverImageOverride",
    "body",
    "primaryAudio",
    "hasPodcastWithField.podcast",
    "episodeNumber",
    "hasSecondarySectionsWithField.secondarySections",
    "hasTags.tags",
    "embargoable.embargo",
    "landingCascading.content",
    "seo.title",
    "seo.suppressSeoDisplayName",
    "seo.description",
    "seo.keywords",
    "seo.robots",
    "ampPage.ampDisabled"
})
@ToolUi.FieldDisplayPreview({
    "title",
    "description",
    "hasSectionWithField.section",
    "hasTags.tags",
    "cms.content.updateDate",
    "cms.content.updateUser" })
public class AudioPodcastEpisodePage extends AbstractPodcastEpisodePage {

    private Audio primaryAudio;

    public Audio getPrimaryAudio() {
        return primaryAudio;
    }

    public void setPrimaryAudio(Audio primaryAudio) {
        this.primaryAudio = primaryAudio;
    }

    @Override
    public Object getMedia() {
        return getPrimaryAudio();
    }

    @Override
    public String getRssFeedItemEnclosureUrlFallback() {
        //TODO this only supports AudioFile
        return Optional.ofNullable(getPrimaryAudio())
            .filter(AudioFile.class::isInstance)
            .map(AudioFile.class::cast)
            .map(AudioFile::getItems)
            .flatMap(sources -> sources.stream()
                .map(AudioStorageItemWrapper::getFile)
                .filter(Objects::nonNull)
                .findFirst())
            .map(StorageItem::getSecurePublicUrl)
            .orElse(null);
    }

    @Override
    public Long getRssFeedItemEnclosureLengthFallback() {
        //TODO this only supports AudioFile
        return Optional.ofNullable(getPrimaryAudio())
            .filter(AudioFile.class::isInstance)
            .map(AudioFile.class::cast)
            .map(AudioFile::getItems)
            .flatMap(sources -> sources.stream().filter(wrapper -> wrapper.getFile() != null).findFirst())
            .map(AudioStorageItemWrapper::getLength)
            .orElse(null);
    }

    @Override
    public String getRssFeedItemEnclosureTypeFallback() {
        //TODO this only supports AudioFile
        return Optional.ofNullable(getPrimaryAudio())
            .filter(AudioFile.class::isInstance)
            .map(AudioFile.class::cast)
            .map(AudioFile::getItems)
            .flatMap(sources -> sources.stream().filter(wrapper -> wrapper.getFile() != null).findFirst())
            .map(AudioStorageItemWrapper::getMimeType)
            .orElse(null);
    }

    @Override
    public Long getAppleRssFeedItemDurationFallback() {
        //TODO this only supports AudioFile
        return Optional.ofNullable(getPrimaryAudio())
            .filter(AudioFile.class::isInstance)
            .map(AudioFile.class::cast)
            .map(AudioFile::getItems)
            .flatMap(sources -> sources.stream().filter(wrapper -> wrapper.getFile() != null).findFirst())
            .map(AudioStorageItemWrapper::getDuration)
            .map(Duration::getSeconds)
            .orElse(null);
    }
}
