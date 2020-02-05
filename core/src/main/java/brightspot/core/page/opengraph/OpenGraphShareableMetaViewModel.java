package brightspot.core.page.opengraph;

import java.util.Optional;

import brightspot.core.share.Shareable;
import brightspot.core.share.ShareableData;
import brightspot.core.tool.TextUtils;

public class OpenGraphShareableMetaViewModel extends OpenGraphMetaViewModel<Shareable> {

    @Override
    public CharSequence getDescription() {
        return TextUtils.truncate(
            Optional.ofNullable(model.asShareableData())
                .map(ShareableData::getShareDescription)
                .orElse(null),
            300, true);
    }

    @Override
    public CharSequence getTitle() {
        return Optional.ofNullable(model.asShareableData())
            .map(ShareableData::getShareTitle)
            .orElse(null);
    }
}
