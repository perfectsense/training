package brightspot.google.drives.conversion.section;

import java.util.Collections;
import java.util.Set;

import brightspot.section.SectionPage;
import com.psddev.cms.tool.widget.AssociatedContentWidget;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;

/**
 * Shows the {@link SectionPage} objects that were imported from the same Google Sheet.
 */
public class GoogleDriveImportedSectionWidget extends AssociatedContentWidget {

    private static final String IMPORTED_SECTION_FIELD =
        GoogleDriveSectionPageModification.FIELD_PREFIX + "originalImportedGoogleSheetSection";

    @Override
    protected Class<? extends Recordable> getContainingClass() {
        return ImportedGoogleSheetSection.class;
    }

    @Override
    protected Set<ObjectType> getAssociatedTypes() {
        return Collections.singleton(ObjectType.getInstance(SectionPage.class));
    }

    @Override
    protected Class<? extends Recordable> getAssociationClass() {
        return GoogleDriveSectionPageModification.class;
    }

    @Override
    protected String getFullyQualifiedAssociationFieldName() {
        return GoogleDriveSectionPageModification.class.getName() + "/" + IMPORTED_SECTION_FIELD;
    }
}
