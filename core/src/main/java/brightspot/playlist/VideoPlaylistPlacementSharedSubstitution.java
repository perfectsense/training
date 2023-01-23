package brightspot.playlist;

import java.util.List;
import java.util.UUID;

import brightspot.playlist.video.VideoPlaylist;
import brightspot.playlist.video.VideoPlaylistPlacementInline;
import brightspot.playlist.video.VideoPlaylistPlacementShared;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.Substitution;

public class VideoPlaylistPlacementSharedSubstitution extends VideoPlaylistPlacementShared implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        if (getShared() == null) {
            return false;
        }

        if (target instanceof VideoPlaylistPlacementInline) {

            VideoPlaylist sharedModule = getShared();
            State targetState = State.getInstance(target);
            targetState.putAll(State.getInstance(Copyable.copy(targetState.getType(), sharedModule)).getSimpleValues());

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        return ImmutableList.of(
                ObjectType.getInstance(VideoPlaylistPlacementInline.class).getId()
        );
    }
}
