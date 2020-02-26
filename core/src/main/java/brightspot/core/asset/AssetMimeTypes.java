package brightspot.core.asset;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.psddev.dari.db.ObjectType;

/**
 * Specifies the mime types provider for this asset type.
 */
@Documented
@ObjectType.AnnotationProcessorClass(AssetMimeTypesProcessor.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AssetMimeTypes {

    Class<? extends AssetMimeTypesProvider> value();
}
