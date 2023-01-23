package brightspot.podcast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import brightspot.podcast.providers.HasPodcastProvidersMetadata;
import brightspot.podcast.providers.PodcastProviderAssetMetadata;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.podcast.list.PodcastProviderListView;
import com.psddev.styleguide.podcast.list.PodcastProviderListViewItemsField;

public class PodcastProvidersViewModel extends ViewModel<HasPodcastProvidersMetadata>
    implements PodcastProviderListView {

    @CurrentSite
    private Site site;

    private List<PodcastProviderAssetMetadata<?>> metadatas = new ArrayList<>();

    @Override
    protected boolean shouldCreate() {
        metadatas = model.getPodcastProviderAssetMetadata()
            .stream()
            .filter(metadata -> metadata.getProvider(site) != null)
            .collect(Collectors.toList());
        return !ObjectUtils.isBlank(metadatas);
    }

    @Override
    public Iterable<? extends PodcastProviderListViewItemsField> getItems() {
        return createViews(PodcastProviderListViewItemsField.class, metadatas);
    }
}
