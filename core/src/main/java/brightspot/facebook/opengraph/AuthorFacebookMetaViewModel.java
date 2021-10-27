package brightspot.facebook.opengraph;

import java.util.Optional;

import brightspot.author.PersonAuthor;
import brightspot.facebook.FacebookSettingsModification;
import brightspot.social.facebook.FacebookSocialEntityData;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.facebook.FacebookMetasView;

public class AuthorFacebookMetaViewModel extends ViewModel<PersonAuthor> implements FacebookMetasView {

    @CurrentSite
    protected Site site;

    @Override
    public Iterable<? extends CharSequence> getAdminIds() {
        return null;
    }

    @Override
    public CharSequence getPagesId() {
        return null;
    }

    @Override
    public CharSequence getProfileId() {
        return Optional.ofNullable(model.as(FacebookSocialEntityData.class))
            .map(FacebookSocialEntityData::getFacebookUsername)
            .orElse(null);
    }

    @Override
    public CharSequence getAppId() {
        return SiteSettings.get(site, item -> item.as(FacebookSettingsModification.class).getFacebookAppId());
    }
}
