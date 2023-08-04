package brightspot.entitlements.zephr;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.psddev.dari.db.ObjectField;

/**
 * An annotation to mark a content model field as something that should be included in the {@link ZephrContentApi}
 * response. Intended primarily to be used by Editorial Content Types to extend data passed in API.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ObjectField.AnnotationProcessorClass(ZephrContentApiCustomVariableProcessor.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ZephrContentApiCustomVariable {

    boolean value() default true;
}
