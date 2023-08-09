package brightspot.module.list.podcast.shared;

import java.util.Optional;

import brightspot.module.ModulePlacement;
import brightspot.module.list.podcast.PodcastEpisodeListModule;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared Podcast Episode List")
@Recordable.Embedded
@Deprecated
public class PodcastEpisodeListModulePlacementShared extends Record implements ModelWrapper, ModulePlacement {

    private PodcastEpisodeListModule shared;

    public PodcastEpisodeListModule getShared() {
        return shared;
    }

    public void setShared(PodcastEpisodeListModule shared) {
        this.shared = shared;
    }

    @Override
    public Object unwrap() {
        return getShared();
    }

    @Override
    public String getLabel() {

        return Optional.ofNullable(getShared())
            .map(Record::getLabel)
            .orElse(super.getLabel());
    }
}
