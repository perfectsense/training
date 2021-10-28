package brightspot.util;

import com.google.common.collect.ImmutableMap;

/**
 * Wrapper around Google Guava {@link ImmutableMap} which filters out entries with null keys or values.
 *
 * Use {@link Builder} for maps with more than 6 entries.
 *
 * Copied from https://github.com/perfectsense/component-lib/blob/release/4.2/null-safe-immutable-map/src/main/java/brightspot/util/FixedMap.java
 */
public class FixedMap {

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static <K, V> ImmutableMap<K, V> of() {
        return ImmutableMap.of();
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
        return k1 != null && v1 != null
            ? ImmutableMap.of(k1, v1)
            : ImmutableMap.of();
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        putIfNonNull(k1, v1, builder);
        putIfNonNull(k2, v2, builder);
        return builder.build();
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        putIfNonNull(k1, v1, builder);
        putIfNonNull(k2, v2, builder);
        putIfNonNull(k3, v3, builder);
        return builder.build();
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        putIfNonNull(k1, v1, builder);
        putIfNonNull(k2, v2, builder);
        putIfNonNull(k3, v3, builder);
        putIfNonNull(k4, v4, builder);
        return builder.build();
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        putIfNonNull(k1, v1, builder);
        putIfNonNull(k2, v2, builder);
        putIfNonNull(k3, v3, builder);
        putIfNonNull(k4, v4, builder);
        putIfNonNull(k5, v5, builder);
        return builder.build();
    }

    public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        putIfNonNull(k1, v1, builder);
        putIfNonNull(k2, v2, builder);
        putIfNonNull(k3, v3, builder);
        putIfNonNull(k4, v4, builder);
        putIfNonNull(k5, v5, builder);
        putIfNonNull(k6, v6, builder);
        return builder.build();
    }

    private static <K, V> ImmutableMap.Builder<K, V> putIfNonNull(K k, V v, ImmutableMap.Builder<K, V> builder) {
        if (k != null && v != null) {
            return builder.put(k, v);
        }
        return builder;
    }

    public static class Builder<K, V> {

        private final ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();

        Builder<K, V> put(K k, V v) {
            putIfNonNull(k, v, builder);
            return this;
        }

        /**
         * @return an ImmutableMap containing all non-null mappings added using {@link #put(Object, Object)}
         */
        ImmutableMap<K, V> build() {
            return builder.build();
        }
    }
}
