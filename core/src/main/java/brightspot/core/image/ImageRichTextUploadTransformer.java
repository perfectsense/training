package brightspot.core.image;

import java.util.List;

import com.psddev.cms.rte.RichTextUploadTransformer;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Image Enhancements")
public class ImageRichTextUploadTransformer extends RichTextUploadTransformer<Image> {

    @Override
    public String transform(List<Image> images) {
        StringBuilder html = new StringBuilder();

        for (Image image : images) {
            ImageRichTextElement element = ObjectUtils.build(new ImageRichTextElement(), e -> {
                e.setImage(ObjectUtils.build(new SharedImageOption(), o -> {
                    o.setImage(image);
                }));
            });

            html.append(element.toHtml());
        }

        return html.toString();
    }

    @Override
    public boolean isPreferred(long count) {
        return count == 1;
    }
}
