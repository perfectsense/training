package brightspot.module.list.podcast.shared;

import java.util.Optional;

import brightspot.module.ModulePlacement;
import brightspot.module.list.podcast.PodcastListModule;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared Podcast List")
@Recordable.Embedded
@Deprecated
public class PodcastListModulePlacementShared extends Record implements ModelWrapper, ModulePlacement {

    private PodcastListModule shared;

    public PodcastListModule getShared() {
        return shared;
    }

    public void setShared(PodcastListModule shared) {
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
