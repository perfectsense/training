package brightspot.core.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.psddev.cms.db.Site;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.TypeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface PageElementSupplier<T> {

    Logger LOGGER = LoggerFactory.getLogger(PageElementSupplier.class);

    /**
     * Return an Iterable of the indicated type.
     *
     * @param site the current Site
     * @param object the current main object
     * @return iterable of the page element
     */
    Iterable<T> get(Site site, Object object);

    LoadingCache<Class<?>, Collection<Class<?>>> SUPPLIER_CLASSES = CacheBuilder.newBuilder()
        .build(new CacheLoader<Class<?>, Collection<Class<?>>>() {

            @Override
            public Collection<Class<?>> load(Class<?> returnTypeClass) throws Exception {

                Set<Class<? extends PageElementSupplier>> concreteSupplierClasses = ClassFinder.findConcreteClasses(
                    PageElementSupplier.class);
                Set<Class<?>> thisElementSupplierClasses = concreteSupplierClasses
                    .stream()
                    .filter(concreteClass -> {
                        Class<?> supplierReturnTypeClass = TypeDefinition.getInstance(concreteClass)
                            .getInferredGenericTypeArgumentClass(PageElementSupplier.class, 0);
                        return supplierReturnTypeClass != null
                            && returnTypeClass.isAssignableFrom(supplierReturnTypeClass);
                    })
                    .collect(Collectors.toSet());

                // TODO: disambiguate using C3 linearization if any of these classes extend each other

                return thisElementSupplierClasses;
            }
        });

    /**
     * Return an Iterable containing the result of all of the suppliers that can supply the indicated return type.
     */
    static <T> Iterable<T> get(Class<T> returnTypeClass, Site site, Object object) {

        Collection<Class<?>> thisElementSupplierClasses = SUPPLIER_CLASSES.getUnchecked(returnTypeClass);
        Collection<T> result = new ArrayList<>();

        for (Class<?> cls : thisElementSupplierClasses) {
            if (PageElementSupplier.class.isAssignableFrom(cls)) {
                try {
                    @SuppressWarnings("unchecked")
                    PageElementSupplier<T> supplier = (PageElementSupplier<T>) cls.newInstance();
                    Iterable<T> supplierResult = supplier.get(site, object);

                    if (supplierResult != null) {
                        for (T element : supplierResult) {
                            result.add(element);
                        }
                    }

                } catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.warn("Unable to instantiate " + cls.getName(), e);
                }
            }
        }

        return result;
    }
}
