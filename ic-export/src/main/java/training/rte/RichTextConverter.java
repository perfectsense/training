package training.rte;

import java.util.Optional;

import com.psddev.dari.db.Recordable;
import org.jsoup.nodes.Element;

public abstract class RichTextConverter {

    public abstract void convert(Recordable parent, Element element);

    public static RichTextConverter getConverter(String tagName) {
        return Optional.ofNullable(ConverterMapping.CONVERTERS.get(tagName))
            .orElseThrow(() -> new IllegalArgumentException("Converter not found for " + tagName));
    }
}
