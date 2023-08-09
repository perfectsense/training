package brightspot.product;

import java.util.Optional;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class ProductVariantOption extends Record {

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getLabel() {
        return Optional.ofNullable(name).orElse("") + " -> " + Optional.ofNullable(value).orElse("");
    }
}
