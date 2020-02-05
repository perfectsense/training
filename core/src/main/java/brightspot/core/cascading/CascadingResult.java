package brightspot.core.cascading;

import java.util.Optional;
import java.util.function.Function;

public final class CascadingResult<T> {

    private final Object provider;
    private final Object providerOriginalObject;
    private final String appendNote;

    private transient Cascading<T> result;
    private transient Function<Object, Cascading<T>> getter;

    public CascadingResult(Object provider, Object providerOriginalObject, String appendNote) {
        this.provider = provider;
        this.providerOriginalObject = providerOriginalObject;
        this.appendNote = appendNote;
    }

    public CascadingResult(Object provider, Object providerOriginalObject) {
        this.provider = provider;
        this.providerOriginalObject = providerOriginalObject;
        this.appendNote = null;
    }

    public Optional<Object> getProvider() {
        return Optional.ofNullable(provider);
    }

    public Optional<Object> getProviderOriginalObject() {
        return Optional.ofNullable(providerOriginalObject);
    }

    public String getAppendNote() {
        return appendNote;
    }

    public Cascading<T> getResult() {
        if (provider == null) {
            return null;
        }
        if (getter == null) {
            throw new IllegalStateException("Getter has not been initialized!");
        }
        if (result == null) {
            result = getter.apply(provider);
        }
        return result;
    }

    public Cascading<T> applyAndGetResult(Function<Object, Cascading<T>> getter) {
        setGetter(getter);
        return getResult();
    }

    public void setGetter(Function<Object, Cascading<T>> getter) {
        if (this.getter != getter) {
            this.result = null;
        }
        this.getter = getter;
    }

    public CascadingResult<T> apply(Function<Object, Cascading<T>> getter) {
        applyAndGetResult(getter);
        return this;
    }
}
