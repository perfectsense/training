package brightspot.core.social;

import java.util.Optional;

public class LinkedInSocialService extends AbstractSocialService {

    @Override
    public String getKey() {
        return "linkedin";
    }

    @Override
    public String getUrl(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getLinkedInUrl)
            .orElse(null);
    }

    @Override
    public String getUsername(SocialEntityData data) {
        return Optional.ofNullable(data)
            .map(SocialEntityData::getLinkedInUsername)
            .orElse(null);
    }

    @Override
    public String getDisplayName() {
        return "LinkedIn";
    }

}
