package brightspot.util;

import java.util.Map;
import java.util.Optional;

import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.db.StateValueAdapter;
import com.psddev.dari.util.TypeDefinition;

/**
 * StateValueAdapter which automatically converts Records of moved types.
 *
 * It DOES NOT handle any changes to the type other than moving from one package to another.
 *
 * Like all StateValueAdapters, it works only with embedded Records.
 *
 * @param <S> the type at the old location
 * @param <T> the moved type
 */
public abstract class TypeStateValueAdapter<S extends Recordable, T extends Recordable> implements StateValueAdapter<S, T> {

    @Override
    public final T adapt(S source) {
        State sourceState = source.getState();

        Map<String, Object> simpleValues = sourceState.getSimpleValues();
        simpleValues.remove(State.TYPE_KEY); // don't overwrite the target's type ID

        @SuppressWarnings("unchecked")
        T target = (T) ObjectType.getInstance(getTargetClass())
                .createObject(sourceState.getId());

        target.getState().setValues(simpleValues);

        return target;
    }

    protected Class<T> getTargetClass() {
        @SuppressWarnings("unchecked")
        Class<T> c = (Class<T>) Optional.of(TypeDefinition.getInstance(getClass()))
                .map(td -> td.getInferredGenericTypeArgumentClass(TypeStateValueAdapter.class, 1))
                .orElseThrow(() -> new IllegalArgumentException("Cannot find target class for " + getClass().getName()));
        return c;
    }
}
