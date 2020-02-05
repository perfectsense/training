package brightspot.core.action.actionbar;

import java.util.Optional;

import brightspot.core.share.Shareable;
import brightspot.core.social.SocialService;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.core.action_bar.ActionBarView;
import com.psddev.styleguide.core.action_bar.ActionBarViewItemsField;

public class ActionBarViewModel extends ViewModel<Recordable> implements ActionBarView {

    @CurrentSite
    Site site;

    ActionBar bar;

    Optional<Shareable> shareable;

    @Override
    protected boolean shouldCreate() {
        ActionBar defaultActionBar = SiteSettings.get(site, f -> f.as(ActionBarSettings.class).getDefaultActionBar());

        bar = SiteSettings.get(site, f -> f.as(ActionBarSettings.class).getActionBarSettings())
            .stream()
            .filter(setting -> setting.getTypes().contains(model.getState().getType()))
            .findFirst()
            .map(ActionBarSetting::getActionBar)
            .orElse(defaultActionBar);

        return bar != null;
    }

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);
        shareable = Optional.ofNullable(model)
            .filter(Shareable.class::isInstance)
            .map(Shareable.class::cast);
    }

    @Override
    public Iterable<? extends ActionBarViewItemsField> getItems() {
        PageContextFilter.Static.getRequest()
            .setAttribute(
                ActionBar.ACTION_BAR_CONTEXT_ATTRIBUTE,
                model); // TODO: https://perfectsense.atlassian.net/browse/BSP-3655
        return createViews(ActionBarViewItemsField.class, bar.getItems());
    }

    public static CharSequence appendTrackingParameter(Site site, String url, String paramValue) {
        String appendParam = SiteSettings.get(site, s -> s.as(ActionBarSettings.class).getTrackingParameterName());
        return StringUtils.isBlank(appendParam) ? url : StringUtils.addQueryParameters(url, appendParam, paramValue);
    }

    /**
     * Helper method added to reduce redundant logic in *ShareActionViewmodel#getUrl.
     *
     * @param service Social service needed to call getShareableUrl.
     * @param site
     * @param mainContent
     * @return share URL string for given content and social service.
     */
    public static CharSequence getShareActionUrl(SocialService service, Site site, Recordable mainContent) {
        return service != null
            ? Optional.ofNullable(mainContent.as(Shareable.class))
            .map(shareable -> shareable.getShareableUrl(site))
            .map(url -> ActionBarViewModel.appendTrackingParameter(site, url, service.getKey()))
            .orElse(null)
            : null;
    }
}
