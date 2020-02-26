package brightspot.core.search;

import brightspot.core.promo.PromoOption;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;

public class SearchSpotlightSiteOverride extends Record {

    @Required
    @ToolUi.DropDown
    private Site site;

    @Required
    private PromoOption promo;

    public PromoOption getPromo() {
        return promo;
    }

    public void setPromo(PromoOption promo) {
        this.promo = promo;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public String getLabel() {
        return getSite() == null ? "Global" : getSite().getName();
    }
}
