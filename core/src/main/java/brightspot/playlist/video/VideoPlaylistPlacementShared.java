package brightspot.playlist.video;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import brightspot.module.ModularSearchIndexFields;
import brightspot.module.ModulePlacement;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared Playlist")
@Deprecated
public class VideoPlaylistPlacementShared extends Record implements
    ModelWrapper,
    ModularSearchIndexFields,
    ModulePlacement {

    @Required
    private VideoPlaylist shared;

    public VideoPlaylist getShared() {
        return shared;
    }

    public void setShared(VideoPlaylist shared) {
        this.shared = shared;
    }

    // --- ModelWrapper support ---

    @Override
    public VideoPlaylist unwrap() {
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
            .map(VideoPlaylist::getLabel)
            .orElseGet(super::getLabel);
    }
}
