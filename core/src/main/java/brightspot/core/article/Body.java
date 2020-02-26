package brightspot.core.article;

import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class Body extends Record {

    public static final String INTERNAL_NAME = "brightspot.core.article.Body";

    static Body createDefault() {
        return DefaultImplementationSupplier.createDefault(Body.class, RichTextBody.class);
    }

    /**
     * Returns a plain text preview of the body.
     * <p/>
     * <strong>Note:</strong> For internal use only!
     *
     * @return a plain text {@link String} (optional).
     */
    @Indexed
    @ToolUi.Hidden
    public abstract String getPlainTextPreview();

    /**
     * Returns the {@code character count} of the body.
     *
     * @return a positive {@code long} value.
     */
    public abstract long getCharacterCount();
}
