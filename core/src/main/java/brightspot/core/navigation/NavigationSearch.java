package brightspot.core.navigation;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;

public interface NavigationSearch extends Recordable {

    CharSequence getSearchAction(Site site);
}
