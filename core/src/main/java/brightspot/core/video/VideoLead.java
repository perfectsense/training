package brightspot.core.video;

import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.lead.Lead;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Video")
@Recordable.Embedded
public class VideoLead extends Record implements Lead {

    @ToolUi.Placeholder(dynamicText = "${content.getHeadlineFallback()}")
    private String headline;

    @ToolUi.Placeholder(dynamicText = "${content.getSubHeadlineFallback()}")
    private String subHeadline;

    @Required
    private VideoOption video = VideoOption.createDefault();

    @Override
    public ImageOption getLeadImage() {
        return Optional.ofNullable(video.getVideoOptionVideo())
            .map(Video::getLeadItemImage)
            .orElse(null);
    }

    public Video getVideo() {
        return Optional.ofNullable(video.getVideoOptionVideo())
            .orElse(null);
    }

    public VideoOption getVideoOption() {
        return video;
    }

    public void setVideo(VideoOption video) {
        this.video = video;
    }

    public String getHeadline() {
        return ObjectUtils.firstNonNull(headline, getHeadlineFallback());
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSubHeadline() {
        return ObjectUtils.firstNonNull(subHeadline, getSubHeadlineFallback());
    }

    public void setSubHeadline(String subHeadline) {
        this.subHeadline = subHeadline;
    }

    public String getHeadlineFallback() {
        return Optional.ofNullable(video.getVideoOptionVideo()).map(Video::getHeadline).orElse(null);
    }

    public String getSubHeadlineFallback() {
        return Optional.ofNullable(video.getVideoOptionVideo()).map(Video::getSubHeadline).orElse(null);
    }

}
