package etc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import brightspot.cascading.CascadingStrategyModification;
import brightspot.cascading.DefaultCascadingStrategy;
import brightspot.l10n.LocaleSettings;
import brightspot.l10n.SiteLocaleProvider;
import brightspot.stylepackage.BuiltInStyle;
import brightspot.stylepackage.StylePackage;
import brightspot.stylepackage.StyleThemeModification;
import brightspot.task.pipeline.Pipeline;
import brightspot.task.pipeline.Read;
import brightspot.task.pipeline.Transform;
import brightspot.task.pipeline.Write;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.History;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.l10n.AvailableLocaleSiteSettings;
import com.psddev.cms.l10n.JavaLocale;
import com.psddev.cms.l10n.LocaleGlobalSettings;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.tool.TrailingSlashConfiguration;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Singleton;
import com.psddev.dari.db.State;
import com.psddev.dari.db.WriteOperation;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.IoUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Task;
import com.psddev.dari.util.TypeReference;
import com.psddev.theme.Bundleable;
import com.psddev.theme.FixedRelease;
import com.psddev.theme.SharedThemeOption;
import com.psddev.theme.Theme;
import com.psddev.theme.ThemeSettings;
import com.psddev.theme.jar.JarBundle;

/**
 * Run this in {@code /_debug/code} on a fresh Docker instance running the training {@code web} war.
 *
 * Exported Inspire Confidence content will be loaded from {@code export-training.json} located in the same directory
 * as the {@code docker-compose.yaml} file.
 */
public class Setup {

    private static final UUID EXISTING_IC_SITE_ID = UUID.fromString("0000015d-6154-d664-a95d-e3ff95640000");
    private static final String TOOL_USER_NAME = "Inspire Confidence import";
    private static final UUID TOOL_USER_ID = UUID.nameUUIDFromBytes(TOOL_USER_NAME.getBytes(StandardCharsets.UTF_8));

    public static Object main() {
        new Task() {

            @Override
            protected void doTask() throws Exception {
                setUpDocker();
            }
        }.submit();

        return "Setup and import started";
    }

    public static void setUpDocker() throws IOException {
        if (Query.fromAll()
            .where("_type != ?", ClassFinder.findConcreteClasses(Singleton.class))
            .and("_type != ?", JarBundle.class)
            .hasMoreThan(0)) {

            throw new IllegalArgumentException("Database is not empty");
        }
        if (Query.fromAll()
            .where("_type != ?", ClassFinder.findConcreteClasses(Singleton.class))
            .and("_type != ?", JarBundle.class)
            .and("* matches *")
            .hasMoreThan(0)) {

            throw new IllegalArgumentException("solr is not empty");
        }

        ToolUser importUser = new ToolUser();
        importUser.getState().setId(TOOL_USER_ID);
        importUser.setName(TOOL_USER_NAME);
        importUser.setUsername("ic_import");
        importUser.save();

        CmsTool cmsTool = setUpCmsTool();
        Content.Static.publish(cmsTool, null, importUser);

        Site site = setUpSite();
        Content.Static.publish(site, site, importUser);

        Theme theme = setUpTheme();
        Content.Static.publish(theme, site, importUser);

        SharedThemeOption sharedTheme = new SharedThemeOption();
        sharedTheme.setTheme(theme);
        site.as(ThemeSettings.class).setThemeOption(sharedTheme);

        Content.Static.publish(site, site, importUser);

        importContent(importUser);
    }

    private static CmsTool setUpCmsTool() {
        CmsTool cmsTool = Singleton.getInstance(CmsTool.class);

        // cmsTool.setCompanyLogo(); TODO
        cmsTool.setEnvironment("Training Docker");

        cmsTool.as(CascadingStrategyModification.class).setCascadingStrategy(new DefaultCascadingStrategy());
        // cmsTool.as(PageViewsCmsToolModification.class).setTaskHost(); TODO

        cmsTool.setListGalleryDefaultView(true);
        cmsTool.setTrailingSlashConfiguration(TrailingSlashConfiguration.NORMALIZE);

        JavaLocale javaLocale = new JavaLocale();
        javaLocale.setLocale(Locale.US);
        cmsTool.as(LocaleGlobalSettings.class).getLocales().add(javaLocale);

        return cmsTool;
    }

