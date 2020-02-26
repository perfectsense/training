package brightspot.core.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * C3 Linearization copied from {@link com.psddev.cms.view.ViewModel}.
 */
public final class C3Utils {

    private C3Utils() {
    }

    // https://en.wikipedia.org/wiki/C3_linearization
    public static List<Class<?>> c3LinearizeClass(Class<?> source) {
        return c3Linearize(source, child -> {

            List<Class<?>> parents = new ArrayList<>();

            // super class first...
            Class<?> superClass = child.getSuperclass();
            if (superClass != null && superClass != Object.class) {
                parents.add(superClass);
            }

            // interfaces second...
            parents.addAll(Arrays.asList(child.getInterfaces()));

            // and Object last for all top level classes and interfaces.
            // This is to ensure Object always ends up last.
            if (child != Object.class && (superClass == null || superClass == Object.class)) {
                parents.add(Object.class);
            }

            return parents;

        }, new HashSet<>());
    }

    private static <T> List<T> c3Linearize(T source, Function<T, List<T>> parentsFunction, Set<T> visited) {

        // Guard against stack overflow.
        if (!visited.add(source)) {
            throw new IllegalStateException("Cyclic hierarchy detected.");
        }

        // Store the linearization result.
        List<T> result = new ArrayList<>();

        // The source is always first.
        result.add(source);

        // Collect the source's direct parents.
        List<T> sourceParents = new ArrayList<>(parentsFunction.apply(source));

        if (!sourceParents.isEmpty()) {

            // Linearize each parent and add the result to merge list.
            List<List<T>> toMerge = sourceParents.stream()
                .map(parent -> c3Linearize(parent, parentsFunction, new HashSet<>(visited)))
                .collect(Collectors.toCollection(ArrayList::new));

            // Add the source parents as the last item in the merge list.
            toMerge.add(sourceParents);

            // Merge and add to result.
            result.addAll(c3merge(toMerge));
        }

        return result;
    }

    private static <T> List<T> c3merge(List<List<T>> lists) {

        List<T> merged = new ArrayList<>();

        // while the lists are not empty
        while (lists.stream().map(List::size).mapToInt(i -> i).sum() > 0) {

            // grab the first item from each list
            List<T> candidates = new ArrayList<>(lists.stream()
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

            // find the first candidate that is not present in the tail of any of the lists
            T candidate = candidates.stream()
                .filter(c -> lists.stream()
                    .allMatch(list -> list.size() <= 1 || !list.subList(1, list.size()).contains(c)))
                .findFirst().orElse(null);

            if (candidate != null) {
                // remove the candidate from each list and add it the merge list.
                lists.forEach(list -> list.remove(candidate));
                merged.add(candidate);

            } else {
                throw new IllegalStateException("Cyclic hierarchy detected.");
            }
        }

        return merged;
    }

}
