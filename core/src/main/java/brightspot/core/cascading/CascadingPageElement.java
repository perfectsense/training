package brightspot.core.cascading;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.psddev.dari.db.ObjectField;

/**
 * Apply this annotation to any {@link Cascading} field in {@link brightspot.core.page.CascadingPageData}.
 */
@ObjectField.AnnotationProcessorClass(CascadingPageElementAnnotationProcessor.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CascadingPageElement {

}
