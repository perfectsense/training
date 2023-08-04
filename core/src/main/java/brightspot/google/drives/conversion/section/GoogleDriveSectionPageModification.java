package brightspot.google.drives.conversion.section;

import brightspot.section.SectionPage;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

/**
 * {@link Modification} that associates an {@link ImportedGoogleSheetSection} to it's corresponding
 * {@link SectionPage}.
 */
@Recordable.FieldInternalNamePrefix(GoogleDriveSectionPageModification.FIELD_PREFIX)
public class GoogleDriveSectionPageModification extends Modification<SectionPage> {

    static final String FIELD_PREFIX = "google.drive.";

    @Indexed
    @ToolUi.Hidden
    private String importKey;

    @Indexed
    @ToolUi.Hidden
    private ImportedGoogleSheetSection originalImportedGoogleSheetSection;

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    public ImportedGoogleSheetSection getOriginalImportedGoogleSheetSection() {
        return originalImportedGoogleSheetSection;
    }

    public void setOriginalImportedGoogleSheetSection(ImportedGoogleSheetSection originalImportedGoogleSheetSection) {
        this.originalImportedGoogleSheetSection = originalImportedGoogleSheetSection;
    }
}
