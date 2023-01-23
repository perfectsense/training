package brightspot.newsletter;

import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;

import brightspot.api.client.ApiClientManager;
import brightspot.mailchimp.MailchimpApiSettings;
import brightspot.mailchimp.MailchimpSiteSettings;
import brightspot.mailchimp.MailchimpTemplateProvider;
import brightspot.mailchimp.api.MailchimpClient;
import brightspot.mailchimp.api.StandardMailchimpClientConfiguration;
import brightspot.sailthru.SailthruApiSettings;
import brightspot.sailthru.SailthruSiteSettings;
import brightspot.sailthru.SailthruTemplateProvider;
import brightspot.sailthru.api.SailthruClient;
import brightspot.sailthru.api.SailthruInternalException;
import brightspot.sailthru.api.StandardSailthruClientConfiguration;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.PathViewTemplateLoader;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewModelCreator;
import com.psddev.cms.view.ViewOutput;
import com.psddev.cms.view.ViewRenderer;
import com.psddev.cms.view.ViewResponse;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.web.WebRequest;
import com.psddev.theme.Bundleable;
import com.psddev.theme.Theme;
import com.psddev.theme.ThemeSettings;

public class NewsletterSubstitution extends Newsletter implements Substitution, MailchimpTemplateProvider,
        SailthruTemplateProvider {

    @Override
    public String getMailchimpTemplateUrl() {
        return this.as(Directory.ObjectModification.class).getPermalink();
    }

    @Override
    public MailchimpClient getMailchimpClient() {
        Site site = WebRequest.isAvailable()
            ? WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite()
            : this.as(Site.ObjectModification.class).getOwner();
        MailchimpApiSettings settings = SiteSettings.get(
            site,
            siteSettings -> siteSettings.as(MailchimpSiteSettings.class).getSettings());
        Date lastChanged = SiteSettings.get(
            site,
            siteSettings -> siteSettings.as(MailchimpSiteSettings.class).getLastChanged());
        if (settings == null || lastChanged == null) {

            return null;
        }

        StandardMailchimpClientConfiguration configuration = new StandardMailchimpClientConfiguration(
            getId(),
            settings.getEndpoint(),
            settings.getApiKey(),
            lastChanged.toInstant());
        return ApiClientManager.getApiClient(configuration);
    }

    @Override
    public String getSailthruContentHtml() {
        Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
        Theme theme = SiteSettings.get(site, s -> s.as(ThemeSettings.class).getTheme());
        Path rootPath = Optional.ofNullable(theme.getBundle())
                .map(Bundleable::getRootDirectory)
                .orElse(null);
        return generateHtml(this, PageEntryView.class, theme, rootPath);
    }

    @Override
    public SailthruClient getSailthruClient() {
        Site site = WebRequest.isAvailable()
                ? WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite()
                : this.as(Site.ObjectModification.class).getOwner();
        SailthruApiSettings settings = SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(SailthruSiteSettings.class).getSettings());
        Date lastChanged = SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(SailthruSiteSettings.class).getLastChanged());
        if (settings == null || lastChanged == null) {

            return null;
        }

        StandardSailthruClientConfiguration configuration = new StandardSailthruClientConfiguration(
                getId(),
                "https://api.sailthru.com",
                settings.getApiKey(),
                settings.getApiSecret(),
                lastChanged.toInstant());
        return ApiClientManager.getApiClient(configuration);
    }

    private static <T> ViewModel<? super T> createViewModel(T object, Class<?> view) {
        if (ObjectUtils.isBlank(view)) {
            throw new IllegalArgumentException();
        }

        // 1. Find ViewCreator class (check the different view types, etc.)
        Class<? extends ViewModel<? super T>> viewModelClass = null;

        viewModelClass = ViewModel.findViewModelClass(view, object);

        // 2. Create custom ViewModelCreator
        ViewModelCreator viewModelCreator = new ViewModel.DefaultCreator();

        // 3. Create a ViewResponse
        ViewResponse viewResponse = new ViewResponse();

        // 4. Create ViewModel from ViewModelCreator and pass ViewResponse
        return viewModelCreator.createViewModel(viewModelClass, object, viewResponse);
    }

    private static <T> String generateHtml(T object, Class<?> view, Theme theme, Path themeRootPath) {

        ViewModel<? super T> viewModel = createViewModel(object, view);

        if (viewModel != null) {
            // Find the ViewRenderer for the ViewModel
            ViewRenderer renderer = ViewRenderer.createRenderer(viewModel);

            // Render the ViewModel
            if (renderer != null) {
                if (theme != null) {
                    ViewOutput result = renderer
                            .render(viewModel, PathViewTemplateLoader.getInstance(theme.getId(), null, themeRootPath));
                    return result.get();
                } else {
                    throw new SailthruInternalException("Could not generate HTML for Sailthru: Theme not found!");
                }
            } else {
                throw new SailthruInternalException("Could not generate HTML for Sailthru: Could not create ViewRenderer!");
            }
        } else {
            throw new SailthruInternalException("Could not generate HTML for Sailthru: ViewModel not found!");
        }
    }
}
