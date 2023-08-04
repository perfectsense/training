package brightspot.promo.page;

import java.util.stream.Collectors;

import brightspot.author.AuthoringPageViewModel;
import brightspot.page.PageElementSupplier;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Site;

public class PagePromoAuthorContextSupplier implements PageElementSupplier<PagePromoAuthorContext> {

    @Override
    public Iterable<PagePromoAuthorContext> get(Site site, Object object) {

        PagePromoAuthorContext context = new PagePromoAuthorContext();
        context.setBylineAuthorImage(AuthoringPageViewModel.getPrimaryAuthorImage(object));
        context.setBylineLinkUrl(AuthoringPageViewModel.getPrimaryAuthorUrl(site, object));
        context.setBylineText(AuthoringPageViewModel.getPrimaryAuthorName(object));
        context.setAuthors(
            AuthoringPageViewModel.getAuthors(object)
                .stream()
                .map(author -> new PagePromoAuthor(
                    author.getName(),
                    AuthoringPageViewModel.getAuthorUrl(site, author),
                    author.getImage()))
                .collect(Collectors.toList())
        );

        return ImmutableList.of(context);
    }
}
