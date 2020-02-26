package brightspot.core.link;

import brightspot.core.tool.DirectoryItemUtils;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;

public interface Linkable extends Recordable {

    default String getLinkableUrl(Site site) {
        return DirectoryItemUtils.getCanonicalUrl(site, this);
    }

    String getLinkableText();
}
