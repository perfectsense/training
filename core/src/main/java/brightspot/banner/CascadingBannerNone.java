package brightspot.banner;

import java.util.List;

import brightspot.cascading.listmerge.ListMergeStrategy;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("None")
public class CascadingBannerNone extends CascadingBanner {

    @Override
    protected List<Banner> getItems() {
        return null;
    }

    @Override
    protected ListMergeStrategy<Banner> getMergeStrategy() {
        return null;
    }
}
