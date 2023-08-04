package brightspot.banner;

import java.util.ArrayList;
import java.util.List;

import brightspot.cascading.listmerge.AfterListMergeStrategy;
import brightspot.cascading.listmerge.ListMergeStrategy;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.theme.StyleEmbeddedContentCreator;

@Recordable.DisplayName("Add After")
public class CascadingBannerAfter extends CascadingBanner {

    @ToolUi.Unlabeled
    @ToolUi.EmbeddedContentCreatorClass(StyleEmbeddedContentCreator.class)
    private List<Banner> items;

    @Override
    public List<Banner> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    @Override
    protected ListMergeStrategy<Banner> getMergeStrategy() {
        return new AfterListMergeStrategy<>();
    }

}
