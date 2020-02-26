package brightspot.core.tool;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.psddev.cms.tool.Search;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.RoutingFilter;

public abstract class AbstractSearchPage implements AutoArea {

    public abstract Search getSearch();

    public void initializeSearch(Search search) {

    }

    @Override
    public String getUrl() {

        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
        ServletContext servletContext = null;

        if (request != null) {
            servletContext = PageContextFilter.Static.getServletContext(request);
        }

        Search search = getSearch();
        initializeSearch(search);

        if (search.getLimit() == 0) {
            search.setLimit(10);
        }

        String url =
            RoutingFilter.Static.getApplicationPath("cms") + "/searchAdvancedFull" + "?search=" + ObjectUtils.toJson(
                search.getState().getSimpleValues());

        if (servletContext != null) {
            url = servletContext.getContextPath() + url;
        }

        return url;
    }
}
