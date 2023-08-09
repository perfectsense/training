package brightspot.cascading;

import brightspot.util.DefaultImplementationSupplier;

public class DefaultCascadingStrategyImplementationSupplier
    implements DefaultImplementationSupplier<CascadingStrategy> {

    @Override
    public CascadingStrategy createDefault() {
        return new DefaultCascadingStrategy();
    }
}
