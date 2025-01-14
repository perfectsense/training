package brightspot.init;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import brightspot.cascading.CascadingStrategyModification;
import brightspot.cascading.DefaultCascadingStrategy;
import brightspot.cascading.module.CascadingModuleListAfter;
import brightspot.cascading.navigation.CascadingNavigationOverride;
import brightspot.footer.CascadingFooterOverride;
import brightspot.footer.PageFooter;
import brightspot.genericpage.GenericPage;
import brightspot.homepage.Homepage;
import brightspot.image.WebImage;
import brightspot.l10n.LocaleSettings;
import brightspot.l10n.SiteLocaleProvider;
import brightspot.landing.LandingCascadingData;
import brightspot.landing.TypeSpecificLandingPageElements;
import brightspot.landing.TypeSpecificLandingPageElementsSettingsModification;
import brightspot.link.InternalLink;
import brightspot.logo.CascadingLogoOverride;
import brightspot.logo.ImageLogo;
import brightspot.module.list.page.DynamicPageItemStream;
import brightspot.module.list.page.PageListModulePlacementInline;
import brightspot.navigation.NavigationItem;
import brightspot.navigation.NavigationLink;
import brightspot.navigation.PageNavigation;
import brightspot.navigation.TopNavigationItem;
import brightspot.opengraph.image.OpenGraphImageSettingsModification;
import brightspot.page.CascadingPageData;
import brightspot.page.ContentErrorHandler;
import brightspot.page.ErrorHandlerSettings;
import brightspot.page.FaviconSettings;
import brightspot.search.NavigationSearchSettings;
import brightspot.search.SiteSearchPage;
import brightspot.search.TypeFilter;
import brightspot.search.sectionsecondaryfilter.SectionSecondaryFilter;
import brightspot.search.tagfilter.TagFilter;
import brightspot.section.AllSectionMatch;
import brightspot.section.CurrentSectionOptionYes;
import brightspot.section.Section;
import brightspot.section.SectionPage;
import brightspot.sort.publishdate.NewestPublishDate;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteCopierGlobalSettings;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.tool.TrailingSlashConfiguration;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Singleton;
import com.psddev.dari.util.ImageMetadataMap;
import com.psddev.dari.util.RandomUuidStorageItemPathGenerator;
import com.psddev.dari.util.RepeatingTask;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.ThrowingConsumer;
import com.psddev.localization.ListAvailableLocaleSetting;
import com.psddev.localization.LocalizationData;
import com.psddev.localization.LocalizationSiteSettings;
import com.psddev.theme.Config;
import com.psddev.theme.FixedRelease;
import com.psddev.theme.SharedThemeOption;
import com.psddev.theme.Theme;
import com.psddev.theme.ThemeSettings;
import com.psddev.theme.jar.JarBundle;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * Runs once on first server startup to initialize the environment.
 */
public class EnvironmentSetupTask extends RepeatingTask {

    private static final String TOOL_USER_NAME = "Initial Setup";
    private static final UUID TOOL_USER_ID = UUID.nameUUIDFromBytes(TOOL_USER_NAME.getBytes(StandardCharsets.UTF_8));

    @Override
    protected DateTime calculateRunTime(DateTime currentTime) {
        return everyMinute(currentTime);
    }

    @Override
    protected void doRepeatingTask(DateTime runTime) {
        if (!Site.Static.findAll().isEmpty()) {
            // probably ran already
            return;
        }

        ToolUser importUser = new ToolUser();
        importUser.getState().setId(TOOL_USER_ID);
        importUser.setName(TOOL_USER_NAME);
        importUser.setUsername("initial_setup");
        importUser.save();

        CmsTool cmsTool = setUpCmsTool();
        Content.Static.publish(cmsTool, null, importUser);

        Site site = setUpSite(importUser);
        Content.Static.publish(site, site, importUser);

        Theme theme = setUpTheme();
        Content.Static.publish(theme, site, importUser);

        site.as(ThemeSettings.class).setThemeOption(build(new SharedThemeOption(), sharedTheme -> {
            sharedTheme.setTheme(theme);
        }));

        // Section landing page elements requires theme for style selection
        site.as(TypeSpecificLandingPageElementsSettingsModification.class)
            .getTypeSpecificLandingPageContent()
            .add(build(new TypeSpecificLandingPageElements(), lpe -> {
                lpe.getTypes().add(ObjectType.getInstance(SectionPage.class));
                lpe.as(LandingCascadingData.class).setContent(build(new CascadingModuleListAfter(), after -> {
                    after.getItems().add(build(new PageListModulePlacementInline(), pageList -> {
                        pageList.getState().put(buildThemeKey("/page/list/PageList.hbs", theme), "/page/list/PageListStandardH.hbs");
                        pageList.setItemStream(build(new DynamicPageItemStream(), is -> {
                            is.setSort(new NewestPublishDate());
                            is.asQueryBuilderDynamicQueryModifier().setQueryBuilder(build(new AllSectionMatch(), sectionMatch -> {
                                sectionMatch.setCurrentSectionOption(new CurrentSectionOptionYes());
                            }));
                        }));
                    }));
                }));
            }));

        Content.Static.publish(site, site, importUser);

        Homepage homepage = setUpHomepage(site);
        Content.Static.publish(homepage, site, importUser);
    }

