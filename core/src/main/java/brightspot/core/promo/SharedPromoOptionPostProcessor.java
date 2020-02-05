package brightspot.core.promo;

import java.util.stream.Collectors;

import com.psddev.dari.db.Database;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;

// Temporary to work around a bug in Dari.
class SharedPromoOptionPostProcessor implements ObjectType.PostProcessor {

    @Override
    public void process(ObjectType type) {
        ObjectField moduleField = type.getField("module");

        if (moduleField != null) {
            moduleField.setPredicate(
                "moduleTypeObjectType = [ " + Database.Static.getDefault()
                    .getEnvironment()
                    .getTypesByGroup(Promo.class.getName())
                    .stream()
                    .map(t -> "'" + t.getId() + "'")
                    .collect(Collectors.joining(", ")) + " ]");
        }
    }
}
