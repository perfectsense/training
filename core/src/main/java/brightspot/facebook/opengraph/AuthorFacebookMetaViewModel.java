package brightspot.facebook.opengraph;

import java.util.Optional;

import brightspot.core.person.Author;
import brightspot.core.social.SocialEntityData;
import brightspot.facebook.FacebookSettingsModification;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.facebook.FacebookMetasView;

public class AuthorFacebookMetaViewModel extends ViewModel<Author> implements FacebookMetasView {

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
        return Optional.ofNullable(model.asSocialEntityData())
            .map(SocialEntityData::getFacebookUsername)
            .orElse(null);
    }

    @Override
    public CharSequence getAppId() {
        return SiteSettings.get(site, item -> item.as(FacebookSettingsModification.class).getFacebookAppId());
    }
}
