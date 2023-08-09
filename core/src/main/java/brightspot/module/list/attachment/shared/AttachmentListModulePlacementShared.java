package brightspot.module.list.attachment.shared;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import brightspot.module.ModularSearchIndexFields;
import brightspot.module.ModulePlacement;
import brightspot.module.list.attachment.AttachmentListModule;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared Attachment List")
@Recordable.Embedded
@Deprecated
public class AttachmentListModulePlacementShared extends Record
    implements ModelWrapper, ModularSearchIndexFields, ModulePlacement {

    private AttachmentListModule shared;

    public AttachmentListModule getShared() {
        return shared;
    }

    public void setShared(AttachmentListModule shared) {
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
