package brightspot.module.list.podcast.inline;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import brightspot.module.ModulePlacement;
import brightspot.module.list.podcast.shared.PodcastEpisodeListModule;
import brightspot.module.list.podcast.shared.PodcastEpisodeListModulePlacementShared;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.ui.LocalizationContext;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.web.WebRequest;

/**
 * The purpose of this class is to provide support for CMS embedded conversion between
 * {@link PodcastEpisodeListModulePlacementInline} and {@link PodcastEpisodeListModulePlacementShared}.
 */
public class PodcastEpisodeListModulePlacementInlineSubstitution extends PodcastEpisodeListModulePlacementInline implements
        Copyable,
        Interchangeable,
        Substitution {

    // --- Copyable support ---

    @Override
    public void onCopy(Object source) {
        if (source instanceof PodcastEpisodeListModulePlacementShared) {
            getState().remove("internalName");
        }
    }

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        if (target instanceof PodcastEpisodeListModulePlacementShared) {

            PodcastEpisodeListModule sharedModule = Copyable.copy(ObjectType.getInstance(PodcastEpisodeListModule.class), this);
            String inlineLabel = getLabel();
                        String sharedLabel = ToolLocalization.text(new LocalizationContext(
                    ModulePlacement.class,
                    ImmutableMap.of(
                            "label",
                            ObjectUtils.to(UUID.class, inlineLabel) != null
                                    ? ToolLocalization.text(this.getClass(), "label.untitled")
                                    : inlineLabel,
                            "date",
                            ToolLocalization.dateTime(new Date().getTime()))), "convertLabel");
            // Internal Name field of the shared module will be set to this inline module's label with a converted copy text suffix
            sharedModule.setInternalName(sharedLabel);

            // Publish converted module
            Content.Static.publish(
                    sharedModule,
                    WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite(),
                    WebRequest.getCurrent().as(ToolRequest.class).getCurrentUser());

            // Update the shared placement to reference the newly-published shared module
            PodcastEpisodeListModulePlacementShared sharedPlacement = ((PodcastEpisodeListModulePlacementShared) target);
            sharedPlacement.setShared(sharedModule);

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        return ImmutableList.of(
                ObjectType.getInstance(PodcastEpisodeListModulePlacementShared.class).getId()
        );
    }
}
