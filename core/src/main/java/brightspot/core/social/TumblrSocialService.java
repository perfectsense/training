package brightspot.core.social;

import java.util.Optional;

public class TumblrSocialService extends AbstractSocialService {

    @Override
    public String getKey() {
        return "tumblr";
    }

    @Override
    public String getUrl(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getTumblrUrl)
            .orElse(null);
    }

    @Override
    public String getUsername(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getTumblrUsername)
            .orElse(null);
    }

    @Override
    public String getDisplayName() {
        return "Tumblr";
    }
}
