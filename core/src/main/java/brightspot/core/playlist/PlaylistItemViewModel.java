package brightspot.core.playlist;

import java.util.Optional;

import brightspot.core.page.PageViewModel;
import brightspot.core.person.Authorable;
import brightspot.core.promo.Promotable;
import brightspot.core.tool.DateTimeUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.video.PlaylistItemView;
import com.psddev.styleguide.core.video.PlaylistItemViewActionsField;
import com.psddev.styleguide.core.video.PlaylistItemViewAuthorImageField;
import com.psddev.styleguide.core.video.PlaylistItemViewCompanionsField;
import com.psddev.styleguide.core.video.PlaylistItemViewMediaField;
import com.psddev.styleguide.core.video.PlaylistItemViewSocialField;

public class PlaylistItemViewModel extends ViewModel<PlaylistItem> implements PlaylistItemView {

    @CurrentSite
    Site site;

    @Override
    public Iterable<? extends PlaylistItemViewActionsField> getActions() {
        return createViews(PlaylistItemViewActionsField.class, model.getTimedContent());
    }

    @Override
    public Iterable<? extends PlaylistItemViewAuthorImageField> getAuthorImage() {
        return Authorable.getAuthorImage(model.getTimedContent())
            .map(image -> this.createViews(PlaylistItemViewAuthorImageField.class, image))
            .orElse(null);
    }

    @Override
    public CharSequence getAuthorName() {
        return Authorable.getAuthorName(model.getTimedContent());
    }

    @Override
    public CharSequence getAuthorUrl() {
        return Authorable.getAuthorUrl(site, model.getTimedContent());
    }

    @Override
    public CharSequence getButton() {
        return null;
    }

    @Override
    public CharSequence getCategory() {
        return getTimedContentAs(Promotable.class)
            .map(promotable -> promotable.getPromotableCategoryUrl(site))
            .orElse(null);
    }

    @Override
    public CharSequence getCategoryUrl() {
        return getTimedContentAs(Promotable.class)
            .map(promotable -> promotable.getPromotableCategoryUrl(site))
            .orElse(null);
    }

    @Override
    public Iterable<? extends PlaylistItemViewCompanionsField> getCompanions() {
        return createViews(PlaylistItemViewCompanionsField.class, model.getTimedCompanions());
    }

    @Override
    public CharSequence getContentId() {
        return getTimedContentAs(Promotable.class)
            .map(promotable -> promotable.getState().getId().toString())
            .orElse(null);
    }

    @Override
    public Number getCount() {
        return null;
    }

    @Override
    public CharSequence getDate() {
        return getTimedContentAs(Promotable.class)
            .map(Promotable::getPromotableDate)
            .map(date -> DateTimeUtils.format(date, Promotable.class, Promotable.DATE_FORMAT_KEY, site,
                PageViewModel.DEFAULT_DATE_FORMAT))
            .orElse(null);
    }

    @Override
    public CharSequence getDescription() {
        return getTimedContentAs(Promotable.class)
            .map(Promotable::getPromotableDescription)
            .orElse(null);
    }

    @Override
    public CharSequence getDuration() {
        return getTimedContentAs(Promotable.class)
            .map(Promotable::getPromotableDuration)
            .orElse(null);
    }

    @Override
    public Iterable<? extends PlaylistItemViewMediaField> getMedia() {
        return getTimedContentAs(Promotable.class)
            .map(Promotable::getPromotableImage)
            .map(image -> createViews(PlaylistItemViewMediaField.class, image))
            .orElse(null);
    }

    @Override
    public Iterable<? extends PlaylistItemViewSocialField> getSocial() {
        return null;
    }

    @Override
    public CharSequence getStatus() {
        return null;
    }

    @Override
    public CharSequence getTarget() {
        return null;
    }

    @Override
    public CharSequence getTitle() {
        return getTimedContentAs(Promotable.class)
            .map(Promotable::getPromotableTitle)
            .orElse(null);
    }

    @Override
    public CharSequence getType() {
        return getTimedContentAs(Promotable.class)
            .map(Promotable::getPromotableType)
            .orElse(null);
    }

    @Override
    public CharSequence getUrl() {
        return getTimedContentAs(Promotable.class)
            .map(promotable -> promotable.getPromotableUrl(site))
            .orElse(null);
    }

    private <T> Optional<T> getTimedContentAs(Class<T> type) {
        return Optional.ofNullable(model.getTimedContent())
            .filter(type::isInstance)
            .map(type::cast);
    }
}
