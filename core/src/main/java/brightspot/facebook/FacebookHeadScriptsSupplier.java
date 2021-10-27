package brightspot.facebook;

import java.util.Collections;
import java.util.Optional;

import brightspot.page.PageElementSupplier;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;

/**
 * Producer of HEAD SCRIPT for Facebook (Share Button). In essence this will produce list of objects that later will be
 * rendered as scripts to be put at the HEAD section of the HTML
 */
public class FacebookHeadScriptsSupplier implements PageElementSupplier<FacebookSettings> {

    @Override
    public Iterable<FacebookSettings> get(Site site, Object mainObject) {

        return Optional.ofNullable(
            SiteSettings.get(site, item -> item.as(FacebookSettingsModification.class).getFacebookSettings()))
            .map(Collections::singletonList)
            .orElse(null);

    }
}
