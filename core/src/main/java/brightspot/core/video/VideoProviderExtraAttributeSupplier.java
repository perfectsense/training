package brightspot.core.video;

import java.util.stream.Collectors;

import brightspot.core.page.PageElementSupplier;
import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;

public class VideoProviderExtraAttributeSupplier implements PageElementSupplier<VideoProviderExtraAttributes> {

    @Override
    public Iterable<VideoProviderExtraAttributes> get(Site site, Object object) {
        return FrontEndSettings.get(site, FrontEndSettings::getIntegrations)
            .stream()
            .filter(e -> e instanceof VideoProviderExtraAttributes)
            .map(e -> (VideoProviderExtraAttributes) e)
            .collect(Collectors.toList());
    }
}
