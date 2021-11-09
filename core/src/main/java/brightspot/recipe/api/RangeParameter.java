package brightspot.recipe.api;

import com.psddev.dari.db.Query;

/**
 * Values from a GraphQL query numeric range argument.
 *
 * @see RangeGraphQLParameter
 */
public class RangeParameter {

    private final Integer min;
    private final Integer max;

    public RangeParameter(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public void updateQuery(Query<?> query, String field) {
        if (min != null) {
            query.and(field + " >= ?", min);
        }

        if (max != null) {
            query.and(field + " <= ?", max);
        }
    }
}
