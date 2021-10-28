package training;

import brightspot.core.video.Video;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.text.StringEscapeUtils;

@JsonView
@ViewInterface
public abstract class AbstractVideoExportViewModel extends ExportViewModel<Video> {

    @ViewKey("video.titleOverride")
    public CharSequence getTitleOverride() {
        return StringEscapeUtils.escapeHtml4(ObjectUtils.to(String.class, model.getState().get("headline")));
    }

    @ViewKey("video.descriptionOverride")
    public CharSequence getDescriptionOverride() {
        return ExportUtils.processRichText(model, v -> ObjectUtils.to(String.class, v.getState().get("subHeadline")));
    }

    @ViewKey("video.thumbnailImageOverride")
    public RefView getThumbnailImageOverride() {
        return createView(RefView.class, model.getState().get("promotable.promoImage"));
    }

    @ViewKey("hasTags.tags")
    public Iterable<RefView> getTags() {
        return createViews(RefView.class, model.asTaggableData().getTags());
    }

    @Override
    @ViewKey("_type")
    public String getType() {
        return ExportUtils.getExportType(model.getVideoProvider());
    }
}
