package brightspot.core.promo;

import java.util.Optional;

import brightspot.core.page.PageViewModel;
import brightspot.core.tool.DateTimeUtils;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.dari.db.State;
import com.psddev.styleguide.core.promo.PromoView;
import com.psddev.styleguide.core.promo.PromoViewActionsField;
import com.psddev.styleguide.core.promo.PromoViewAuthorImageField;
import com.psddev.styleguide.core.promo.PromoViewMediaField;
import com.psddev.styleguide.core.promo.PromoViewSocialField;

public class PromoWrapperPromoViewModel extends ViewModel<PromoWrapper> implements PromoView {

    @CurrentSite
    protected Site site;

    @Override
    public Iterable<? extends PromoViewMediaField> getMedia() {
        return createViews(PromoViewMediaField.class, model.getMedia());
    }

    @Override
    public CharSequence getUrl() {
        // Plain text
        return model.getUrl(site);
    }

    @Override
    public CharSequence getTarget() {
        // Plain text
        return model.getTarget();
    }

    @Override
    public CharSequence getType() {
        // Plain text
        return model.getType();
    }

    @Override
    public CharSequence getTitle() {
        // Plain text
        return model.getTitle();
    }

    @Override
    public CharSequence getAuthorName() {
        return model.getAuthorName();
    }

    @Override
    public CharSequence getAuthorUrl() {
        return model.getAuthorUrl(site);
    }

    @Override
    public Iterable<? extends PromoViewAuthorImageField> getAuthorImage() {
        return model.getAuthorImage()
            .map(image -> createViews(PromoViewAuthorImageField.class, image))
            .orElse(null);
    }

    @Override
    public CharSequence getDescription() {
        return RichTextUtils.buildInlineHtml(
            ((Promo) model.unwrap()).getState().getDatabase(),
            model.getDescription(),
            this::createView);
    }

    @Override
    public CharSequence getCategory() {
        return model.getCategory();
    }

    public CharSequence getCategoryUrl() {
        return model.getCategoryUrl(site);
    }

    @Override
    public CharSequence getDate() {
        return DateTimeUtils.format(model.getDate(), Promotable.class, Promotable.DATE_FORMAT_KEY, site,
            PageViewModel.DEFAULT_DATE_FORMAT);
    }

    @Override
    public CharSequence getDuration() {
        return model.getDuration();
    }

    @Override
    public boolean shouldCreate() {
        return model.asset != null;
    }

    // -- Unused --

    @Override
    public CharSequence getButton() {
        return null;
    }

    @Override
    public Iterable<? extends PromoViewActionsField> getActions() {
        return Optional.ofNullable(model.asset)
            .map(asset -> createViews(PromoViewActionsField.class, asset))
            .orElse(null);
    }

    @Override
    public CharSequence getContentId() {
        return Optional.ofNullable(model.asset)
            .map(e -> State.getInstance(e).getId().toString())
            .orElse(null);
    }

    @Override
    public Iterable<? extends PromoViewSocialField> getSocial() {
        return Optional.ofNullable(model.asset)
            .map(entity -> createViews(PromoViewSocialField.class, entity))
            .orElse(null);
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