    private static Site setUpSite() {

        Site site = new Site();
        site.getState().setId(EXISTING_IC_SITE_ID);
        site.setName("Inspire Confidence");
        site.getUrls().add("http://localhost");
        /* TODO
        site.as(CascadingPageData.class).setLogo();

        site.as(FaviconSettings.class).setFavicon();

        site.as(CascadingPageData.class).setNavigation();
        site.as(CascadingPageData.class).setFooter();

        site.as(NavigationSearchSettings.class).setSearchPage();

        site.as(TypeSpecificLandingPageElementsSettingsModification.class).setTypeSpecificLandingPageContent();
        site.as(TypeSpecificOverridesSettings.class).setTypeSpecificOverrides();

        site.as(ActionBarSettings.class).setActionBarSettings();

        site.as(ErrorHandlerSettings.class).setErrorHandlers();

        site.setCmsLogo();
        */
        SiteLocaleProvider siteLocaleProvider = new SiteLocaleProvider();
        siteLocaleProvider.setLocale(Locale.US);
        site.as(LocaleSettings.class).setLocaleProvider(siteLocaleProvider);
        site.as(AvailableLocaleSiteSettings.class).getAvailableLanguageTags().add(Locale.US.toLanguageTag());

        return site;
    }

    private static Theme setUpTheme() {

        Theme theme = new Theme();
        theme.setName("Training Theme");

        Bundleable bundle = Objects.requireNonNull(Query.from(JarBundle.class).first());

        FixedRelease release = new FixedRelease();
        release.setBundle(bundle);
        theme.setStableRelease(release);

        StylePackage stylePackage = new StylePackage();
        stylePackage.setBuiltIn(new BuiltInStyle("style-1"));
        theme.as(StyleThemeModification.class).setStylePackage(stylePackage);

        return theme;
    }

    private static void importContent(ToolUser importUser) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(new File("/code/export-training.json"))) {

            @SuppressWarnings("unchecked")
            List<Object> data = (List<Object>) ObjectUtils.fromJson(IoUtils.toByteArray(inputStream));

            Pipeline.create("Import content")

                .stage(Read.from(Object.class)
                    .description("Read import data")
                    .iterable(data))

                .stage(Transform.make(Object.class, Recordable.class)
                    .transformer(Setup::createRecord))

                .stage(Write.make(Recordable.class)
                    .operation(WriteOperation.SAVE_UNSAFELY))

                .stage(Transform.make(Recordable.class, History.class)
                    .transformer(r -> new History(importUser, r)))

                .stage(Write.make(History.class)
                    .operation(WriteOperation.SAVE_UNSAFELY))

                .build()
                .start(new Date().toString());
        }
    }

    private static Recordable createRecord(Object object) {

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) object;

        List<Map<String, String>> pathData = ObjectUtils.to(
            new TypeReference<List<Map<String, String>>>() { },
            data.remove("import.paths"));

        UUID typeId = ObjectUtils.to(UUID.class, CollectionUtils.getByPath(data, "_type"));

        Recordable recordable = (Recordable) ObjectType.getInstance(typeId)
            .createObject(ObjectUtils.to(UUID.class, data.get("_type")));

        State state = recordable.getState();
        state.setResolveToReferenceOnly(true);
        state.setValues(data);

        if (pathData != null) {
            processPaths(state, pathData);
        }

        return (Recordable) state.getOriginalObject();
    }

    private static void processPaths(State state, List<Map<String, String>> paths) {
        for (Map<String, String> pathData : paths) {
            String path = pathData.get("path");
            Directory.PathType pathType = Directory.PathType.valueOf(pathData.get("pathType"));

            state.as(Directory.ObjectModification.class).addSitePath(
                state.as(Site.ObjectModification.class).getOwner(),
                path,
                pathType);
        }
    }
}
