package brightspot.core.social;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.styleguide.core.social.SocialLinkView;

public interface SocialService extends Recordable {

    String getKey();

    String getUrl(SocialEntityData data);

    String getUsername(SocialEntityData data);

    String getDisplayName();

    default SocialLinkView toView(SocialEntityData data) {
        String url = getUrl(data);

        if (url == null) {
            return null;
        }

        return new SocialLinkView.Builder()
            .href(url)
            .socialService(getKey())
            .body(getUsername(data))
            .build();
    }

    static SocialService getServiceByName(String serviceName) {
        return Query.from(SocialService.class).where("getServiceName = ?", serviceName).first();
    }
}
