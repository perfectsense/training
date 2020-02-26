package brightspot.core.site;

import java.util.stream.Collectors;

import brightspot.core.page.PageElementSupplier;
import com.psddev.cms.db.Site;

public final class IntegrationExtraBodyItemsSupplier implements PageElementSupplier<IntegrationExtraBodyItems> {

    @Override
    public Iterable<IntegrationExtraBodyItems> get(Site site, Object mainObject) {
        return FrontEndSettings.get(site, FrontEndSettings::getIntegrations)
            .stream()
            .filter(e -> e instanceof IntegrationExtraBodyItems)
            .map(e -> (IntegrationExtraBodyItems) e)
            .collect(Collectors.toList());
    }
}
