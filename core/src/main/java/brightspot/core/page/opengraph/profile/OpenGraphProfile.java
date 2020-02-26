package brightspot.core.page.opengraph.profile;

import com.psddev.dari.db.Recordable;

/**
 * Marker Interface for OpenGraphProfileMetaViewModel. Add to a class to have it generate OpenGraph Profile tags.
 */
public interface OpenGraphProfile extends Recordable {

    String getOpenGraphUsername();

    String getOpenGraphFirstName();

    String getOpenGraphLastName();

    String getOpenGraphGender();
}
