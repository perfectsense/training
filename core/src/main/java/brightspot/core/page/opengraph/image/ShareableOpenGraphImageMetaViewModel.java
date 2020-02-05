package brightspot.core.page.opengraph.image;

import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.share.Shareable;
import brightspot.core.share.ShareableData;

public class ShareableOpenGraphImageMetaViewModel extends OpenGraphImageMetaViewModel<Shareable> {

    @Override
    protected ImageOption getImageOption() {
        return Optional.ofNullable(model.asShareableData())
            .map(ShareableData::getShareImage)
            .orElse(super.getImageOption());
    }
}
