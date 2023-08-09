package brightspot.google.drives.conversion.tag;

import java.util.Collections;
import java.util.Set;

import brightspot.tag.TagPage;
import com.psddev.cms.tool.widget.AssociatedContentWidget;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;

/**
 * Shows the {@link TagPage} objects that were imported from the same Google Sheet.
 */
public class GoogleDriveImportedTagWidget extends AssociatedContentWidget {

    private static final String IMPORTED_TAG_FIELD =
        GoogleDriveTagPageModification.FIELD_PREFIX + "originalImportedGoogleSheetTag";

    @Override
    protected Class<? extends Recordable> getContainingClass() {
        return ImportedGoogleSheetTag.class;
    }

    @Override
    protected Set<ObjectType> getAssociatedTypes() {
        return Collections.singleton(ObjectType.getInstance(TagPage.class));
    }

    @Override
    protected Class<? extends Recordable> getAssociationClass() {
        return GoogleDriveTagPageModification.class;
    }

    @Override
    protected String getFullyQualifiedAssociationFieldName() {
        return GoogleDriveTagPageModification.class.getName() + "/" + IMPORTED_TAG_FIELD;
    }
}
