package brightspot.core.video;

import brightspot.core.cascading.Cascading;
import brightspot.core.cascading.CascadingPageElement;
import brightspot.core.cascading.CascadingPageElementsModification;
import brightspot.core.playlist.Playlist;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("videoCascading.")
@Modification.Classes({ VideoPageElements.class, SiteSettings.class })
public class VideoCascadingData extends Modification<Object> {

    @CascadingPageElement
    @ToolUi.Cluster("Video")
    private CascadingPlaylist defaultPlaylist;

    public Cascading<Playlist> getCascadingPlaylist() {
        return defaultPlaylist;
    }

    public void setCascadingPlaylist(CascadingPlaylist defaultPlaylist) {
        this.defaultPlaylist = defaultPlaylist;
    }

    public Playlist getDefaultPlaylist(Site site) {
        return as(CascadingPageElementsModification.class)
            .get(site, VideoCascadingData.class,
                VideoCascadingData::getCascadingPlaylist);
    }

    /**
     * To support localization for the "Inherit" placeholder.
     */
    public String getInheritPlaceholder() {
        return Localization.currentUserText(null, "option.inherit", "Inherit");
    }
}
