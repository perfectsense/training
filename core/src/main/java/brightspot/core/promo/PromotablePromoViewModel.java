package brightspot.core.promo;

import brightspot.core.page.PageViewModel;
import brightspot.core.person.Authorable;
import brightspot.core.tool.DateTimeUtils;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.promo.PromoView;
import com.psddev.styleguide.core.promo.PromoViewActionsField;
import com.psddev.styleguide.core.promo.PromoViewAuthorImageField;
import com.psddev.styleguide.core.promo.PromoViewMediaField;
import com.psddev.styleguide.core.promo.PromoViewSocialField;

public class PromotablePromoViewModel extends ViewModel<Promotable> implements PromoView {

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getAuthorName() {
        return Authorable.getAuthorName(model);
    }

    @Override
    public CharSequence getAuthorUrl() {
        return Authorable.getAuthorUrl(site, model);
    }

    @Override
    public Iterable<? extends PromoViewAuthorImageField> getAuthorImage() {
        return Authorable.getAuthorImage(model)
            .map((image) -> this.createViews(PromoViewAuthorImageField.class, image))
            .orElse(null);
    }

    @Override
    public Iterable<? extends PromoViewMediaField> getMedia() {
        return createViews(PromoViewMediaField.class, model.getPromotableImage());
    }

    @Override
    public CharSequence getUrl() {
        return model.getPromotableUrl(site);
    }

    @Override
    public CharSequence getTarget() {
        return null;
    }

    @Override
    public CharSequence getCategory() {
        return model.getPromotableCategory();
    }

    @Override
    public CharSequence getCategoryUrl() {
        return model.getPromotableCategoryUrl(site);
    }

    @Override
    public CharSequence getType() {
        return model.getPromotableType();
    }

    @Override
    public CharSequence getTitle() {
        // Plain text
        return model.getPromotableTitle();
    }

    @Override
    public CharSequence getDescription() {
        return RichTextUtils.buildInlineHtml(
            model.getState().getDatabase(),
            model.getPromotableDescription(),
            this::createView);
    }

    @Override
    public CharSequence getButton() {
        return null;
    }

    @Override
    public CharSequence getDate() {
        // Plain text
        return DateTimeUtils.format(model.getPromotableDate(), Promotable.class, Promotable.DATE_FORMAT_KEY, site,
            PageViewModel.DEFAULT_DATE_FORMAT);
    }

    @Override
    public CharSequence getDuration() {
        return model.getPromotableDuration();
    }

    @Override
    public Iterable<? extends PromoViewActionsField> getActions() {
        return createViews(PromoViewActionsField.class, model);
    }

    @Override
    public CharSequence getContentId() {
        return model.getState().getId().toString();
    }

    @Override
    public Iterable<? extends PromoViewSocialField> getSocial() {
        return createViews(PromoViewSocialField.class, model);
    }

    @Override
    public CharSequence getStatus() {
        return null;
    }

    @Override
    public Number getCount() {
        return null;
    }
}
