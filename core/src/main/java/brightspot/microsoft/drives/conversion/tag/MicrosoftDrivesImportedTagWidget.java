package brightspot.microsoft.drives.conversion.tag;

import java.util.Collections;
import java.util.Set;

import brightspot.tag.TagPage;
import com.psddev.cms.tool.widget.AssociatedContentWidget;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;

/**
 * Shows the {@link TagPage} objects that were imported from the same Microsoft spreadsheet.
 */
public class MicrosoftDrivesImportedTagWidget extends AssociatedContentWidget {

    private static final String IMPORTED_TAG_FIELD =
        MicrosoftDrivesTagPageModification.FIELD_PREFIX + "originalImportedMicrosoftSpreadsheetTag";

    @Override
    protected Class<? extends Recordable> getContainingClass() {
        return ImportedMicrosoftSpreadsheetTag.class;
    }

    @Override
    protected Set<ObjectType> getAssociatedTypes() {
        return Collections.singleton(ObjectType.getInstance(TagPage.class));
    }

    @Override
    protected Class<? extends Recordable> getAssociationClass() {
        return MicrosoftDrivesTagPageModification.class;
    }

    @Override
    protected String getFullyQualifiedAssociationFieldName() {
        return MicrosoftDrivesTagPageModification.class.getName() + "/" + IMPORTED_TAG_FIELD;
    }
}
