package brightspot.module.html;

import java.util.List;
import java.util.UUID;

import brightspot.rte.html.HtmlEmbedRichTextElement;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is to provide drag and drop support for {@link HtmlEmbed}
 * from the CMS UI Shelf into {@link HtmlEmbedModulePlacementShared}.
 */
public class HtmlEmbedSubstitution extends HtmlEmbed implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - HtmlEmbedModulePlacementShared
        if (target instanceof HtmlEmbedModulePlacementShared) {
            HtmlEmbedModulePlacementShared htmlEmbedModulePlacementShared = (HtmlEmbedModulePlacementShared) target;
            htmlEmbedModulePlacementShared.setShared(this);
            return true;
        } else if (target instanceof HtmlEmbedRichTextElement) {
            HtmlEmbedRichTextElement htmlEmbedRichTextElement = (HtmlEmbedRichTextElement) target;
            htmlEmbedRichTextElement.setShared(this);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - HtmlEmbedModulePlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(HtmlEmbedModulePlacementShared.class).getId(),
                ObjectType.getInstance(HtmlEmbedRichTextElement.class).getId()
        );
    }
}
