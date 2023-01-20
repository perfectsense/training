package brightspot.module.list.attachment.shared;

import java.util.List;
import java.util.UUID;

import brightspot.module.list.attachment.inline.AttachmentListModulePlacementInline;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is to provide support for CMS embedded conversion between
 * {@link AttachmentListModulePlacementShared} and {@link AttachmentListModulePlacementInline}.
 */
public class AttachmentListModulePlacementSharedSubstitution extends AttachmentListModulePlacementShared implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        if (getShared() == null) {
            return false;
        }

        if (target instanceof AttachmentListModulePlacementInline) {

            AttachmentListModule sharedModule = getShared();
            State targetState = State.getInstance(target);
            targetState.putAll(State.getInstance(Copyable.copy(targetState.getType(), sharedModule)).getSimpleValues());

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        return ImmutableList.of(
                ObjectType.getInstance(AttachmentListModulePlacementInline.class).getId()
        );
    }
}
