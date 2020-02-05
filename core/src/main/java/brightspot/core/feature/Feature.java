package brightspot.core.feature;

import java.util.HashSet;
import java.util.Optional;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.State;

/**
 * All concrete implementations of this class will be used to power asset-level features disabling, via {@link
 * FeatureSettings}.
 *
 * This provides for a centralized/consistent way of doing this, to replace the typical technique in the past of adding
 * feature-specific (e.g., "Disable Comments") checkbox toggles to the tool UI via one-off object modifications.
 */
public abstract class Feature extends Modification<SiteSettings> {

    protected boolean isEnabled(Object object) {
        return true;
    }

    public static boolean isDisabled(Class<? extends Feature> featureClass, Object object, Site site) {
        if (featureClass == null || object == null) {
            return true;
        }

        boolean disabled = Optional.ofNullable(State.getInstance(object))
            .map(state -> state.as(FeatureSettings.class).getDisabledFeatures())
            .orElseGet(HashSet::new)
            .contains(ObjectType.getInstance(featureClass));

        if (!disabled) {
            disabled = !SiteSettings.get(site, s -> s.as(featureClass).isEnabled(object));
        }

        return disabled;
    }
}
