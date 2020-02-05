package brightspot.core.page;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessorClass;

/**
 * Access the entire HTTP query string for use by {@link com.psddev.dari.util.StringUtils#addQueryParameters}.
 */
@ServletViewRequestAnnotationProcessorClass(HttpQueryStringProcessor.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HttpQueryString {

}
