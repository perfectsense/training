package brightspot.core.classification;

import java.util.HashSet;
import java.util.Set;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

/**
 * A Modification that indexes all of an object's classification attributes. The modifications of the object that are
 * marked as a {@link ClassificationDataItem} provide a subset of classification attributes via {@link
 * ClassificationDataItem#getClassificationAttributes()}.
 */
@Recordable.FieldInternalNamePrefix(ClassificationObjectModification.FIELD_PREFIX)
public class ClassificationObjectModification extends Modification<Object> {

    public static final String FIELD_PREFIX = "classification.";

    public static final String CLASSIFICATION_ATTRIBUTES_INDEX = FIELD_PREFIX + "getAllClassificationAttributes";

    /**
     * Returns a set containing all of the classification attributes in each of the object's modifications that are
     * marked as a {@link ClassificationDataItem}.
     *
     * @return set of all the object's classification attributes
     */
    @Indexed
    @ToolUi.Hidden
    public Set<Recordable> getAllClassificationAttributes() {

        Set<Recordable> allClassificationAttributes = new HashSet<>();

        Recordable originalObject = ObjectUtils.to(Recordable.class, this.getOriginalObject());

        ObjectType type = originalObject.getState().getType();
        if (type == null) {
            return allClassificationAttributes;
        }

        Set<String> modificationClassNames = type.getModificationClassNames();

        for (String modificationClassName : modificationClassNames) {

            ObjectType modificationObjectType = ObjectType.getInstance(modificationClassName);
            Class<?> modificationClass = modificationObjectType.getObjectClass();

            if (modificationClass != null && ClassificationDataItem.class.isAssignableFrom(modificationClass)) {
                @SuppressWarnings("unchecked")
                Class<? extends ClassificationDataItem> clas = (Class<? extends ClassificationDataItem>) modificationClass;

                Set<Recordable> classificationAttributes = originalObject.as(clas).getClassificationAttributes();

                if (classificationAttributes != null) {
                    allClassificationAttributes.addAll(classificationAttributes);
                }
            }
        }

        return allClassificationAttributes;
    }
}
