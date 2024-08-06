package brightspot.recipe;

import java.util.Optional;

import brightspot.ingredient.Ingredient;
import brightspot.ingredient.IngredientUnit;
import brightspot.rte.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.text.StringEscapeUtils;

@Recordable.Embedded
public class RecipeIngredient extends Record {

    @Required
    private String quantityString;

    private IngredientUnit unit;

    @Required
    private Ingredient ingredient;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String instruction;

    // --- Getters/setters ---

    public String getQuantityString() {
        return quantityString;
    }

    public void setQuantityString(String quantityString) {
        this.quantityString = quantityString;
    }

    public IngredientUnit getUnit() {
        return unit;
    }

    public void setUnit(IngredientUnit unit) {
        this.unit = unit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    /**
     * @return rich text
     */
    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    // --- API ---

    /**
     * @return rich text
     */
    public String toDescription() {
        StringBuilder label = new StringBuilder();

        Optional.ofNullable(getQuantityString())
            .filter(StringUtils::isNotBlank)
            .map(StringEscapeUtils::escapeHtml4)
            .map(q -> q + " ")
            .ifPresent(label::append);

        Optional.ofNullable(getUnit())
            .map(u -> Optional.ofNullable(getQuantity())
                .map(Number::doubleValue)
                .map(v -> v > 1.0)
                .orElse(false)
                ? u.asPlural() : u.getDisplayName())
            .map(StringEscapeUtils::escapeHtml4)
            .map(u -> u + " ")
            .ifPresent(label::append);

        Optional.ofNullable(getIngredient())
            .map(Ingredient::getName)
            .ifPresent(label::append);

        Optional.ofNullable(getInstruction())
            .map(i -> ", " + i)
            .ifPresent(label::append);

        return label.toString();
    }

    public Number getQuantity() {
        String q = getQuantityString();
        if (StringUtils.isBlank(q)) {
            return null;
        }

        try {
            return Double.parseDouble(q);

        } catch (NumberFormatException e) {
            try {
                return new FractionFormat().parse(q);
            } catch (MathParseException | NumberFormatException e2) {
                return null;
            }
        }
    }
}
