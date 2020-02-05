package brightspot.core.cascading;

/**
 * Top level interface to contain assets that can be managed via a selected {@link CascadingStrategy}.
 */
public interface Cascading<T> {

    T get();

}
