package brightspot.core.site;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import brightspot.core.navigation.NavigationSearch;
import brightspot.core.requestextras.headelement.CustomHeadElements;
import brightspot.core.requestextras.responseheader.CustomResponseHeaders;
import brightspot.core.tool.DateTimeUtils;
import com.google.common.collect.ImmutableSet;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Application;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import org.joda.time.DateTimeZone;

@Recordable.BeanProperty("frontEndSettings")
@Recordable.FieldInternalNamePrefix("express.frontendSettings.")
public class FrontEndSettings extends Modification<SiteSettings> {

    private static final String ERROR_PAGES_TAB = "Error Pages";

    @ToolUi.Tab("Front-End")
    private Locale locale;

    @ToolUi.FieldDisplayType("timeZone")
    @ToolUi.Placeholder(dynamicText = "${modification.getTimeZonePlaceholder()}")
    @ToolUi.Tab("Front-End")
    private String timeZone;

    @ToolUi.Placeholder(DateTimeUtils.DEFAULT_DATE_TIME_FORMAT)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getDefaultDateTimeFormatNote()}'></span>")
    @ToolUi.Tab("Front-End")
    private String defaultDateTimeFormat;

    //TODO: disable image editor
    @MimeTypes("+image/png +image/jpeg")
    @ToolUi.NoteHtml("<strong>Expected File Types:</strong> PNG or JPEG    <strong>Recommended Size:</strong> 260w x 260h or larger")
    @ToolUi.Tab("Front-End")
    private StorageItem favicon;

    @ToolUi.Tab("Front-End")
    @ToolUi.Note("Upload .ico file here")
    private StorageItem faviconIco;

    @Embedded
    @ToolUi.Cluster("Integrations")
    @ToolUi.Tab("Front-End")
    private List<IntegrationItems> integrations;

    @Minimum(5)
    @ToolUi.Cluster("Integrations")
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getDisableThirdPartyHtml()}'></span>")
    @ToolUi.Tab("Front-End")
    private String disableThirdPartyParameterValue;

    @ToolUi.Cluster("Video")
    @ToolUi.Tab("Front-End")
    private VideoFrontendSettings videoFrontendSettings;

    @ToolUi.Cluster("Advanced")
    @ToolUi.Tab("Front-End")
    @ToolUi.CodeType("application/json")
    @ToolUi.NoteHtml("<a href=\"https://developer.mozilla.org/en-US/docs/Web/Manifest\" target=\"_blank\">Web App Manifest</a>")
    private String webAppManifest;

    @ToolUi.Cluster("Advanced")
    @ToolUi.Tab("Front-End")
    @ToolUi.CodeType("text/xml")
    @ToolUi.NoteHtml("<a href=\"https://msdn.microsoft.com/en-us/library/dn320426(v=vs.85).aspx\" target=\"_blank\">Schema Reference</a>")
    private String browserConfig;

    /**
     * @deprecated Replaced by errorHandlers
     */
    @ToolUi.Cluster("Error")
    @ToolUi.OnlyPathed
    @ToolUi.Tab("Front-End")
    @ToolUi.DisplayName("Not Found Error Page - Deprecated")
    @Deprecated
    private Content notFoundErrorPage;

    /**
     * @deprecated Replaced by errorHandlers
     */
    @ToolUi.Cluster("Error")
    @ToolUi.OnlyPathed
    @ToolUi.Tab("Front-End")
    @ToolUi.DisplayName("Server Error Page - Deprecated")
    @Deprecated
    private Content serverErrorPage;

    @ToolUi.Cluster("Error")
    @ToolUi.Tab("Front-End")
    private Set<ErrorHandler> errorHandlers;

    @ToolUi.Cluster("Header")
    @ToolUi.Tab("Page Defaults")
    private NavigationSearch searchPage;

    @ToolUi.Cluster("Advanced")
    @ToolUi.NoteHtml("Add custom Javascript and CSS resources to certain pages.")
    @ToolUi.Tab("Front-End")
    private List<CustomHeadElements> customScriptsAndStyles;

    @ToolUi.Cluster("Advanced")
    @ToolUi.NoteHtml("Add custom HTTP Response Headers to certain pages.")
    @ToolUi.Tab("Front-End")
    private List<CustomResponseHeaders> customResponseHeaders;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDefaultDateTimeFormat() {
        return defaultDateTimeFormat;
    }

    public void setDefaultDateTimeFormat(String defaultDateTimeFormat) {
        this.defaultDateTimeFormat = defaultDateTimeFormat;
    }

    public String getDefaultDateTimeFormatNote() {

        String currentDateTime = DateTimeUtils
            .getLocalizedDateTimeFormatter(this.defaultDateTimeFormat, this.locale, this.timeZone)
            .format(LocalDateTime.now(DateTimeUtils.getZoneIdOrDefault(this.timeZone)));

        String validityNotice = DateTimeUtils.isDateTimePatternValid(defaultDateTimeFormat) ? ""
            : "<strong>Invalid Pattern!</strong> Falling back to default format: "
                + DateTimeUtils.DEFAULT_DATE_TIME_FORMAT + "<br>";

        String reference = "<a href=\"https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns\" target=\"_blank\">Pattern Reference</a><br>";

        return validityNotice + reference + "<strong>Output:</strong> " + currentDateTime;
    }

    public List<IntegrationItems> getIntegrations() {
        if (integrations == null) {
            integrations = new ArrayList<>();
        }
        return integrations;
    }

    public void setIntegrations(List<IntegrationItems> integrations) {
        this.integrations = integrations;
    }

    public StorageItem getFavicon() {
        return favicon;
    }

    public void setFavicon(StorageItem favicon) {
        this.favicon = favicon;
    }

    public StorageItem getFaviconIco() {
        return faviconIco;
    }

    public void setFaviconIco(StorageItem faviconIco) {
        this.faviconIco = faviconIco;
    }

    public String getDisableThirdPartyParameterValue() {
        return disableThirdPartyParameterValue;
    }

    public void setDisableThirdPartyParameterValue(String disableThirdPartyParameterValue) {
        this.disableThirdPartyParameterValue = disableThirdPartyParameterValue;
    }

    public String getDisableThirdPartyHtml() {
        if (!StringUtils.isBlank(disableThirdPartyParameterValue)) {
            return "Add the parameter disable3rdParty to any page with your secret. Ex http://foo.com?disable3rdParty="
                + disableThirdPartyParameterValue;
        }

        return null;
    }

    public static <T> T get(Site site, Function<FrontEndSettings, T> getter) {

        T value = null;
        if (site != null) {
            value = getter.apply(site.as(FrontEndSettings.class));

            if (!ObjectUtils.isBlank(value)) {
                return value;
            }
        }

        CmsTool cms = Application.Static.getInstance(CmsTool.class);

        if (cms != null) {
            value = getter.apply(cms.as(FrontEndSettings.class));
        }

        return value;
    }

    public VideoFrontendSettings getVideoFrontendSettings() {
        return videoFrontendSettings;
    }

    public void setVideoFrontendSettings(
        VideoFrontendSettings videoFrontendSettings) {
        this.videoFrontendSettings = videoFrontendSettings;
    }

    public String getWebAppManifest() {
        return webAppManifest;
    }

    public void setWebAppManifest(String webAppManifest) {
        this.webAppManifest = webAppManifest;
    }

    public String getBrowserConfig() {
        return browserConfig;
    }

    public void setBrowserConfig(String browserConfig) {
        this.browserConfig = browserConfig;
    }

    @Relocate
    public Set<ErrorHandler> getErrorHandlers() {
        if (errorHandlers == null) {
            errorHandlers = new HashSet<>();
        }

        // Migrates legacy Error Page settings to new handlers
        if (errorHandlers.isEmpty()) {
            if (notFoundErrorPage != null) {
                ContentErrorHandler handler = new ContentErrorHandler(
                    ImmutableSet.of(404),
                    null,
                    null,
                    notFoundErrorPage);
                errorHandlers.add(handler);
                notFoundErrorPage = null;
            }

            if (serverErrorPage != null) {
                ContentErrorHandler handler = new ContentErrorHandler(
                    null,
                    ImmutableSet.of(ErrorStatusCategory.SERVER_ERROR),
                    null,
                    serverErrorPage);
                errorHandlers.add(handler);
                serverErrorPage = null;
            }
        }
        return errorHandlers;
    }

    public void setErrorHandlers(Set<ErrorHandler> errorHandlers) {
        this.errorHandlers = errorHandlers;
    }

    @Deprecated
    public Content getNotFoundErrorPage() {
        return notFoundErrorPage;
    }

    @Deprecated
    public void setNotFoundErrorPage(Content notFoundErrorPage) {
        this.notFoundErrorPage = notFoundErrorPage;
    }

    @Deprecated
    public Content getServerErrorPage() {
        return serverErrorPage;
    }

    @Deprecated
    public void setServerErrorPage(Content serverErrorPage) {
        this.serverErrorPage = serverErrorPage;
    }

    public NavigationSearch getSearchPage() {
        return searchPage;
    }

    public void setSearchPage(NavigationSearch searchPage) {
        this.searchPage = searchPage;
    }

    public List<CustomHeadElements> getCustomScriptsAndStyles() {
        if (customScriptsAndStyles == null) {
            customScriptsAndStyles = new ArrayList<>();
        }
        return customScriptsAndStyles;
    }

    public void setCustomScriptsAndStyles(List<CustomHeadElements> customScriptsAndStyles) {
        this.customScriptsAndStyles = customScriptsAndStyles;
    }

    public List<CustomResponseHeaders> getCustomResponseHeaders() {
        if (customResponseHeaders == null) {
            customResponseHeaders = new ArrayList<>();
        }
        return customResponseHeaders;
    }

    public void setCustomResponseHeaders(List<CustomResponseHeaders> customResponseHeaders) {
        this.customResponseHeaders = customResponseHeaders;
    }

    /**
     * Not for external use
     **/
    public String getTimeZonePlaceholder() {
        return String.format(
            "Default (%s)",
            Optional.ofNullable(FrontEndSettings.get(null, FrontEndSettings::getTimeZone))
                .orElse(DateTimeZone.getDefault().getID()).replace("/", " / ").replace("_", " "));
    }
}
