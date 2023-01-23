package brightspot.homepage;

import java.util.Optional;

import brightspot.jsonld.actions.SearchAction;
import brightspot.page.AbstractPageViewModel;
import brightspot.search.NavigationSearchSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLd;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

@JsonLdType("WebPage")
public class HomepageViewModel extends AbstractPageViewModel<Homepage> implements PageEntryView {

    private static final String ACTION_NODE = "potentialAction";

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return createViews(PageViewMainField.class, model.getContent());
    }

    @Override
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }

    @Override
    public CharSequence getJsonLinkedData() {
        return Optional.ofNullable(JsonLd.createHtmlScriptBody(this))
            .map(RawHtml::of)
            .orElse(null);
    }

    @JsonLdNode(ACTION_NODE)
    public Object getAction() {
        Site site = getSite();
        SearchAction searchAction = Optional.ofNullable(SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(NavigationSearchSettings.class).getSearchPage()))
            .map(sp -> sp.getSearchAction(site))
            .map(url -> new SearchAction(url.toString()))
            .orElse(null);

        // Only add search action if search action configured
        if (searchAction == null) {
            return null;
        }

        return searchAction;
    }
}
