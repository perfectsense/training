package brightspot.microsoft.drives.conversion.section;

import java.util.Collections;
import java.util.Set;

import brightspot.section.SectionPage;
import com.psddev.cms.tool.widget.AssociatedContentWidget;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;

/**
 * Shows the {@link SectionPage} objects that were imported from the same Microsoft spreadsheet.
 */
public class MicrosoftDrivesImportedSectionWidget extends AssociatedContentWidget {

    private static final String IMPORTED_SECTION_FIELD =
        MicrosoftDrivesSectionPageModification.FIELD_PREFIX + "originalImportedMicrosoftSpreadsheetSection";

    @Override
    protected Class<? extends Recordable> getContainingClass() {
        return ImportedMicrosoftSpreadsheetSection.class;
    }

    @Override
    protected Set<ObjectType> getAssociatedTypes() {
        return Collections.singleton(ObjectType.getInstance(SectionPage.class));
    }

    @Override
    protected Class<? extends Recordable> getAssociationClass() {
        return MicrosoftDrivesSectionPageModification.class;
    }

    @Override
    protected String getFullyQualifiedAssociationFieldName() {
        return MicrosoftDrivesSectionPageModification.class.getName() + "/" + IMPORTED_SECTION_FIELD;
    }
}
