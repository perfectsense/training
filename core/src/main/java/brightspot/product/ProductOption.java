package brightspot.product;

import java.util.HashSet;
import java.util.Set;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
@ToolUi.ReadOnly
public class ProductOption extends Record {

    private String name;

    private Set<String> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getValues() {
        if (values == null) {
            values = new HashSet<>();
        }
        return values;
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }
}
