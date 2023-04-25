package brightspot.filteringrequests;

import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import brightspot.link.Link;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.page.PageRequest;
import com.psddev.dari.db.ApplicationFilter;
import com.psddev.dari.util.AbstractFilter;
import com.psddev.dari.web.WebRequest;

public class RequestRedirectFilter extends AbstractFilter implements AbstractFilter.Auto {

    @Override
    protected void doRequest(
        HttpServletRequest request,
        HttpServletResponse response, FilterChain chain) throws Exception {
        String servletPath = request.getServletPath();
        Site currentSite = WebRequest.getCurrent()
            .as(PageRequest.class)
            .getCurrentSite();
        String regex =
            SiteSettings.get(currentSite, s ->
                s.as(RequestRedirectSiteSettingsModification.class)
                    .getRegex());
        Boolean isFilterEnabled =
            SiteSettings.get(currentSite, s ->
                s.as(RequestRedirectSiteSettingsModification.class)
                    .getFilterEnabled());

        if (!isFilterEnabled) {
            chain.doFilter(request, response);
        } else {
            boolean servletPathMatchesRegex = request.getServletPath()
                .matches(SiteSettings.get(currentSite, s ->
                    s.as(RequestRedirectSiteSettingsModification.class)
                        .getRegex()));
            if (servletPathMatchesRegex) {
                Link redirectPath = SiteSettings.get(currentSite, s ->
                    s.as(RequestRedirectSiteSettingsModification.class)
                        .getRedirectTo());
                response.sendRedirect(redirectPath.getLinkUrl(currentSite));
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    /**
     * Runs before ApplicationFilter.
     */
    @Override
    public void updateDependencies(
        Class<? extends AbstractFilter> filterClass,
        List<Class<? extends Filter>> dependencies) {

        if (ApplicationFilter.class.isAssignableFrom(filterClass)) {
            dependencies.add(getClass());
        }
    }
}
