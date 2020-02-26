package brightspot.core.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.promo.Promo;
import brightspot.core.promo.PromoOption;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.terms.Dictionary;
import com.psddev.terms.Term;

@Recordable.FieldInternalNamePrefix("bex.spotlight.")
public class SearchSpotlight extends Modification<Term> {

    private static final String CLUSTER = "Search Spotlight";

    @ToolUi.Cluster(CLUSTER)
    private PromoOption promo;

    @Embedded
    @ToolUi.Cluster(CLUSTER)
    private List<SearchSpotlightSiteOverride> siteOverrides;

    public PromoOption getPromo() {
        return promo;
    }

    public void setPromo(PromoOption promo) {
        this.promo = promo;
    }

    public List<SearchSpotlightSiteOverride> getSiteOverrides() {
        if (siteOverrides == null) {
            siteOverrides = new ArrayList<>();
        }
        return siteOverrides;
    }

    public void setSiteOverrides(List<SearchSpotlightSiteOverride> siteOverrides) {
        this.siteOverrides = siteOverrides;
    }

    public Promo getPromoForSite(Site site) {
        if (site == null) {
            return promo.getPromoOptionPromo();
        }

        if (siteOverrides != null) {
            Optional<Promo> sitePromo = siteOverrides
                .stream()
                .filter(override -> override.getSite() != null)
                .filter(override -> override.getSite().equals(site))
                .map(SearchSpotlightSiteOverride::getPromo)
                .filter(Objects::nonNull)
                .map(PromoOption::getPromoOptionPromo)
                .findFirst();

            return sitePromo.orElse(Optional.ofNullable(getPromo()).map(PromoOption::getPromoOptionPromo).orElse(null));
        }

        return Optional.ofNullable(getPromo()).map(PromoOption::getPromoOptionPromo).orElse(null);
    }

    /**
     * Static helper method to return all spotlights for a site search.
     */
    public static List<Promo> getSpotlights(
        String queryString,
        Dictionary dictionary,
        Integer maxSpotlights,
        Site site) {
        if (ObjectUtils.isBlank(queryString)) {
            return new ArrayList<>();
        }
        List<Promo> spotlights = new ArrayList<>();
        List<Term> terms = SearchSpotlightTriggers.getTriggeredTerms(queryString);

        // add all the valid promos for this site.
        for (Term term : terms) {
            if (!term.getDictionaries().contains(dictionary)) {
                continue;
            }
            Promo promo = term.as(SearchSpotlight.class).getPromoForSite(site);

            if (promo != null) {
                spotlights.add(promo);
            }
        }
        spotlights = spotlights.stream().distinct().collect(Collectors.toList());

        if (maxSpotlights != null && spotlights.size() > maxSpotlights) {
            return spotlights.subList(0, Math.max(0, maxSpotlights));
        }

        return spotlights;
    }

}
