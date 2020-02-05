package brightspot.core.site;

import java.util.stream.Collectors;

import brightspot.core.page.PageElementSupplier;
import com.psddev.cms.db.Site;

public final class IntegrationHeadScriptsSupplier implements PageElementSupplier<IntegrationHeadScripts> {

    @Override
    public Iterable<IntegrationHeadScripts> get(Site site, Object mainObject) {
        return FrontEndSettings.get(site, FrontEndSettings::getIntegrations)
            .stream()
            .filter(e -> e instanceof IntegrationHeadScripts)
            .map(e -> (IntegrationHeadScripts) e)
            .collect(Collectors.toList());
    }
}
