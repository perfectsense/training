package brightspot.core.asset;

import java.util.Set;

/**
 * Provider to supply the appropriate mime types for the {@link com.psddev.dari.util.StorageItem} of an {@link
 * AbstractAsset} type.
 */
public interface AssetMimeTypesProvider {

    /**
     * @return Nonnull.
     */
    Set<String> get();
}
