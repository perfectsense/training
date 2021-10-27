package brightspot.facebook.opengraph;

import java.util.Optional;

import brightspot.author.HasAuthorsWithFieldData;
import brightspot.facebook.FacebookSettingsModification;
import brightspot.social.SocialEntity;
import brightspot.social.facebook.FacebookSocialEntityData;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
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
        return Optional.ofNullable(model.as(HasAuthorsWithFieldData.class))
            .map(HasAuthorsWithFieldData::getAuthors)
            .filter(a -> a instanceof SocialEntity)
            .map(f -> f.isEmpty() ? null : f.get(0))
            .map(a -> (SocialEntity) a)
            .map(a -> a.as(FacebookSocialEntityData.class))
            .map(FacebookSocialEntityData::getFacebookUsername)
            .orElse(null);
    }

    @Override
    public CharSequence getAppId() {
        return SiteSettings.get(site, item -> item.as(FacebookSettingsModification.class).getFacebookAppId());
    }
}
