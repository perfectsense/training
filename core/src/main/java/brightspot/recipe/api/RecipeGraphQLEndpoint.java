package brightspot.recipe.api;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Singleton;
import com.psddev.graphql.CustomGraphQLCorsConfiguration;
import com.psddev.graphql.GraphQLCorsConfiguration;
import com.psddev.graphql.cda.ContentDeliveryApiAccessOption;
import com.psddev.graphql.cda.ContentDeliveryApiAccessOptionExplicit;
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
    private ContentDeliveryApiAccessOption accessOption = new ContentDeliveryApiAccessOptionExplicit();

    @Recordable.DisplayName("CORS Configuration")
    private CustomGraphQLCorsConfiguration corsConfiguration = new CustomGraphQLCorsConfiguration();

    @Override // ContentDeliveryApiEndpoint
    public ContentDeliveryApiAccessOption getAccessOption() {
        if (accessOption == null) {
            accessOption = new ContentDeliveryApiAccessOptionExplicit();
        }
        return accessOption;
    }

    public void setAccessOption(ContentDeliveryApiAccessOption accessOption) {
        this.accessOption = accessOption;
    }

    public CustomGraphQLCorsConfiguration getCorsConfiguration() {
        return corsConfiguration;
    }

    public void setCorsConfiguration(CustomGraphQLCorsConfiguration corsConfiguration) {
        this.corsConfiguration = corsConfiguration;
    }

    // --- ContentDeliveryApiEndpoint support ---

    @Override
    protected String getPathSuffix() {
        return "/recipes";
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
