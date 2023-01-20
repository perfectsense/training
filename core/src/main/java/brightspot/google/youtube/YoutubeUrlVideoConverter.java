package brightspot.google.youtube;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import brightspot.tag.HasTagsWithFieldData;
import brightspot.tag.Tag;
import brightspot.video.VideoData;
import com.psddev.cms.db.ExternalItemConverter;
import com.psddev.cms.db.Site;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Query;
import com.psddev.dari.web.WebRequest;
import com.psddev.google.youtube.YouTubeVideo;

/**
 * Creates a Youtube Url Video from a YouTube Video
 */

public class YoutubeUrlVideoConverter extends ExternalItemConverter<YouTubeVideo, YouTubeUrlVideo> {

    @Override
    public Collection<? extends YouTubeUrlVideo> convert(YouTubeVideo youTubeVideo) {
        YouTubeUrlVideo youtubeUrlVideo = new YouTubeUrlVideo();
        youtubeUrlVideo.setYoutubeIdentifier(youTubeVideo.getYouTubeId());
        VideoData video = youtubeUrlVideo.as(VideoData.class);
        video.setTitleOverride(youTubeVideo.getTitle());
        video.setDescriptionOverride(youTubeVideo.getDescription());

        List<String> youtubeTags = youTubeVideo.getTags();

        if (!youtubeTags.isEmpty() && WebRequest.isAvailable()) {
            Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
            youtubeUrlVideo.as(HasTagsWithFieldData.class).setTags(Query.from(Tag.class).where("tag.getTagDisplayNamePlainText = ?", youtubeTags).and(site != null ? site.itemsPredicate() : null).selectAll());
        }
        return Collections.singletonList(youtubeUrlVideo);
    }
}
