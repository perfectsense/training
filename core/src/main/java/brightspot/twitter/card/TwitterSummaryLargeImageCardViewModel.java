package brightspot.twitter.card;

import java.util.Map;
import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.person.Authorable;
import brightspot.core.person.AuthorableData;
import brightspot.core.share.Shareable;
import brightspot.core.share.ShareableData;
import brightspot.core.social.SocialEntityData;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.dari.db.Recordable;
import com.psddev.styleguide.twitter.card.TwitterSummaryLargeImageCardView;

public class TwitterSummaryLargeImageCardViewModel extends ViewModel<Recordable>
    implements TwitterSummaryLargeImageCardView {

    @CurrentSite
    Site site;

    @Override
    public CharSequence getSite() {
        return Optional.ofNullable(site)
            .map(site -> site.as(SocialEntityData.class))
            .map(SocialEntityData::getTwitterUsername)
            .orElse(null);
    }

    @Override
    public CharSequence getCreator() {
        return model.isInstantiableTo(Authorable.class)
            ? Optional.ofNullable(model.as(AuthorableData.class))
            .map(AuthorableData::getAuthors)
            .map(authors -> authors.isEmpty() ? null : authors.get(0))
            .map(author -> author.as(SocialEntityData.class))
            .map(SocialEntityData::getTwitterUsername)
            .orElse(null)
            : null;
    }

    @Override
    public Map<String, ?> getImage() {
        return model.isInstantiableTo(Shareable.class)
            ? Optional.ofNullable(model.as(ShareableData.class))
            .map(ShareableData::getShareImage)
            .map(ImageOption::getAttributes)
            .orElse(null)
            : null;
    }

    @Override
    public CharSequence getCreatorId() {
        return null;
    }

    @Override
    public CharSequence getSiteId() {
        return null;
    }

    @Override
    public CharSequence getDescription() {
        //TODO: remove isInstantiableTo check after BSP-4154
        return model.isInstantiableTo(Shareable.class)
            ? Optional.ofNullable(model.as(ShareableData.class))
            .map(ShareableData::getShareDescription)
            .orElse(null)
            : null;
    }

    @Override
    public CharSequence getTitle() {
        //TODO: remove isInstantiableTo check after BSP-4154
        return model.isInstantiableTo(Shareable.class)
            ? Optional.ofNullable(model.as(ShareableData.class))
            .map(ShareableData::getShareTitle)
            .orElse(null)
            : null;
    }

    @Override
    public CharSequence getImageAlt() {
        //TODO: remove isInstantiableTo check after BSP-4154
        return model.isInstantiableTo(Shareable.class)
            ? Optional.ofNullable(model.as(ShareableData.class))
            .map(ShareableData::getShareImage)
            .map(ImageOption::getImageOptionAltText)
            .orElse(null)
            : null;
    }
}
