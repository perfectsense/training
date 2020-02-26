package brightspot.core.video;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.psddev.cms.tool.BulkUploadProcessor;
import com.psddev.dari.util.StorageItem;

public class VideoBulkUploadProcessor extends BulkUploadProcessor<Video> {

    @Override
    public void onUpload(Video object, StorageItem file) {
        VideoStorageItemWrapper wrapper = new VideoStorageItemWrapper();
        wrapper.setFile(file);

        Html5VideoProvider provider = new Html5VideoProvider();
        provider.setItems(Collections.singletonList(wrapper));

        object.setVideoProvider(provider);
    }

    @Override
    public Set<String> getMimeTypes() {
        return ImmutableSet.copyOf(Arrays.asList("video/mp4", "video/quicktime")); // TODO: Add more
    }

    @Override
    public Set<String> getHiddenFields() {
        return ImmutableSet.copyOf(Arrays.asList("headline", "videoProvider", "preview"));
    }
}
