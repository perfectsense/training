package brightspot.core.social;

import com.psddev.dari.db.Modification;

public class SocialServiceModification extends Modification<SocialService> {

    @Indexed
    public String getServiceName() {
        return getOriginalObject().getKey();
    }

}
