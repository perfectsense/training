package brightspot.core.social;

import java.util.Optional;

public class TwitterSocialService extends AbstractSocialService {

    @Override
    public String getKey() {
        return "twitter";
    }

    @Override
    public String getUrl(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getTwitterUrl)
            .orElse(null);
    }

    @Override
    public String getUsername(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getTwitterUsername)
            .orElse(null);
    }

    @Override
    public String getDisplayName() {
        return "Twitter";
    }
}