    static class ToolUserInitModification extends Modification<ToolUser> {

        @Override
        protected void beforeCommit() {
            ToolUser user = getOriginalObject();
            if (user.getCurrentSetSite() == null && getState().isNew()) {
                Site.Static.findAll()
                    .stream()
                    .filter(s -> user.hasPermission(s.getPermissionId()))
                    .findFirst()
                    .ifPresent(user::setCurrentSite);
            }
        }
    }

    private static CmsTool setUpCmsTool() {
        CmsTool cmsTool = Singleton.getInstance(CmsTool.class);

        cmsTool.setCompanyLogo(makeStorageItem("brightspot-bistro-logo-white.svg", "image/svg+xml"));
        cmsTool.setBackgroundImage(makeStorageItem("brightspot-bistro-food-blog.svg", "image/svg+xml"));
        cmsTool.setEnvironment("Training Docker");

        cmsTool.as(CascadingStrategyModification.class).setCascadingStrategy(new DefaultCascadingStrategy());

        cmsTool.setListGalleryDefaultView(true);
        cmsTool.setTrailingSlashConfiguration(TrailingSlashConfiguration.NORMALIZE);

        cmsTool.as(LocalizationSiteSettings.class)
            .setAvailableLocalesSetting(build(
                new ListAvailableLocaleSetting(),
                localeSetting -> localeSetting.getLocales().add(Locale.US)));

        cmsTool.as(SiteCopierGlobalSettings.class).setDisallowSiteCopy(true);

        return cmsTool;
    }

    private static Site setUpSite(ToolUser importUser) {

        Site site = new Site();
        site.setName("Brightspot Bistro");
        site.getUrls().add("http://localhost");

        WebImage siteLogo = build(new WebImage(), image -> {
            image.setInternalName("Brightspot Bistro Logo");
            image.setFile(makeStorageItem("brightspot-bistro-logo.svg", "image/svg+xml"));

            image.as(LocalizationData.class).setLocale(Locale.US);

            Content.Static.publish(image, site, importUser);
        });

        site.as(CascadingPageData.class).setLogo(build(new CascadingLogoOverride(), cascadingLogo -> {
            cascadingLogo.setLogo(build(new ImageLogo(), imageLogo -> {
                imageLogo.setInternalName("Brightspot Bistro Site Logo");
                imageLogo.setImage(siteLogo);
                Content.Static.publish(imageLogo, site, importUser);
            }));
        }));

        site.as(CascadingPageData.class).setFooter(build(new CascadingFooterOverride(), cascadingFooter -> {
            cascadingFooter.setFooter(build(new PageFooter(), footer -> {
                footer.setInternalName("Brightspot Bistro Footer");
                footer.setLogo(site.as(CascadingPageData.class).getLogo(site));
                footer.setNavigation(site.as(CascadingPageData.class).getNavigation(site));
                footer.setDisclaimer("©2024 Brightspot.");

                footer.as(LocalizationData.class).setLocale(Locale.US);

                Content.Static.publish(footer, site, importUser);
            }));
        }));

        site.as(FaviconSettings.class).setFavicon(makeStorageItem("brightspot-bistro-favicon.png", "image/png"));

        site.as(NavigationSearchSettings.class).setSearchPage(build(new SiteSearchPage(), siteSearch -> {
            siteSearch.setTitle("Search Brightspot Bistro");

            siteSearch.getFilters().add(new TypeFilter());
            siteSearch.getFilters().add(new SectionSecondaryFilter());
            siteSearch.getFilters().add(new TagFilter());

            siteSearch.as(LocalizationData.class).setLocale(Locale.US);

            siteSearch.as(Directory.ObjectModification.class).addSitePath(
                site,
                "/search",
                Directory.PathType.PERMALINK);

            Content.Static.publish(siteSearch, site, importUser);
        }));

        site.as(ErrorHandlerSettings.class).getErrorHandlers().add(build(new ContentErrorHandler(), errorHandler -> {
            errorHandler.getStatusCodes().add(404);
            errorHandler.setContent(build(new GenericPage(), page -> {
                page.setDisplayName("Content Not Found");

                page.as(LocalizationData.class).setLocale(Locale.US);

                Content.Static.publish(page, site, importUser);
            }));
        }));

        site.as(OpenGraphImageSettingsModification.class).setDefaultOpenGraphImage(siteLogo);

        site.setCmsLogo(makeStorageItem("brightspot-bistro-logo-white.svg", "image/svg+xml"));

        site.as(LocaleSettings.class).setLocaleProvider(build(new SiteLocaleProvider(), localeProvider -> {
            localeProvider.setLocale(Locale.US);
        }));

        site.as(LocalizationSiteSettings.class)
            .setAvailableLocalesSetting(build(
                new ListAvailableLocaleSetting(),
                localeSetting -> localeSetting.getLocales().add(Locale.US)));

        Section recipeSection = build(new SectionPage(), section -> {
            section.setDisplayName("Recipes");

            section.as(LocalizationData.class).setLocale(Locale.US);

            section.as(Directory.ObjectModification.class).addSitePath(
                site,
                "/recipes",
                Directory.PathType.PERMALINK);

            Content.Static.publish(section, site, importUser);
        });

        site.as(CascadingPageData.class).setNavigation(build(new CascadingNavigationOverride(), cascadingNav -> {
            cascadingNav.setNavigation(build(new PageNavigation(), nav -> {
                nav.setInternalName("Brightspot Bistro Site Navigation");
                nav.getItems().add(makeSectionNavItem(recipeSection, site));

                nav.as(LocalizationData.class).setLocale(Locale.US);

                Content.Static.publish(nav, site, importUser);
            }));
        }));

        return site;
    }

