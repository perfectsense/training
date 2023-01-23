package brightspot.module.list.quote;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of the {@link Interchangeable} implementation on this class is to
 * provide drag and drop support for {@link QuoteListModule} from the CMS UI
 * Shelf into {@link QuoteListModulePlacementShared}.
 */
public class QuoteListModuleSubstitution extends QuoteListModule implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - QuoteListModulePlacementShared
        if (target instanceof QuoteListModulePlacementShared) {
            QuoteListModulePlacementShared quoteListModulePlacementShared = (QuoteListModulePlacementShared) target;
            quoteListModulePlacementShared.setShared(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - QuoteListModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(QuoteListModulePlacementShared.class).getId()
        );
    }
}
