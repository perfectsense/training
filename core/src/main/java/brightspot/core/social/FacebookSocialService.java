package brightspot.core.social;

import java.util.Optional;

public class FacebookSocialService extends AbstractSocialService {

    @Override
    public String getKey() {
        return "facebook";
    }

    @Override
    public String getUrl(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getFacebookUrl)
            .orElse(null);
    }

    @Override
    public String getUsername(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getFacebookUsername)
            .orElse(null);
    }

    @Override
    public String getDisplayName() {
        return "Facebook";
    }
}
