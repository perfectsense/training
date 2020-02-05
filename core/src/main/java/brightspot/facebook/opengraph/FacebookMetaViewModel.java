package brightspot.facebook.opengraph;

import java.util.Optional;

import brightspot.core.person.Author;
import brightspot.core.person.AuthorableData;
import brightspot.core.social.SocialEntityData;
import brightspot.facebook.FacebookSettingsModification;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.dari.db.Recordable;
import com.psddev.styleguide.facebook.FacebookMetasView;

public class FacebookMetaViewModel extends ViewModel<Recordable> implements FacebookMetasView {

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

    /**
     * The returned facebook ID should be "The Facebook ID of a user that can be followed". Right now, just returns the
     * facebook id of the first author, but should be changed to something more meaningful.
     */
    @Override
    public CharSequence getProfileId() {
        return Optional.ofNullable(model.as(AuthorableData.class))
            .map(AuthorableData::getAuthors)
            .map(f -> f.isEmpty() ? null : f.get(0))
            .map(Author::asSocialEntityData)
            .map(SocialEntityData::getFacebookUsername)
            .orElse(null);
    }

    @Override
    public CharSequence getAppId() {
        return SiteSettings.get(site, item -> item.as(FacebookSettingsModification.class).getFacebookAppId());
    }
}
