package brightspot.entitlements.zephr;

import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;

/**
 * Processor for {@link ZephrContentApiCustomVariable} annotations to mark ObjectField
 * {@link ZephrContentApiCustomVariableObjectFieldModification metadata}.
 */
public class ZephrContentApiCustomVariableProcessor
    implements ObjectField.AnnotationProcessor<ZephrContentApiCustomVariable> {

    @Override
    public void process(
        ObjectType type, ObjectField field, ZephrContentApiCustomVariable annotation) {
        field.as(ZephrContentApiCustomVariableObjectFieldModification.class).setZephrCustomVariable(annotation.value());
    }
}
