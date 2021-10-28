package training.rte;

import java.util.Objects;

import brightspot.core.video.Video;
import brightspot.core.video.VideoMetaData;
import brightspot.core.video.VideoRichTextElement;
import brightspot.util.FixedMap;
import com.psddev.cms.db.RichTextElement;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import org.jsoup.nodes.Element;
import training.ExportUtils;

public class VideoRTEConverter extends RichTextConverter {

    @Override
    public void convert(Recordable parent, Element element) {
        RichTextElement rte = Objects.requireNonNull(RichTextElement.fromElement(parent, element));
        if (!(rte instanceof VideoRichTextElement)) {
            throw new IllegalArgumentException("Received " + rte.getClass().getName());
        }

        VideoRichTextElement videoRTE = (VideoRichTextElement) rte;

        VideoMetaData videoMetaData = videoRTE.getVideo();
        if (videoMetaData == null) {
            element.remove();
            return;
        }

        if (!(videoMetaData instanceof Video)) {
            throw new IllegalArgumentException("Unhandled VideoMetadata type " + videoMetaData.getClass().getName());
        }

        Video video = (Video) videoMetaData;

        Element replacement = new Element("bsp-video");
        replacement.attr("data-state", ObjectUtils.toJson(FixedMap.of(
            "video", ExportUtils.buildRef(video, video.getVideoProvider()),
            "_id", videoRTE.getId(),
            "_type", ExportUtils.getExportType(videoRTE)
        )));

        replacement.text(video.getLabel());

        element.replaceWith(replacement);
    }
}
