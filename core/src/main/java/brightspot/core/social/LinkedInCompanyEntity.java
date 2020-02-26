package brightspot.core.social;

import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.Recordable;

/**
 * A type of {@link LinkedInEntity} that represenst a company.
 */
@Recordable.DisplayName("Company")
public class LinkedInCompanyEntity extends LinkedInEntity implements Interchangeable {

    @Override
    public String getBaseUrl() {
        return "https://www.linkedin.com/company/";
    }

    @Override
    public boolean loadTo(Object newObject) {
        if (newObject instanceof LinkedInUserEntity) {
            ((LinkedInUserEntity) newObject).setLinkedInUsername(this.getLinkedInUsername());
            return true;
        }
        return false;
    }
}
