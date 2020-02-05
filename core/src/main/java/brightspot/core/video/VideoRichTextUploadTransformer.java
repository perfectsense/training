package brightspot.core.video;

import java.util.List;

import com.psddev.cms.rte.RichTextUploadTransformer;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Video Enhancements")
public class VideoRichTextUploadTransformer extends RichTextUploadTransformer<Video> {

    @Override
    public String transform(List<Video> videos) {
        StringBuilder html = new StringBuilder();

        for (Video video : videos) {
            VideoRichTextElement element = ObjectUtils.build(new VideoRichTextElement(), e -> {
                e.setVideo(video);
            });

            html.append(element.toHtml());
        }

        return html.toString();
    }
}
