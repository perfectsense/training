package brightspot.core.social;

import com.psddev.dari.db.Recordable;

/**
 * Marker interface for content that is considered a "Social" entity and that would need all or some of the fields in
 * {@link SocialEntityData}.
 */
public interface SocialEntity extends Recordable {

    default SocialEntityData asSocialEntityData() {
        return this.as(SocialEntityData.class);
    }
}
