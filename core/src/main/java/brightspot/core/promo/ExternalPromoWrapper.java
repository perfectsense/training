package brightspot.core.promo;

import java.util.Optional;

import com.psddev.cms.db.Site;

/**
 * @deprecated Replaced by {@link ExternalLinkPromoWrapper}.
 */
@Deprecated
public class ExternalPromoWrapper extends PromoWrapper {

    protected ExternalLinkPromoItem item;

    public ExternalPromoWrapper(ExternalLinkPromoItem item, Promo overrides) {
        super(item, overrides);
        this.item = item;
    }

    @Override
    public String getUrl(Site site) {
        return Optional.ofNullable(item)
            .map(item -> item.getPromoItemUrl(site))
            .orElse(null);
    }

    @Override
    public String getType() {
        return "external";
    }
}
