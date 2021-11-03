package brightspot.module.recipe;

import java.util.Optional;

import brightspot.module.ModulePlacement;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Wraps a shared {@link RecipeModule} so it can be used in the {@code @Embedded} context of ModulePlacement.
 *
 * @see RecipeModulePlacementInline
 */

@Recordable.DisplayName("Shared Recipe")
@Recordable.Embedded
public class RecipeModulePlacementShared extends Record implements
    ModelWrapper,
    ModulePlacement {

    private RecipeModule shared;

    // --- Getters/setters ---

    public RecipeModule getShared() {
        return shared;
    }

    public void setShared(RecipeModule shared) {
        this.shared = shared;
    }

    // --- ModelWrapper support ---

    @Override
    public Object unwrap() {
        return getShared();
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return Optional.ofNullable(getShared())
            .map(Record::getLabel)
            .orElse(super.getLabel());
    }
}
