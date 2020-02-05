package brightspot.core.seo;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psddev.cms.db.PageFilter;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.dari.util.RoutingFilter;
import com.psddev.dari.util.StringUtils;

@RoutingFilter.Path(application = "", value = "/robots.txt")
public class RobotsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Site site = PageFilter.Static.getSite(request);
        String robotsText = SiteSettings.get(site, f -> f.as(SeoSettingsModification.class).getRobotsTxt());

        if (StringUtils.isBlank(robotsText)) {
            robotsText = SeoSettingsModification.DEFAULT_ROBOTS_TEXT;
        }

        response.getWriter().write(robotsText);
    }
}
