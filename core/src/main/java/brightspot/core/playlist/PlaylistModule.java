package brightspot.core.playlist;

import brightspot.core.link.Link;
import brightspot.core.module.ModuleType;
import brightspot.core.timedcontentitemstream.PlaylistModuleItemStream;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Playlist")
public class PlaylistModule extends ModuleType {

    private static final int DEFAULT_MAX_ITEMS = 10;

    private String title;

    @Embedded
    @Required
    private PlaylistModuleItemStream playlist = PlaylistModuleItemStream.createDefault();

    @ToolUi.Placeholder("" + DEFAULT_MAX_ITEMS)
    private Integer maxItems;

    private Link callToAction;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Link getCallToAction() {
        return callToAction;
    }

    public void setCallToAction(Link callToAction) {
        this.callToAction = callToAction;
    }

    public PlaylistModuleItemStream getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistModuleItemStream playlist) {
        this.playlist = playlist;
    }

    public Integer getMaxItems() {
        return ObjectUtils.firstNonNull(maxItems, DEFAULT_MAX_ITEMS);
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }
}