    private static Theme setUpTheme() {

        Theme theme = new Theme();
        theme.setName("Default Theme");

        theme.setStableRelease(build(new FixedRelease(), release -> {
            release.setBundle(Query.from(JarBundle.class)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("JarBundle not found")));
        }));

        return theme;
    }

    private static Homepage setUpHomepage(Site site) {
        Homepage homepage = new Homepage();
        homepage.setInternalName("Brightspot Bistro Homepage");

        homepage.getContent().add(build(new PageListModulePlacementInline(), listModule -> {
            listModule.setItemStream(build(new DynamicPageItemStream(), itemStream -> {
                itemStream.setSort(new NewestPublishDate());
            }));
        }));

        homepage.as(LocalizationData.class).setLocale(Locale.US);

        homepage.as(Directory.ObjectModification.class).addSitePath(
            site,
            "/",
            Directory.PathType.PERMALINK);

        return homepage;
    }

    private static String buildThemeKey(String template, Theme theme) {
        String reformattedFile = template
            .replace("/", ":")
            .concat("._template");

        String themeId = Config.getInstance(theme, theme.getBundle()).getFieldPrefix();
        return themeId.concat(reformattedFile);
    }

    private static NavigationItem makeSectionNavItem(Section section, Site site) {
        return build(new TopNavigationItem(), navItem -> {
            navItem.setTitle(build(new NavigationLink(), navLink -> {
                navLink.setLink(build(new InternalLink(), link -> {
                    link.setItem(section);
                }));
            }));
        });
    }

    private static StorageItem makeStorageItem(String filename, String mimeType) {
        try {
            StorageItem storageItem = StorageItem.Static.create();

            storageItem.setPath(StringUtils.removeStart(
                new RandomUuidStorageItemPathGenerator().createPath(filename),
                "/"));

            storageItem.setContentType(mimeType);

            try (InputStream data = EnvironmentSetupTask.class.getResourceAsStream("/images/" + filename)) {
                storageItem.setMetadata(new ImageMetadataMap(Objects.requireNonNull(data)));
            }

            try (InputStream data = EnvironmentSetupTask.class.getResourceAsStream("/images/" + filename)) {
                storageItem.setData(Objects.requireNonNull(data));
                storageItem.save();
            }

            return storageItem;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static <T, X extends Throwable> T build(T object, ThrowingConsumer<T, X> consumer) throws X {
        consumer.accept(object);
        return object;
    }
}
