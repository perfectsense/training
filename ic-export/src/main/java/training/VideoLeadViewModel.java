package training;

import brightspot.core.video.Video;
import brightspot.core.video.VideoLead;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.dari.util.UuidUtils;

@JsonView
@ViewInterface
public class VideoLeadViewModel extends ExportViewModel<Video> implements ArticleLeadView {

    @ViewKey("video")
    public RefView getVideo() {
        return createView(RefView.class, model);
    }

    @Override
    public String getId() {
        // Video is not embedded so need to make up a new id here for the embedded VideoLead
        return UuidUtils.createSequentialUuid().toString();
    }

    @Override
    @ViewKey("_type")
    public String getType() {
        return ExportUtils.getExportType(new VideoLead());
    }
}

