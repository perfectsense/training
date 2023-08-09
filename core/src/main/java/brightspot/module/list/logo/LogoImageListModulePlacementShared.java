
package brightspot.module.list.logo;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import brightspot.module.ModularSearchIndexFields;
import brightspot.module.ModulePlacement;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared Logo List Module")
@Recordable.Embedded
@Deprecated
public class LogoImageListModulePlacementShared extends Record implements
    ModelWrapper,
    ModularSearchIndexFields,
    ModulePlacement {

    private LogoImageListModule shared;

    public LogoImageListModule getShared() {
        return shared;
    }

    public void setShared(LogoImageListModule shared) {
        this.shared = shared;
    }

    // --- ModelWrapper Support ---

    @Override
    public Object unwrap() {
        return getShared();
    }

    // --- ModularSearchIndexSupport Support ---

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

    // --- Recordable Support ---

    @Override
    public String getLabel() {

        return Optional.ofNullable(getShared())
            .map(Record::getLabel)
            .orElse(super.getLabel());
    }
}
