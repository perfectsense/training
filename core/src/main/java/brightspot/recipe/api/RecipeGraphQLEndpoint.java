package brightspot.recipe.api;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Singleton;
import com.psddev.graphql.CustomGraphQLCorsConfiguration;
import com.psddev.graphql.GraphQLApiAccessOption;
import com.psddev.graphql.GraphQLApiAccessOptionExplicit;
import com.psddev.graphql.GraphQLCorsConfiguration;
import com.psddev.graphql.cda.ContentDeliveryApiEndpoint;
import com.psddev.graphql.cda.ContentDeliveryApiThemeable;
import com.psddev.graphql.cda.ContentDeliveryEntryPointField;

/**
 * GraphQL endpoint supporting querying for {@link brightspot.recipe.Recipe} by various fields.
 *
 * @see RecipeGraphQLEndpointViewModel
 */

@Recordable.DisplayName("Recipes (GraphQL)")
public class RecipeGraphQLEndpoint extends ContentDeliveryApiEndpoint implements
    ContentDeliveryApiThemeable,
    Singleton {

    @Recordable.DisplayName("Access")
    private GraphQLApiAccessOption apiAccessOption = new GraphQLApiAccessOptionExplicit();

    @Recordable.DisplayName("CORS Configuration")
    private CustomGraphQLCorsConfiguration corsConfiguration = new CustomGraphQLCorsConfiguration();

    @Override // ContentDeliveryApiEndpoint
    public GraphQLApiAccessOption getApiAccessOption() {
        return apiAccessOption;
    }

    public void setApiAccessOption(GraphQLApiAccessOption apiAccessOption) {
        this.apiAccessOption = apiAccessOption;
    }

    public CustomGraphQLCorsConfiguration getCorsConfiguration() {
        return corsConfiguration;
    }

    public void setCorsConfiguration(CustomGraphQLCorsConfiguration corsConfiguration) {
        this.corsConfiguration = corsConfiguration;
    }

    // --- ContentDeliveryApiEndpoint support ---

    @Override
    public String getFullPath() {
        return "/api/recipes";
    }

    @Override
    protected void updateCorsConfiguration(GraphQLCorsConfiguration corsConfiguration) {
        super.updateCorsConfiguration(corsConfiguration);

        Optional.ofNullable(getCorsConfiguration())
            .map(CustomGraphQLCorsConfiguration::getAllowedOrigins)
            .ifPresent(o -> o.forEach(corsConfiguration::addAllowedOrigin));

        Optional.ofNullable(getCorsConfiguration())
            .map(CustomGraphQLCorsConfiguration::getAllowedHeaders)
            .ifPresent(h -> h.forEach(corsConfiguration::addAllowedHeader));
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
