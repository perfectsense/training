package brightspot.core.classification;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.core.listmodule.DynamicQueryModifier;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

public interface ClassificationDynamicQueryModifier extends DynamicQueryModifier {

    String CLASSIFICATION_CLUSTER = "Classification";

    default <T extends Recordable, S extends Recordable> void updateQueryWithClassificationQuery(
        Query<?> query,
        Object mainObject,
        Collection<? super S> classifiers,
        Class<T> currentClassifierType,
        Class<S> classifierType) {

        if (query == null || mainObject == null || ObjectUtils.isBlank(classifiers)) {

            return;
        }

        // The classifiers to use for filtering
        Set<S> classifierSet = new HashSet<S>();

        // Adding all the actual ancestors (not the CurrentClassifiers Singleton) to the set
        classifiers.stream()
            .filter(classifierType::isInstance)
            .map(classifierType::cast)
            .forEach(classifierSet::add);

        // Finding the CurrentClassifiers Singleton
        boolean useCurrentClassifiers = classifiers.parallelStream()
            .filter(currentClassifierType::isInstance)
            .map(currentClassifierType::cast)
            .findAny()
            .isPresent();

        if (useCurrentClassifiers) {

            Set<S> currentClassifiers = this.getCurrentClassifiers(classifierType, mainObject);

            if (currentClassifiers != null) {

                classifierSet.addAll(currentClassifiers);
            }
        }

        if (ObjectUtils.isBlank(classifierSet) && useCurrentClassifiers) {

            // Cause empty set to be returned (IDs are never null)
            // ONLY IF
            // - no classifiers were provided
            // - the CurrentClassifiers Singleton was provided
            // - no current classifiers were found
            query.where("id = null");

        } else {

            this.addClassificationQuery(query, mainObject, classifierSet);
        }
    }

    default <S extends Recordable> Set<S> getCurrentClassifiers(Class<S> classifierType, Object mainObject) {
        Set<S> currentClassifiers = Collections.emptySet();

        if (classifierType != null && mainObject != null && mainObject instanceof Recordable) {

            Recordable recordable = (Recordable) mainObject;

            if (recordable.isInstantiableTo(classifierType)) {

                S classifier = recordable.as(classifierType);

                if (classifier != null) {
                    currentClassifiers = Collections.singleton(classifier);
                }

            } else {

                currentClassifiers = Optional.ofNullable(recordable.as(ClassificationObjectModification.class))
                    .map(ClassificationObjectModification::getAllClassificationAttributes)
                    .map(classificationAttributes -> classificationAttributes.stream()
                        .filter(classifierType::isInstance)
                        .map(classifierType::cast)
                        .collect(Collectors.toSet())
                    )
                    .orElse(Collections.emptySet());
            }
        }

        return currentClassifiers;
    }

    default <S extends Recordable> void addClassificationQuery(
        Query<?> query, Object mainObject,
        Set<S> currentClassifiers) {

        query.where(ClassificationObjectModification.class.getName() + "/"
            + ClassificationObjectModification.CLASSIFICATION_ATTRIBUTES_INDEX
            + " = ?", currentClassifiers);

        if (mainObject instanceof Recordable) {

            // Filter current object
            query.and("id != " + ((Recordable) mainObject).getState().getId());
        }
    }
}
