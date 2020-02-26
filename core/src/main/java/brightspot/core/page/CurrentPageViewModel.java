package brightspot.core.page;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessorClass;

/**
 * Annotation to create a {@link PageViewModel} object (or other extendable ViewModel) in a ViewModel.
 */
@ServletViewRequestAnnotationProcessorClass(CurrentPageViewModelAnnotationProcessor.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CurrentPageViewModel {

    Class value() default PageViewModel.class;

}
