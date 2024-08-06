package brightspot.ingredient;

import com.psddev.dari.util.DariUtils;

public enum IngredientUnit {

    BAG("bag", null),
    BAR_SPOON("bar spoon", null),
    BOTTLE("bottle", null),
    BRANCH("branch", "branches"),
    BUNCH("bunch", "bunches"),
    CAN("can", null),
    CLOVE("clove", null),
    CONTAINER("container", null),
    CUP("cup", null),
    DASH("dash", "dashes"),
    DOZEN("dozen", "dozen"),
    DROP("drop", null),
    ENVELOPE("envelope", null),
    GRAM("gram", null),
    HEAD("head", null),
    JAR("jar", null),
    LEAF("leaf", "leaves"),
    OUNCE("ounce", null),
    PACKAGE("package", null),
    PINCH("pinch", "pinches"),
    PINT("pint", null),
    POUND("pound", null),
    QUART("quart", null),
    SHEET("sheet", null),
    SLICE("slice", null),
    SPRIG("sprig", null),
    STICK("stick", null),
    TABLESPOON("tablespoon", null),
    TABLET("tablet", null),
    TEASPOON("teaspoon", null);

    private final String displayName;
    private final String plural;

    IngredientUnit(String displayName, String plural) {
        this.displayName = displayName;
        this.plural = plural;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String asPlural() {
        return plural != null ? plural : getDisplayName() + "s";
    }

    @Override
    public String toString() {
        return DariUtils.toLabel(name());
    }
}
