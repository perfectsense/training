package brightspot.core.asset;

import java.util.Arrays;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.psddev.cms.tool.BulkUploadProcessor;

/**
 * Basic processor that hides the {@code title} and {@code file} fields for a type that follows the {@link
 * AbstractAsset} model.
 */
public abstract class AssetBulkUploadProcessor<T extends AbstractAsset> extends BulkUploadProcessor<T> {

    @Override
    public Set<String> getHiddenFields() {
        return ImmutableSet.copyOf(Arrays.asList("title", "file"));
    }
}
