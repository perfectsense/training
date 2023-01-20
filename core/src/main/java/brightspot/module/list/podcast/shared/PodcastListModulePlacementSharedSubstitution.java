package brightspot.module.list.podcast.shared;

import java.util.List;
import java.util.UUID;

import brightspot.module.list.podcast.inline.PodcastListModulePlacementInline;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.Substitution;

/**
 * The purpose of this class is to provide support for CMS embedded conversion between
 * {@link PodcastListModulePlacementShared} and {@link PodcastListModulePlacementInline}.
 */
public class PodcastListModulePlacementSharedSubstitution extends PodcastListModulePlacementShared implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        if (getShared() == null) {
            return false;
        }

        if (target instanceof PodcastListModulePlacementInline) {

            PodcastListModule sharedModule = getShared();
            State targetState = State.getInstance(target);
            targetState.putAll(State.getInstance(Copyable.copy(targetState.getType(), sharedModule)).getSimpleValues());

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        return ImmutableList.of(
                ObjectType.getInstance(PodcastListModulePlacementInline.class).getId()
        );
    }
}
