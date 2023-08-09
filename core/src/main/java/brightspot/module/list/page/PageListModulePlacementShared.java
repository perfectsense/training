package brightspot.module.list.page;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import brightspot.module.ModularSearchIndexFields;
import brightspot.module.ModulePlacement;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared List Module")
@Recordable.Embedded
@Deprecated
public class PageListModulePlacementShared extends Record implements
    ModelWrapper,
    ModularSearchIndexFields,
    ModulePlacement {

    private PageListModule shared;

    public PageListModule getShared() {
        return shared;
    }

    public void setShared(PageListModule shared) {
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

    // --- Recordable support ---

    @Override
    public String getLabel() {

        return Optional.ofNullable(getShared())
            .map(Record::getLabel)
            .orElse(super.getLabel());
    }
}
