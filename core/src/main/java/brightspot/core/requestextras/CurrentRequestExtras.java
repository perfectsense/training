package brightspot.core.requestextras;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessorClass;

/**
 * Annotation to retrieve a {@link RequestExtras} object in a ViewModel.
 */
@ServletViewRequestAnnotationProcessorClass(CurrentRequestExtrasAnnotationProcessor.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CurrentRequestExtras {

}
