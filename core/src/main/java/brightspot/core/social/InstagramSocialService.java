package brightspot.core.social;

import java.util.Optional;

public class InstagramSocialService extends AbstractSocialService {

    @Override
    public String getKey() {
        return "instagram";
    }

    @Override
    public String getUrl(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getInstagramUrl)
            .orElse(null);
    }

    @Override
    public String getUsername(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getInstagramUsername)
            .orElse(null);
    }

    @Override
    public String getDisplayName() {
        return "Instagram";
    }
}
