package brightspot.module.iframe;

import java.util.List;
import java.util.UUID;

import brightspot.rte.iframe.IframeEmbedRichTextElement;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is to provide drag and drop support for {@link IframeEmbed}
 * from the CMS UI Shelf into {@link IframeEmbedModulePlacementShared}.
 */
public class IframeEmbedSubstitution extends IframeEmbed implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - IframeEmbedModulePlacementShared
        if (target instanceof IframeEmbedModulePlacementShared) {
            IframeEmbedModulePlacementShared iframeEmbedModulePlacementShared = (IframeEmbedModulePlacementShared) target;
            iframeEmbedModulePlacementShared.setShared(this);
            return true;
        } else if (target instanceof IframeEmbedRichTextElement) {
            IframeEmbedRichTextElement iframeEmbedRichTextElement = (IframeEmbedRichTextElement) target;
            iframeEmbedRichTextElement.setShared(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - IframeEmbedModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(IframeEmbedModulePlacementShared.class).getId(),
                ObjectType.getInstance(IframeEmbedRichTextElement.class).getId()
        );
    }
}
