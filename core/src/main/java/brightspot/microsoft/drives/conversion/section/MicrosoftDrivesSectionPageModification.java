package brightspot.microsoft.drives.conversion.section;

import brightspot.section.SectionPage;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

/**
 * {@link Modification} that associates an {@link ImportedMicrosoftSpreadsheetSection} to it's corresponding {@link
 * SectionPage}.
 */
@Recordable.FieldInternalNamePrefix(MicrosoftDrivesSectionPageModification.FIELD_PREFIX)
public class MicrosoftDrivesSectionPageModification extends Modification<SectionPage> {

    static final String FIELD_PREFIX = "microsoft.drives.";

    @Indexed
    @ToolUi.Hidden
    private String importKey;

    @Indexed
    @ToolUi.Hidden
    private ImportedMicrosoftSpreadsheetSection originalImportedMicrosoftSpreadsheetSection;

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    public ImportedMicrosoftSpreadsheetSection getOriginalImportedMicrosoftSpreadsheetSection() {
        return originalImportedMicrosoftSpreadsheetSection;
    }

    public void setOriginalImportedMicrosoftSpreadsheetSection(ImportedMicrosoftSpreadsheetSection originalImportedMicrosoftSpreadsheetSection) {
        this.originalImportedMicrosoftSpreadsheetSection = originalImportedMicrosoftSpreadsheetSection;
    }
}
