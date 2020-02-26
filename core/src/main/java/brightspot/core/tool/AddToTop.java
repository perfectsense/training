package brightspot.core.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;

/**
 * @deprecated Use {@link com.psddev.cms.db.ToolUi.AddToTop} instead.
 */
@Deprecated
@ObjectField.AnnotationProcessorClass(AddToTop.AddToTopProcessor.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AddToTop {

    boolean value() default true;

    class AddToTopProcessor implements ObjectField.AnnotationProcessor<AddToTop> {

        @Override
        public void process(ObjectType type, ObjectField field, AddToTop annotation) {
            field.as(FieldData.class).setAddToTop(annotation.value());
        }
    }

    class FieldData extends Modification<ObjectField> {

        boolean addToTop;

        public boolean isAddToTop() {
            return addToTop;
        }

        public void setAddToTop(boolean addToTop) {
            this.addToTop = addToTop;
        }
    }
}
