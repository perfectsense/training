package brightspot.microsoft.drives.conversion.tag;

import brightspot.tag.TagPage;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

/**
 * {@link Modification} that associates an {@link ImportedMicrosoftSpreadsheetTag} to it's corresponding {@link
 * TagPage}.
 */
@Recordable.FieldInternalNamePrefix(MicrosoftDrivesTagPageModification.FIELD_PREFIX)
public class MicrosoftDrivesTagPageModification extends Modification<TagPage> {

    static final String FIELD_PREFIX = "microsoft.drives.";

    @Indexed
    @ToolUi.Hidden
    private String importKey;

    @Indexed
    @ToolUi.Hidden
    public ImportedMicrosoftSpreadsheetTag originalImportedMicrosoftSpreadsheetTag;

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    public ImportedMicrosoftSpreadsheetTag getOriginalImportedMicrosoftSpreadsheetTag() {
        return originalImportedMicrosoftSpreadsheetTag;
    }

    public void setOriginalImportedMicrosoftSpreadsheetTag(ImportedMicrosoftSpreadsheetTag originalImportedMicrosoftSpreadsheetTag) {
        this.originalImportedMicrosoftSpreadsheetTag = originalImportedMicrosoftSpreadsheetTag;
    }
}
