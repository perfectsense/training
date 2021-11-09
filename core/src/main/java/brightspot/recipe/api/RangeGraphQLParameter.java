package brightspot.recipe.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import com.psddev.cms.api.ApiRequest;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.graphql.SchemaInputType;
import com.psddev.graphql.SchemaInputTypes;
import com.psddev.graphql.cda.annotation.ContentDeliveryApiWebAnnotationProcessor;

/**
 * Custom GraphQL API parameter supporting numeric range arguments in GraphQL queries.
 *
 * @see RangeParameter
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RangeGraphQLParameter {

}

class RangeParameterTypeProcessor implements ContentDeliveryApiWebAnnotationProcessor<RangeGraphQLParameter> {

    @Override
    public RangeParameter getValue(ApiRequest request, Object input, Field field, RangeGraphQLParameter annotation) {
        if (input == null) {
            return null;
        }

        // input is expected to be a Map
        Integer min = ObjectUtils.to(Integer.class, CollectionUtils.getByPath(input, "min"));
        Integer max = ObjectUtils.to(Integer.class, CollectionUtils.getByPath(input, "max"));

        return new RangeParameter(min, max);
    }

    @Override
    public SchemaInputType getInputType(Field field, RangeGraphQLParameter annotation) {
        return SchemaInputTypes.newInputObject("Range")
            .field("min", SchemaInputTypes.INT)
            .field("max", SchemaInputTypes.INT)
            .build();
    }
}
