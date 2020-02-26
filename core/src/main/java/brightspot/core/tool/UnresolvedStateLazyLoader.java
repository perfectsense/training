package brightspot.core.tool;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.State;
import com.psddev.dari.db.StateSerializer;
import com.psddev.dari.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * See: {@link StateUtils#resolveAndGet(Object, java.util.function.Function)}.
 */
class UnresolvedStateLazyLoader extends State.Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnresolvedStateLazyLoader.class);

    @Override
    public void beforeFieldGet(State state, String name) {
        String internalName = name;
        ObjectType type = state.getType();

        if (type != null) {
            ObjectField field = type.getField(name);

            if (field == null) {
                List<ObjectField> matchingFields = type.getFields().stream()
                    .filter(f -> name.equals(f.getJavaFieldName()))
                    .filter(f -> ObjectField.RECORD_TYPE.equals(f.getInternalItemType()))
                    .collect(Collectors.toList());

                if (!matchingFields.isEmpty()) {
                    if (matchingFields.size() > 1) {
                        LOGGER.debug(
                            "More than one field matches the java field name " + name + " in " + type.getInternalName()
                                + ". UnresolvedStateLazyLoader cannot be used.");
                        return;
                    }
                    field = matchingFields.get(0);
                }
            }

            if (field != null) {
                if (!ObjectField.RECORD_TYPE.equals(field.getInternalItemType())) {
                    return;
                }
                internalName = field.getInternalName();
            }
        }

        Object value = state.getRawValue(internalName);
        if (value instanceof Map && ((Map) value).containsKey(StateSerializer.REFERENCE_KEY)) {
            UUID id = ObjectUtils.to(UUID.class, ((Map) value).get(StateSerializer.REFERENCE_KEY));
            Object obj = Query.findById(Record.class, id);
            state.put(internalName, obj);
        } else if (value instanceof Record && ((Record) value).getState().isReferenceOnly()) {
            UUID id = ((Record) value).getId();
            Object obj = Query.findById(Record.class, id);
            state.put(internalName, obj);
        }
    }
}
