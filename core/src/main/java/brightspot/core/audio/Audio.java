package brightspot.core.audio;

import java.time.Duration;

import brightspot.core.creativework.CreativeWork;
import brightspot.core.image.ImageOption;
import brightspot.core.image.OneOffImageOption;
import brightspot.core.timed.DurationUtils;
import brightspot.core.timed.TimedContent;
import brightspot.core.timed.TimedContentMetaData;
import brightspot.core.timed.TimedContentPlayerNoteRenderer;
import brightspot.core.timed.TimedContentToolPlayer;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectIndex;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

/**
 * Provides an Audio implementation of the TimedContent interface, similar to {@link brightspot.core.video.Video}. See
 * the {@link brightspot.media} package for a reference use case.
 */
@Recordable.PreviewField("getPreviewStorageItem")
@ToolUi.FieldDisplayOrder({
    "audioProvider",
    "headline",
    "subHeadline",
    "preview",
    "sectionable.section",
    "packageable.pkg",
    "taggable.tags",
    "taggable.tagging",
    // Main Tab
    "thumbnailOption" }) // Overrides Tab
@ToolUi.IconName("music_note")
@ToolUi.SearchShortcut(shortcut = "hl", field = "getHeadline")
@ToolUi.SearchShortcut(shortcut = "sh", field = "getSubHeadline")
public class Audio extends CreativeWork implements
    TimedContent,
    TimedContentMetaData {

    public static final String AUDIO_PROVIDER_ID_FIELD = "getAudioProviderId";

    @Indexed
    @DisplayName("Provider")
    @ToolUi.Placeholder("Select Provider...")
    private AudioProvider audioProvider;

    @DisplayName("Provider")
    @Indexed
    @Required
    @ToolUi.Hidden
    @ToolUi.Filterable
    @ToolUi.DropDown
    @Where("groups = " + AudioProvider.INTERNAL_NAME
        + " && internalName != " + AudioProvider.INTERNAL_NAME
        + " && (cms.ui.hidden = false || cms.ui.hidden = missing)"
        + " && isAbstract = false")
    private ObjectType audioProviderType;

    @DisplayName("Thumbnail")
    private ImageOption thumbnailOption;

    @ToolUi.Placeholder(dynamicText = "${content.getDurationLabel()}")
    @ToolUi.ReadOnly
    private String duration;

    public AudioProvider getAudioProvider() {
        updateAudioProvider();
        return audioProvider;
    }

    @Indexed
    @ToolUi.Hidden
    public String getAudioProviderId() {
        AudioProvider provider = getAudioProvider();
        return provider != null ? provider.getAudioId() : null;
    }

    @ToolUi.Hidden
    public String getURL() {
        return audioProvider.getEmbeddableStreamUrl();
    }

    @DisplayName("Audio Provider ID")
    @Indexed(unique = true)
    @ToolUi.Hidden
    public String getUniqueIndex() {
        AudioProvider provider = getAudioProvider();
        String providerId = getAudioProviderId();

        if (provider != null && providerId != null) {
            return provider.getState().getType().getId().toString() + "-" + providerId;
        } else {
            return null;
        }
    }

    public void setAudioProvider(AudioProvider audioProvider) {
        this.audioProvider = audioProvider;
    }

    /**
     * Not for public use.
     */
    @Override
    public String getHeadlineFallback() {
        AudioProvider audioProvider = getAudioProvider();
        return audioProvider != null ? audioProvider.getAudioTitleFallback() : null;
    }

    /**
     * Not for public use.
     */
    @Override
    public String getSubHeadlineFallback() {
        AudioProvider audioProvider = getAudioProvider();
        return audioProvider != null ? audioProvider.getAudioDescriptionFallback() : null;
    }

    public ImageOption getThumbnailOption() {
        return thumbnailOption;
    }

    public void setThumbnailOption(ImageOption thumbnailOption) {
        this.thumbnailOption = thumbnailOption;
    }

    public ImageOption getThumbnail() {
        ImageOption thumbnail = thumbnailOption;

        if (thumbnail == null) {
            AudioProvider audioProvider = getAudioProvider();

            if (audioProvider != null) {
                StorageItem providerThumbnailFallback = audioProvider.getAudioThumbnailFallback();

                if (providerThumbnailFallback != null) {
                    OneOffImageOption oneOff = new OneOffImageOption();
                    oneOff.setFile(providerThumbnailFallback);
                    oneOff.setAltText(getHeadline());
                    thumbnail = oneOff;
                }
            }
        }

        return thumbnail;
    }

    public String getDurationLabel() {
        return DurationUtils.durationToLabel(getAudioProviderDuration());
    }

    // Used by @Recordable.PreviewField annotation
    @Ignored(false)
    @ToolUi.Hidden
    public StorageItem getPreviewStorageItem() {
        ImageOption thumbnail = getThumbnail();
        return thumbnail != null ? thumbnail.getImageOptionFile() : null;
    }

    public String getPreviewNoteHtml(ToolPageContext page) {
        return new TimedContentPlayerNoteRenderer()
            .render(this, getState().getField("preview"), page);
    }

    /**
     * Gets the duration (in milliseconds) of the audio.
     *
     * @return Nullable
     */
    @Indexed
    @ToolUi.Hidden
    public Long getDuration() {
        AudioProvider provider = getAudioProvider();
        return provider != null ? provider.getAudioDuration() : null;
    }

    private void updateAudioProvider() {
        if (audioProvider != null) {
            audioProvider.setTimedContentMetadata(this);
            audioProviderType = audioProvider.getState().getType();
        } else {
            audioProviderType = null;
        }
    }

    @Override
    public void beforeSave() {
        super.beforeSave();

        updateAudioProvider();
    }

    @Override
    public boolean shouldValidate(ObjectField field) {
        // skip validation of this field's where clause since it's just for global search purposes.
        return !"audioProviderType".equals(field.getInternalName())
            && super.shouldValidate(field);
    }

    @Override
    protected boolean onDuplicate(ObjectIndex index) {

        if ("getUniqueIndex".equals(index.getName())) {
            getState().addError(
                getState().getField("audioProvider"),
                "Provider ID [" + getAudioProviderId() + "] must be unique!");

            return false;
        }

        return super.onDuplicate(index);
    }

    @Override
    public String getLabel() {
        return getHeadline();
    }

    // TimedContent Support

    @Override
    public Long getTimedContentDuration() {
        Duration duration = getAudioProviderDuration();
        return duration != null ? duration.toMillis() : null;
    }

    @Override
    public TimedContentToolPlayer getTimedContentToolPlayer() {
        return new AudioToolPlayer(this);
    }

    private Duration getAudioProviderDuration() {
        AudioProvider provider = getAudioProvider();

        if (provider != null) {
            Long duration = provider.getAudioDuration();

            if (duration != null) {
                return Duration.ofMillis(duration);
            }
        }

        return null;
    }

    // TimedContentItem Support

    @Override
    public TimedContent getTimedContentItemContent() {
        return this;
    }

    @Override
    public Long getTimedContentItemDuration() {
        return getTimedContentDuration();
    }

}
