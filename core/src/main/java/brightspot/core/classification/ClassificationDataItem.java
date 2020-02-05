package brightspot.core.classification;

import java.util.Set;

import com.psddev.dari.db.Recordable;

/**
 * Marker interface for modifications of an object. The {@link ClassificationDataItem#getClassificationAttributes()}
 * will be indexed as part of the object's {@link ClassificationObjectModification#getAllClassificationAttributes()}.
 */
public interface ClassificationDataItem extends Recordable {

    Set<Recordable> getClassificationAttributes();
}
