package brightspot.core.video;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;

@Recordable.DisplayName("HTML5")
public class Html5VideoProvider extends VideoProvider {

    @Required
    @ToolUi.Unlabeled
    private List<VideoStorageItemWrapper> items;

    public List<VideoStorageItemWrapper> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<VideoStorageItemWrapper> items) {
        this.items = items;
    }

    @Override
    public String getVideoId() {
        VideoStorageItemWrapper wrapper = getPrimaryStorageItemWrapper();
        StorageItem file = wrapper != null ? wrapper.getFile() : null;
        return file != null ? file.getStorage() + ":" + file.getPath() : null;
    }

    @Override
    protected String getVideoTitleFallback() {
        VideoStorageItemWrapper wrapper = getPrimaryStorageItemWrapper();
        StorageItem file = wrapper != null ? wrapper.getFile() : null;
        Map<String, Object> metadata = file != null ? file.getMetadata() : null;
        return metadata != null ? ObjectUtils.to(String.class, metadata.get("originalFilename")) : null;
    }

    @Override
    public Long getVideoDuration() {
        VideoStorageItemWrapper wrapper = getPrimaryStorageItemWrapper();
        Long durationSeconds = wrapper != null ? wrapper.getDuration() : null;
        return durationSeconds != null ? durationSeconds * 1_000 : null;
    }

    @Override
    public Integer getOriginalVideoWidth() {
        VideoStorageItemWrapper wrapper = getPrimaryStorageItemWrapper();
        return wrapper != null ? wrapper.getWidth() : null;
    }

    @Override
    public Integer getOriginalVideoHeight() {
        VideoStorageItemWrapper wrapper = getPrimaryStorageItemWrapper();
        return wrapper != null ? wrapper.getHeight() : null;
    }

    @Override
    protected String getOembedProviderName() {
        return null;
    }

    @Override
    protected String getEmbeddableStreamUrl() {
        VideoStorageItemWrapper wrapper = getPrimaryStorageItemWrapper();
        StorageItem file = wrapper != null ? wrapper.getFile() : null;
        return file != null ? file.getPublicUrl() : null;
    }

    @Override
    protected String getEmbeddableStreamMimeType() {
        VideoStorageItemWrapper wrapper = getPrimaryStorageItemWrapper();
        return wrapper != null ? wrapper.getMimeType() : null;
    }

    private VideoStorageItemWrapper getPrimaryStorageItemWrapper() {
        List<VideoStorageItemWrapper> items = getItems();
        if (!items.isEmpty()) {
            return items.get(0);
        }
        return null;
    }

    @Override
    protected List<StorageItem> getDownloadableFiles() {
        return getItems().stream().map(VideoStorageItemWrapper::getFile).collect(Collectors.toList());
    }
}
