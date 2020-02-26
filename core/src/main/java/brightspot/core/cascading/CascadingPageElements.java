package brightspot.core.cascading;

import com.psddev.dari.db.Recordable;

/**
 * Modify this interface to add {@link Cascading} page elements that can be overridden with the selected {@link
 * CascadingStrategy}.
 */
public interface CascadingPageElements extends Recordable {

    default CascadingPageElementsModification asCascadingPageElementsModification() {
        return as(CascadingPageElementsModification.class);
    }

    default Recordable getCascadingPageElementsContainer() {
        return this;
    }
}
