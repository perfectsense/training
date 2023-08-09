package brightspot.product;

import java.util.List;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class ProductVariantType extends Record {

    public abstract List<ProductVariant> toVariants();

    public abstract void fromVariants(List<ProductVariant> fromVariants);
}
