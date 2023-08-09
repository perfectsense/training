package brightspot.module.list.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import brightspot.product.Product;
import brightspot.query.QueryBuilder;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.CompoundPredicate;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
@Recordable.DisplayName("Price Range")
public class ProductPriceRangeMatch extends Record implements QueryBuilder {

    @Required
    private ProductPriceRangeMatchType filterPrice = ProductPriceRangeMatchType.MINIMUM_PRODUCT_PRICE;

    private Double min;

    private Double max;

    public ProductPriceRangeMatchType getFilterPrice() {
        return Optional.ofNullable(filterPrice).orElse(ProductPriceRangeMatchType.MINIMUM_PRODUCT_PRICE);
    }

    public void setFilterPrice(ProductPriceRangeMatchType filterPrice) {
        this.filterPrice = filterPrice;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    // --- QueryBuilder support ---

    @Override
    public Predicate toPredicate(Site site, Object mainContent) {
        if (min == null && max == null) {
            return null;
        }
        String field = getFilterPrice().equals(ProductPriceRangeMatchType.MINIMUM_PRODUCT_PRICE)
            ? Product.PRICE_MIN_FIELD : Product.PRICE_MAX_FIELD;

        List<Predicate> predicates = new ArrayList<>();
        if (min != null) {
            predicates.add(PredicateParser.Static.parse(
                field + " >= ?",
                min));
        }
        if (max != null) {
            predicates.add(PredicateParser.Static.parse(
                field + " <= ?",
                max));
        }
        return new CompoundPredicate(PredicateParser.AND_OPERATOR, predicates);
    }

    @Override
    public String getDescription() {
        if (min == null && max == null) {
            return "";
        }
        if (min != null && max != null) {
            return "Price between " + min + " and " + max;
        } else if (min != null) {
            return "Price greater than or equal to " + min;
        } else {
            return "Price less than or equal to " + max;
        }
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getDescription();
    }
}
