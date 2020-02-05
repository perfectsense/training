package brightspot.core.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;

/**
 * Reference implementation of the {@link AudioProvider}. Generally avoid using this class, as it doesn't stream audio
 * and instead downloads them into the clients browser. For larger files, this can become an issue.
 */
@Recordable.DisplayName("HTML5")
public class Html5AudioProvider extends AudioProvider {

    @Required
    @ToolUi.Unlabeled
    @ToolUi.Note("Optionally upload audio file in multiple formats")
    private List<AudioStorageItemWrapper> items;

    @Override
    public List<AudioStorageItemWrapper> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<AudioStorageItemWrapper> items) {
        this.items = items;
    }

    @Override
    protected String getAudioId() {
        return Optional.ofNullable(getPrimaryStorageItemWrapper())
            .map(AudioStorageItemWrapper::getFile)
            .map(file -> file.getStorage() + ":" + file.getPath())
            .orElse(null);
    }

    @Override
    protected String getAudioTitleFallback() {
        return Optional.ofNullable(getPrimaryStorageItemWrapper())
            .map(AudioStorageItemWrapper::getFile)
            .map(StorageItem::getMetadata)
            .map(metadata -> ObjectUtils.to(String.class, metadata.get("originalFilename")))
            .orElse(null);
    }

    @Override
    protected Long getAudioDuration() {
        return Optional.ofNullable(getPrimaryStorageItemWrapper())
            .map(AudioStorageItemWrapper::getDuration)
            .map(duration -> duration * 1_000)
            .orElse(null);
    }

    @Override
    protected String getOembedProviderName() {
        return null;
    }

    @Override
    public String getEmbeddableStreamUrl() {
        return Optional.ofNullable(getPrimaryStorageItemWrapper())
            .map(AudioStorageItemWrapper::getFile)
            .map(StorageItem::getPublicUrl)
            .orElse(null);
    }

    @Override
    protected String getEmbeddableStreamMimeType() {
        return Optional.ofNullable(getPrimaryStorageItemWrapper())
            .map(AudioStorageItemWrapper::getMimeType)
            .orElse(null);
    }

    private AudioStorageItemWrapper getPrimaryStorageItemWrapper() {
        List<AudioStorageItemWrapper> items = getItems();
        if (!items.isEmpty()) {
            return items.get(0);
        }
        return null;
    }
}
