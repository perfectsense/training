package brightspot.pinterest.action;

import java.util.Optional;

import brightspot.core.action.actionbar.ActionBar;
import brightspot.core.action.actionbar.ActionBarViewModel;
import brightspot.core.image.ImageOption;
import brightspot.core.share.Shareable;
import brightspot.core.share.ShareableData;
import brightspot.core.social.PinterestSocialService;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpRequestAttribute;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.pinterest.PinterestShareButtonView;

/**
 * ViewModel for PinterestShareAction. Provides access to all the fields in the request for a Pinterest share dialog,
 * implements {@link PinterestShareButtonView}
 */
public class PinterestShareActionViewModel extends ViewModel<PinterestShareAction> implements PinterestShareButtonView {

    @HttpRequestAttribute(ActionBar.ACTION_BAR_CONTEXT_ATTRIBUTE)
    protected Recordable mainContent;

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getDescription() {
        // What Pinterest calls "Description" is actually the title that they display.
        return StringUtils.encodeUri(
            Optional.ofNullable(mainContent.as(Shareable.class))
                .map(Shareable::asShareableData)
                .map(ShareableData::getShareTitle)
                .orElse(null)
        );
    }

    @Override
    public CharSequence getMedia() {
        return Optional.ofNullable(mainContent.as(Shareable.class))
            .map(Shareable::asShareableData)
            .map(ShareableData::getShareImage)
            .map(ImageOption::getImageOptionFile)
            .map(StorageItem::getPublicUrl)
            .orElse(null);
    }

    @Override
    public CharSequence getBody() {
        return "Pinterest";
    }

    @Override
    public CharSequence getUrl() {
        PinterestSocialService service = Query.from(PinterestSocialService.class).first();
        return Optional.ofNullable(ActionBarViewModel.getShareActionUrl(service, site, mainContent))
            .map(CharSequence::toString)
            .map(StringUtils::encodeUri)
            .orElse(null);
    }
}
