package brightspot.core.social;

import java.util.Optional;

public class YoutubeSocialService extends AbstractSocialService {

    @Override
    public String getKey() {
        return "youtube";
    }

    @Override
    public String getUrl(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getYouTubeUrl)
            .orElse(null);
    }

    @Override
    public String getUsername(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getYouTubeUsername)
            .orElse(null);
    }

    @Override
    public String getDisplayName() {
        return "YouTube";
    }
}
