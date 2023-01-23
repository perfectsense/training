package brightspot.form;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.util.SubstitutionTarget;

/**
 * The purpose of this class is to provide drag and drop support for {@link ContactForm}
 * from the CMS UI Shelf into {@link FormPlacementShared}.
 */
@SubstitutionTarget(AbstractForm.class)
public class AbstractFormSubstitution extends Record implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - FormPlacementShared
        if (target instanceof FormPlacementShared) {
            FormPlacementShared formPlacementShared = (FormPlacementShared) target;
            formPlacementShared.setShared(this.as(AbstractForm.class));
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - FormPlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(FormPlacementShared.class).getId()
        );
    }
}
