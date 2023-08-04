package brightspot.module.list.product;

public enum ProductPriceRangeMatchType {

    MINIMUM_PRODUCT_PRICE("Filter On Product's Lowest Price"),
    MAXIMUM_PRODUCT_PRICE("Filter On Product's Highest Price");

    private String displayName;

    ProductPriceRangeMatchType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
