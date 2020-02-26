package brightspot.core.carousel;

import java.util.List;

import brightspot.core.image.Image;
import brightspot.core.imageitemstream.SimpleImageItemStream;
import com.psddev.cms.rte.RichTextUploadTransformer;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Gallery Enhancement")
public class CarouselRichTextUploadTransformer extends RichTextUploadTransformer<Image> {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String transform(List<Image> images) {
        CarouselRichTextElement element = ObjectUtils.build(new CarouselRichTextElement(), e -> {
            e.setTitle(getTitle());

            e.setItemStream(ObjectUtils.build(new SimpleImageItemStream(), s -> {
                s.getItems().addAll(images);
            }));
        });

        return element.toHtml();
    }

    @Override
    public boolean isPreferred(long count) {
        return count > 1;
    }
}
