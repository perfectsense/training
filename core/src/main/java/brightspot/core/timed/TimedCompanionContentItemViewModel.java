package brightspot.core.timed;

import java.util.Optional;

import brightspot.core.page.PageViewModel;
import brightspot.core.person.Authorable;
import brightspot.core.promo.Promotable;
import brightspot.core.tool.DateTimeUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.video.CompanionContentItemView;
import com.psddev.styleguide.core.video.CompanionContentItemViewActionsField;
import com.psddev.styleguide.core.video.CompanionContentItemViewAuthorImageField;
import com.psddev.styleguide.core.video.CompanionContentItemViewMediaField;
import com.psddev.styleguide.core.video.CompanionContentItemViewSocialField;

public class TimedCompanionContentItemViewModel extends ViewModel<TimedCompanion> implements CompanionContentItemView {

    @CurrentSite
    Site site;

    @Override
    public Iterable<? extends CompanionContentItemViewActionsField> getActions() {
        return createViews(CompanionContentItemViewActionsField.class, model.getCompanionContent());
    }

    @Override
    public Iterable<? extends CompanionContentItemViewAuthorImageField> getAuthorImage() {
        return Authorable.getAuthorImage(model.getCompanionContent())
            .map((image) -> this.createViews(CompanionContentItemViewAuthorImageField.class, image))
            .orElse(null);
    }

    @Override
    public CharSequence getAuthorName() {
        return Authorable.getAuthorName(model.getCompanionContent());
    }

    @Override
    public CharSequence getAuthorUrl() {
        return Authorable.getAuthorUrl(site, model.getCompanionContent());
    }

    @Override
    public CharSequence getButton() {
        return null;
    }

    @Override
    public CharSequence getCategory() {
        return getCompanionContentAs(Promotable.class)
            .map(Promotable::getPromotableCategory)
            .orElse(null);
    }

    @Override
    public CharSequence getCategoryUrl() {
        return getCompanionContentAs(Promotable.class)
            .map(promotable -> promotable.getPromotableCategoryUrl(site))
            .orElse(null);
    }

    @Override
    public CharSequence getContentId() {
        return getCompanionContentAs(Promotable.class)
            .map(promotable -> promotable.getState().getId().toString())
            .orElse(null);
    }

    @Override
    public CharSequence getDate() {
        return getCompanionContentAs(Promotable.class)
            .map(Promotable::getPromotableDate)
            .map(date -> DateTimeUtils.format(date, Promotable.class, Promotable.DATE_FORMAT_KEY, site,
                PageViewModel.DEFAULT_DATE_FORMAT))
            .orElse(null);
    }

    @Override
    public CharSequence getDescription() {
        return getCompanionContentAs(Promotable.class)
            .map(Promotable::getPromotableDescription)
            .orElse(null);
    }

    @Override
    public CharSequence getDuration() {
        return getCompanionContentAs(Promotable.class)
            .map(Promotable::getPromotableDuration)
            .orElse(null);
    }

    @Override
    public Iterable<? extends CompanionContentItemViewMediaField> getMedia() {
        return getCompanionContentAs(Promotable.class)
            .map(Promotable::getPromotableImage)
            .map(image -> createViews(CompanionContentItemViewMediaField.class, image))
            .orElse(null);
    }

    @Override
    public Iterable<? extends CompanionContentItemViewSocialField> getSocial() {
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
    public CharSequence getTimeStamp() {
        return Optional.ofNullable(model.getOffsetMilliseconds())
            .map(offset -> offset.toString())
            .orElse(null);
    }

    @Override
    public CharSequence getTitle() {
        return getCompanionContentAs(Promotable.class)
            .map(Promotable::getPromotableTitle)
            .orElse(null);
    }

    @Override
    public CharSequence getType() {
        return getCompanionContentAs(Promotable.class)
            .map(Promotable::getPromotableType)
            .orElse(null);
    }

    @Override
    public CharSequence getUrl() {
        return getCompanionContentAs(Promotable.class)
            .map(promotable -> promotable.getPromotableUrl(site))
            .orElse(null);
    }

    private <T> Optional<T> getCompanionContentAs(Class<T> type) {
        return Optional.ofNullable(model.getCompanionContent())
            .filter(type::isInstance)
            .map(type::cast);
    }
}
