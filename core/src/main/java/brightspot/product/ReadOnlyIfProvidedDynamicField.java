package brightspot.product;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicField;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.Recordable;

public class ReadOnlyIfProvidedDynamicField implements DynamicField {

    @Override
    public void update(Recordable recordable, ObjectField field) {
        if (recordable instanceof ProductVariant) {
            ProductVariant productVariant = (ProductVariant) recordable;
            if (productVariant.getDataProvider() != null) {
                field.as(ToolUi.class).setReadOnly(true);
            }
        } else if (recordable instanceof SingleProductVariantType) {
            if (((SingleProductVariantType) recordable).getDataProvider() != null) {
                field.as(ToolUi.class).setReadOnly(true);
            }
        }
    }
}
