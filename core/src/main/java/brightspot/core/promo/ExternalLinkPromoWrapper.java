package brightspot.core.promo;

import java.util.Optional;

import brightspot.core.link.ExternalLink;
import com.psddev.cms.db.Site;

public class ExternalLinkPromoWrapper extends PromoWrapper {

    protected ExternalLink item;

    public ExternalLinkPromoWrapper(ExternalLink item, Promo overrides) {
        super(item, overrides);
        this.item = item;
    }

    @Override
    public String getUrl(Site site) {
        return Optional.ofNullable(item)
            .map(ExternalLink::getUrl)
            .orElse(null);
    }

    @Override
    public String getType() {
        return "external";
    }
}
