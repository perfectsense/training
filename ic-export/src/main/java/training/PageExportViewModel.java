package training;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import brightspot.core.page.Page;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.promo.PromotableWithOverridesData;
import brightspot.core.share.Shareable;
import com.psddev.cms.db.Seo;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.text.StringEscapeUtils;

@JsonView
@ViewInterface
public abstract class PageExportViewModel<T extends Page> extends ExportViewModel<T> {

    // --- Promotable ---

    @ViewKey("pagePromotable.promoTitle")
    public CharSequence getPagePromoTitle() {
        if (!model.isInstantiableTo(PromotableWithOverrides.class)) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(ObjectUtils.to(
            String.class,
            model.getState().get("promotable.promoTitle")));
    }

    @ViewKey("pagePromotable.promoDescription")
    public CharSequence getPagePromoDescription() {
        if (!model.isInstantiableTo(PromotableWithOverrides.class)) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(ObjectUtils.to(
            String.class,
            model.getState().get("promotable.promoDescription")));
    }

    @ViewKey("pagePromotable.promoImage")
    public RefView getPagePromoImage() {
        if (!model.isInstantiableTo(PromotableWithOverrides.class)) {
            return null;
        }
        return createView(RefView.class, model.getState().get("promotable.promoImage"));
    }

    @ViewKey("pagePromotable.promoCategory")
    public CharSequence getPagePromoCategory() {
        if (!model.isInstantiableTo(PromotableWithOverrides.class)) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(ObjectUtils.to(
            String.class,
            model.getState().get("promotable.promoCategory")));
    }

    @ViewKey("pagePromotable.promoCategoryLink")
    public LinkViewModel<?> getPagePromoCategoryLink() {
        if (!model.isInstantiableTo(PromotableWithOverrides.class)) {
            return null;
        }
        return createView(LinkViewModel.class, model.as(PromotableWithOverridesData.class).getPromoCategoryLink());
    }

    // --- SEO ---

    @ViewKey("seo.title")
    public String getSeoTitle() {
        return model.as(Seo.ObjectModification.class).getTitle();
    }

    @ViewKey("seo.description")
    public String getSeoDescription() {
        return model.as(Seo.ObjectModification.class).getDescription();
    }

    @ViewKey("seo.keywords")
    public List<String> getSeoKeywords() {
        return new ArrayList<>(model.as(Seo.ObjectModification.class).getKeywords());
    }

    @ViewKey("seo.robots")
    public List<String> getSeoRobots() {
        return model.as(Seo.ObjectModification.class).getRobots()
            .stream()
            .map(Seo.RobotsValue::name)
            .collect(Collectors.toList());
    }

    // --- Shareable ---

    @ViewKey("shareable.shareTitle")
    public CharSequence getShareTitle() {
        if (!model.isInstantiableTo(Shareable.class)) {
            return null;
        }
        return ObjectUtils.to(String.class, model.getState().get("shareable.shareTitle"));
    }

    @ViewKey("shareable.shareDescription")
    public CharSequence getShareDescription() {
        if (!model.isInstantiableTo(Shareable.class)) {
            return null;
        }
        return ObjectUtils.to(String.class, model.getState().get("shareable.shareDescription"));

    }

    @ViewKey("shareable.shareImage")
    public RefView getShareImage() {
        if (!model.isInstantiableTo(Shareable.class)) {
            return null;
        }
        return createView(RefView.class, model.getState().get("shareable.shareImage"));
    }
}
