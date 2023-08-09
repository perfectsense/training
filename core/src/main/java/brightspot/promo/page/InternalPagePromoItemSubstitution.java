package brightspot.promo.page;

import java.util.Optional;

import brightspot.cascading.CascadingPageElements;
import brightspot.image.WebImageAsset;
import brightspot.page.CascadingPageData;
import com.psddev.cms.db.Site;
import com.psddev.dari.util.Substitution;

public class InternalPagePromoItemSubstitution extends InternalPagePromoItem implements Substitution {

    @Override
    public WebImageAsset getPromoItemImage() {
        return Optional.ofNullable(super.getPromoItemImage())
            .orElseGet(() -> getPromotable()
                .filter(p -> CascadingPageElements.class.isAssignableFrom(p.getClass()))
                .map(p -> p.getState().as(CascadingPageData.class).getDefaultPromoImage(p.as(Site.ObjectModification.class).getOwner()))
                .orElse(null));
    }
}
