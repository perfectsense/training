package brightspot.module;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * This is a {@link SharedModulePlacement} that accepts all types of {@link SharedModule}s. This is typically used on
 * generic Page level module placements.
 */
@Recordable.DisplayName("Shared")
@Recordable.Embedded
public class SharedModulePagePlacement extends Record implements
    Interchangeable,
    ModelWrapper,
    ModularSearchIndexFields,
    SharedModulePlacement {

    private SharedModule shared;

    public SharedModule getShared() {
        return shared;
    }

    public void setShared(SharedModule shared) {
        this.shared = shared;
    }

    @Override
    public SharedModule getSharedModule() {
        return getShared();
    }

    @Override
    public void setSharedModule(SharedModule module) {
        setShared(module);
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

    // --- Recordable support ---

    @Override
    public String getLabel() {

        return Optional.ofNullable(getShared())
            .map(Recordable::getLabel)
            .orElse(super.getLabel());
    }
}
