package brightspot.core.footer;

import com.psddev.dari.db.Recordable;

/**
 * Marker interface for classes that are only able to be published through ${@link FooterShared} and will not be
 * included in ${@link brightspot.core.module.Shared}.
 */
public interface FooterModuleTypeOnly extends Recordable {

}
