package brightspot.google.youtube;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Alteration;

@ToolUi.FieldDisplayPreview({
    "videoMetadataData.getVideoThumbnailImage",
    "videoMetadataData.getVideoTitle",
    "videoMetadataData.getVideoDescription",
    "cms.content.updateDate",
    "cms.content.updateUser" })
public class YouTubeUrlVideoAlteration extends Alteration<YouTubeUrlVideo> {

}
