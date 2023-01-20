package brightspot.google.youtube;

import java.util.List;
import java.util.UUID;

import brightspot.module.video.VideoModule;
import brightspot.playlist.video.VideoPlaylistItem;
import brightspot.rte.video.VideoRichTextElement;
import brightspot.tag.HasTagsWithField;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Substitution;

public class YouTubeUrlVideoSubstitution extends YouTubeUrlVideo implements
        Substitution,
        HasTagsWithField,
        Interchangeable {

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - VideoRichTextElement
        // - VideoModule
        // - VideoPlayListItem
        if (target instanceof VideoRichTextElement) {

            VideoRichTextElement videoRichTextElement = (VideoRichTextElement) target;
            videoRichTextElement.setVideo(this);

            return true;

        } else if (target instanceof VideoModule) {

            VideoModule videoModule = (VideoModule) target;
            videoModule.setVideo(this);

            return true;

        } else if (target instanceof VideoPlaylistItem) {

            VideoPlaylistItem videoPlaylistItem = (VideoPlaylistItem) target;
            videoPlaylistItem.setVideo(this);

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - VideoRichTextElement
        // - VideoModule
        // - VideoPlayListItem
        return ImmutableList.of(
                getState().getTypeId(), // enable dragging and dropping as itself from the shelf
                ObjectType.getInstance(VideoRichTextElement.class).getId(),
                ObjectType.getInstance(VideoModule.class).getId(),
                ObjectType.getInstance(VideoPlaylistItem.class).getId()
        );
    }
}
