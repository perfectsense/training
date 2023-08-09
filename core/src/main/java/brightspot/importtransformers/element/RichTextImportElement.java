package brightspot.importtransformers.element;

import brightspot.importapi.ImportTransformer;
import brightspot.importapi.element.ImportElement;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An import element to convert into BSP Rich Text. Matches to '_type': RichText
 */
public class RichTextImportElement extends ImportElement {

    private static final String TEXT_FIELD = "text";

    @JsonProperty(TEXT_FIELD)
    private String text;

    public String transform(ImportTransformer<?> transformer) {
        return ImportElementUtil.processRichText(this.getText(), transformer);
    }

    public String getText() {
        return text;
    }

}
