package brightspot.promo.page;

import java.util.Optional;
import java.util.function.Function;

import brightspot.image.WebImageAsset;
import brightspot.link.Link;
import brightspot.page.PageElementSupplier;
import brightspot.sponsoredcontent.ContentSponsor;
import brightspot.sponsoredcontent.HasSponsor;
import brightspot.sponsoredcontent.Sponsor;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;

public class PagePromoSponsorContextSupplier implements PageElementSupplier<PagePromoSponsorContext> {

    @Override
    public Iterable<PagePromoSponsorContext> get(Site site, Object object) {

        Sponsor sponsor = Optional.ofNullable(object)
                .filter(HasSponsor.class::isInstance)
                .map(HasSponsor.class::cast)
                .map(HasSponsor::getSponsor)
                .map(contentSponsor -> contentSponsor.as(Sponsor.class))
                .orElse(null);

        if (sponsor == null) {
            return null;
        }

        WebImageAsset logo = sponsor.getLogo();
        Link callToAction = sponsor.getCallToAction();
        Function<Recordable, String> displayNameFunction = parentObject -> parentObject.as(ContentSponsor.class)
                .getDisplayName();

        PagePromoSponsorContext pagePromoSponsorContext = new PagePromoSponsorContext();
        pagePromoSponsorContext.setParentObject(sponsor);
        pagePromoSponsorContext.setLogo(logo);
        pagePromoSponsorContext.setCallToAction(callToAction);
        pagePromoSponsorContext.setDisplayNameFunction(displayNameFunction);

        return ImmutableList.of(pagePromoSponsorContext);

    }

}
