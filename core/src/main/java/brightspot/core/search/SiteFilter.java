package brightspot.core.search;

import com.psddev.cms.db.Site;

public class SiteFilter extends FieldFilter {

    @Override
    public String getField() {
        return "cms.site.owner";
    }

    @Override
    public String getDefaultHeading() {
        return "Site";
    }

    @Override
    public String getItemLabel(Object object) {
        return object instanceof Site ? ((Site) object).getName() : null;
    }
}
