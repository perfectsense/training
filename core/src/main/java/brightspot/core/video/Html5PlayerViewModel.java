package brightspot.core.video;

import java.util.Map;
import java.util.stream.Collectors;

import com.psddev.cms.image.ImageSize;
import com.psddev.styleguide.core.video.Html5videoPlayerSourceView;
import com.psddev.styleguide.core.video.Html5videoPlayerView;
import com.psddev.styleguide.core.video.Html5videoPlayerViewExtraAttributesField;
import com.psddev.styleguide.core.video.Html5videoPlayerViewSourcesField;

public class Html5PlayerViewModel extends AbstractPlayerViewModel<Html5VideoProvider> implements Html5videoPlayerView {

    @Override
    public Iterable<? extends Html5videoPlayerViewSourcesField> getSources() {
        return model.getItems().stream()
            .filter(item -> item.getFile() != null)
            .map(item -> new Html5videoPlayerSourceView.Builder()
                .src(item.getFile().getPublicUrl())
                .type(item.getFile().getContentType())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, ?> getPoster() {
        VideoMetaData parent = model.getVideoMetaData();

        if ((parent != null) && (parent.getThumbnail() != null)) {
            return ImageSize.getAttributes(parent.getThumbnail().getImageOptionFile());
        }
        return null;
    }

    @Override
    public Iterable<? extends Html5videoPlayerViewExtraAttributesField> getExtraAttributes() {
        return getExtraAttributes(Html5videoPlayerViewExtraAttributesField.class);
    }
}
