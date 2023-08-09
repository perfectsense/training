package brightspot.google.drives.conversion.tag;

import brightspot.tag.TagPage;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

/**
 * {@link Modification} that associates an {@link ImportedGoogleSheetTag} to it's corresponding {@link TagPage}.
 */
@Recordable.FieldInternalNamePrefix(GoogleDriveTagPageModification.FIELD_PREFIX)
public class GoogleDriveTagPageModification extends Modification<TagPage> {

    static final String FIELD_PREFIX = "google.drive.";

    @Indexed
    @ToolUi.Hidden
    private String importKey;

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    @Indexed
    @ToolUi.Hidden
    public ImportedGoogleSheetTag originalImportedGoogleSheetTag;

    public ImportedGoogleSheetTag getOriginalImportedGoogleSheetTag() {
        return originalImportedGoogleSheetTag;
    }

    public void setOriginalImportedGoogleSheetTag(ImportedGoogleSheetTag originalImportedGoogleSheetTag) {
        this.originalImportedGoogleSheetTag = originalImportedGoogleSheetTag;
    }
}
