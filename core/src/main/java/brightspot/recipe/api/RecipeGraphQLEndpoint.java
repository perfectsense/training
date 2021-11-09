package brightspot.recipe.api;

import java.util.Collections;
import java.util.List;

import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Singleton;
import com.psddev.graphql.cda.ContentDeliveryApiEndpoint;
import com.psddev.graphql.cda.ContentDeliveryEntryPointField;

/**
 * GraphQL endpoint supporting querying for {@link brightspot.recipe.Recipe} by various fields.
 *
 * @see RecipeGraphQLEndpointViewModel
 */

@Recordable.DisplayName("Recipes (GraphQL)")
public class RecipeGraphQLEndpoint extends ContentDeliveryApiEndpoint implements
    Singleton {

    // --- ContentDeliveryApiEndpoint support ---

    @Override
    public String getFullPath() {
        return "/api/recipes";
    }

    // --- ContentDeliveryApiSchemaSettings support ---

    @Override
    public List<ContentDeliveryEntryPointField> getQueryEntryFields() {
        return Collections.singletonList(new ContentDeliveryEntryPointField(
            RecipeGraphQLEndpointViewModel.class,
            "Recipes",
            null));
    }
}
