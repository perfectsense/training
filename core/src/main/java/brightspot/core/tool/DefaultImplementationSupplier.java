package brightspot.core.tool;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.TypeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DefaultImplementationSupplier<T> {

    T createDefault();

    Logger LOGGER = LoggerFactory.getLogger(DefaultImplementationSupplier.class);

    LoadingCache<Class<?>, Collection<Class<?>>> SUPPLIER_CLASSES = CacheBuilder.newBuilder()
        .build(new CacheLoader<Class<?>, Collection<Class<?>>>() {

            @Override
            public Collection<Class<?>> load(Class<?> returnTypeClass) throws Exception {

                Set<Class<? extends DefaultImplementationSupplier>> concreteSupplierClasses = ClassFinder.findConcreteClasses(
                    DefaultImplementationSupplier.class);
                Set<Class<?>> thisElementSupplierClasses = concreteSupplierClasses
                    .stream()
                    .filter(concreteClass -> {
                        Class<?> supplierReturnTypeClass = TypeDefinition.getInstance(concreteClass)
                            .getInferredGenericTypeArgumentClass(DefaultImplementationSupplier.class, 0);
                        return supplierReturnTypeClass != null
                            && returnTypeClass.isAssignableFrom(supplierReturnTypeClass);
                    })
                    .collect(Collectors.toSet());

                // TODO: disambiguate using C3 linearization if any of these classes extend each other

                return thisElementSupplierClasses;
            }
        });

    /**
     * Return the result of the first default supplier with indicated type, falling back to supplied default.
     * @param returnTypeClass indicated object type class
     * @param defaultClass supplied default of object type
     * @param <T> object of indicated type
     * @return object, default or null
     */
    static <T> T createDefault(Class<T> returnTypeClass, Class<? extends T> defaultClass) {

        T supplierResult = suppliedValueOrNull(returnTypeClass);

        if (supplierResult != null) {
            return supplierResult;
        }

        if (defaultClass != null) {
            ObjectType defaultType = ObjectType.getInstance(defaultClass);

            if (defaultType != null) {
                @SuppressWarnings("unchecked")
                T obj = (T) defaultType.createObject(null);
                return obj;
            }
        }

        return null;
    }

    /**
     * Return the result of the first default supplier with indicated type, falling back to supplied default.
     * @param returnEnumClass indicated enum type class
     * @param defaultEnum supplied default of enum type
     * @param <T> enum of indicated type
     * @return enum or default
     */
    static <T extends Enum<T>> T createDefault(Class<? extends T> returnEnumClass, T defaultEnum) {

        T supplierResult = suppliedValueOrNull(returnEnumClass);

        return supplierResult != null
                ? supplierResult
                : defaultEnum;
    }

    /**
     * Return the result of the first supplier that can supply the indicated return type.
     * @param returnTypeClass indicated return type
     * @param <T> result of type
     * @return supplier result or null
     */
    static <T> T suppliedValueOrNull(Class<T> returnTypeClass) {

        Collection<Class<?>> supplierClasses = SUPPLIER_CLASSES.getUnchecked(returnTypeClass);

        for (Class<?> cls : supplierClasses) {
            if (DefaultImplementationSupplier.class.isAssignableFrom(cls)) {
                try {
                    @SuppressWarnings("unchecked")
                    DefaultImplementationSupplier<T> supplier = (DefaultImplementationSupplier<T>) cls.newInstance();
                    T supplierResult = supplier.createDefault();

                    if (supplierResult != null) {
                        return supplierResult;
                    }

                } catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.warn("Unable to instantiate " + cls.getName(), e);
                }
            }
        }

        return null;
    }
}
