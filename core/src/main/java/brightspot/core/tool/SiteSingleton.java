package brightspot.core.tool;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

/**
 * A site specific singleton object on a enforced by the {@link com.psddev.cms.db.Site.ObjectModification#getOwner()
 * site owner} associated with the object.
 */
public interface SiteSingleton extends Recordable {

    /**
     * Gets the site singleton for the specified type and site.
     *
     * @param singletonClass the return type for the singleton.
     * @param siteOwner the owner site for the singleton.
     * @param <T> the
     * @return the site singleton for the given site owner.
     */
    static <T extends SiteSingleton> T get(Class<T> singletonClass, Site siteOwner) {

        if (singletonClass != null) {
            return Query.from(singletonClass)
                .where(
                    SiteSingletonData.KEY_FIELD + " = ?",
                    singletonClass.getName() + "/" + (siteOwner != null ? siteOwner.getId() : ""))
                .first();
        }

        return null;
    }
}
