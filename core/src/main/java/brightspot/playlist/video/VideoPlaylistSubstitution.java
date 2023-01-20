package brightspot.playlist.video;

import java.util.List;
import java.util.UUID;

import brightspot.rte.playlist.video.VideoPlaylistRichTextElement;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

public class VideoPlaylistSubstitution extends VideoPlaylist implements
        Interchangeable,
        Substitution {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - VideoPlaylistRichTextElement
        // - VideoPlaylistPlacementShared
        if (target instanceof VideoPlaylistRichTextElement) {

            VideoPlaylistRichTextElement videoPlaylistRichTextElement = (VideoPlaylistRichTextElement) target;
            videoPlaylistRichTextElement.setPlaylist(this);

            return true;

        } else if (target instanceof VideoPlaylistPlacementShared) {

            VideoPlaylistPlacementShared videoPlaylistPlacementShared = (VideoPlaylistPlacementShared) target;
            videoPlaylistPlacementShared.setShared(this);

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - VideoPlaylistRichTextElement
        // - VideoPlaylistPlacementShared
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(VideoPlaylistRichTextElement.class).getId(),
                ObjectType.getInstance(VideoPlaylistPlacementShared.class).getId()
        );
    }
}
