package brightspot.core.page;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessorClass;

/**
 * True if the current HTTP Request is a CMS Preview
 */
@ServletViewRequestAnnotationProcessorClass(HttpPreviewProcessor.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HttpPreview {

}
