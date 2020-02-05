package brightspot.core.social;

import java.util.Optional;

public class PinterestSocialService extends AbstractSocialService {

    @Override
    public String getKey() {
        return "pinterest";
    }

    @Override
    public String getUrl(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getPinterestUrl)
            .orElse(null);
    }

    @Override
    public String getUsername(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getPinterestUsername)
            .orElse(null);
    }

    @Override
    public String getDisplayName() {
        return "Pinterest";
    }
}
