package brightspot.core.attachment;

import java.util.Arrays;
import java.util.List;

import brightspot.core.asset.AbstractAsset;
import brightspot.core.link.Linkable;
import brightspot.core.promo.PromotableWithOverrides;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.cms.tool.content.UrlsWidget;
import com.psddev.dam.DocumentDataExtractable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;

@ToolUi.Publishable(false)
public class Attachment extends AbstractAsset implements
    ContentEditWidgetDisplay,
    Directory.Item,
    DocumentDataExtractable,
    Linkable,
    PromotableWithOverrides {

    public static final String PROMOTABLE_TYPE = "attachment";

    private static final List<String> HIDDEN_WIDGETS = Arrays.asList(
        UrlsWidget.class.getName());

    @Override
    public String createPermalink(Site site) {
        StorageItem file = getFile();
        return file != null ? file.getPublicUrl() : null;
    }

    @Override
    public boolean shouldDisplayContentEditWidget(String widgetName) {
        return !HIDDEN_WIDGETS.contains(widgetName);
    }

    @Override
    protected void beforeCommit() {
        Site site = as(Site.ObjectModification.class).getOwner();
        Directory.ObjectModification dirMod = as(Directory.ObjectModification.class);
        String existingPath = Directory.extractExternalUrl(dirMod.getSitePermalinkPath(site));
        String newPath = createPermalink(null);

        if (!ObjectUtils.equals(newPath, existingPath)) {
            dirMod.clearPaths();
            dirMod.addPath(newPath, Directory.PathType.PERMALINK);
        }
    }

    @Override
    public String getLinkableText() {
        return getTitle();
    }

    @Override
    public String getLinkableUrl(Site site) {
        return createPermalink(null);
    }

    @Override
    public String getPromotableTitleFallback() {
        return getTitle();
    }

    @Override
    public String getPromotableType() {
        return PROMOTABLE_TYPE;
    }
}
