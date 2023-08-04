package brightspot.module.container;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import brightspot.module.ModularSearchIndexFields;
import brightspot.module.ModulePlacement;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;

@Recordable.DisplayName("Shared Container Module")
@Recordable.Embedded
@Deprecated
public class ContainerModulePlacementShared extends Record implements
    ModelWrapper,
    ModularSearchIndexFields,
    ModulePlacement {

    private SharedContainerModule shared;

    public SharedContainerModule getShared() {
        return shared;
    }

    public void setShared(SharedContainerModule shared) {
        this.shared = shared;
    }

    // --- ModelWrapper support ---

    @Override
    public Object unwrap() {
        return getShared();
    }

    // --- ModularSearchIndexFields support ---

    @Override
    public Set<String> getModularSearchHeadingPaths() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getModularSearchBodyPaths() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getModularSearchChildPaths() {
        return Collections.singleton("shared");
    }

    // -- Recordable support ---

    @Override
    public String getLabel() {

        return Optional.ofNullable(getShared())
            .map(Recordable::getState)
            .map(State::getLabel)
            .orElse(super.getLabel());
    }
}
