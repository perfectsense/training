package brightspot.core.page;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessorClass;

/**
 * Similar to {@link com.psddev.cms.view.servlet.HttpParameter}, but the object returned is an {@link IdParameterMap}.
 * This allows multiple modules on a page to read their own versions of parameters for activities such as pagination.
 *
 * All parameters matching the pattern [UUID]-[parameterName] will be returned in a {@link IdParameterMap}.
 */
@ServletViewRequestAnnotationProcessorClass(HttpIdPrefixedParametersProcessor.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HttpIdPrefixedParameters {

    String value() default "";
}
