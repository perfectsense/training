package brightspot.apple.news.v2.displaycontrol;

import java.util.Set;

import brightspot.apple.news.v2.distribution.AppleNewsDistributionJobSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.DatabaseEnvironment;
import com.psddev.dari.util.ClassFilter;
import com.psddev.dari.web.WebRequest;

class AppleNewsClassFilter implements ClassFilter {

    @Override
    public <T> Set<Class<? extends T>> filter(Class<T> baseClass, Set<Class<? extends T>> classes) {
        //Only hide for UI
        if (Database.isAvailable()
            && DatabaseEnvironment.getCurrent().isEveryTypeAvailable()
            && WebRequest.isAvailable()) {

            Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
            if (!SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(AppleNewsDisplaySettings.class).isAppleNewsEnabled())) {
                classes.removeIf(c -> c.getPackage().getName().startsWith("brightspot.apple.news")
                    && !c.getName().startsWith("brightspot.apple.news.v2.displaycontrol")
                    && !AppleNewsDistributionJobSettings.class.equals(c));
            }
        }
        return classes;
    }
}

