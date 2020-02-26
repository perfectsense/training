package brightspot.core.site;

import java.util.stream.Collectors;

import brightspot.core.page.PageElementSupplier;
import com.psddev.cms.db.Site;

public class IntegrationExtraBodyAttributesSupplier implements PageElementSupplier<IntegrationExtraBodyAttributes> {

    @Override
    public Iterable<IntegrationExtraBodyAttributes> get(Site site, Object mainObject) {
        return FrontEndSettings.get(site, FrontEndSettings::getIntegrations)
            .stream()
            .filter(e -> e instanceof IntegrationExtraBodyAttributes)
            .map(e -> (IntegrationExtraBodyAttributes) e)
            .collect(Collectors.toList());
    }
}
