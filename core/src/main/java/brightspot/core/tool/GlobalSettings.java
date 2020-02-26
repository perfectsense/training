package brightspot.core.tool;

import java.util.ArrayList;
import java.util.List;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.tool.Plugin;
import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.Tool;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Application;
import com.psddev.dari.util.ClassFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This only exists to power {@link #getPlugins()}} and {@link #initializeSearch(Search, ToolPageContext)} until they
 * can be supplied by something else.
 */
@ToolUi.Hidden
@Deprecated
public class GlobalSettings extends Tool {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalSettings.class);

    @Override
    public List<Plugin> getPlugins() {
        List<Plugin> plugins = new ArrayList<>();

        // AutoPlugin
        for (Class<? extends AutoPlugin> cls : ClassFinder.findConcreteClasses(AutoPlugin.class)) {
            if (!Application.Static.getInstance(CmsTool.class)
                .as(CmsToolModification.class)
                .getDisabledCustomPlugins()
                .contains(cls.getName())) {
                try {
                    AutoPlugin plugin = cls.newInstance();
                    if (plugin instanceof Plugin) {
                        plugins.add((Plugin) plugin);
                    } else if (plugin instanceof AutoArea) {
                        AutoArea autoArea = (AutoArea) plugin;
                        plugins.add(createArea2(
                            autoArea.getDisplayName(),
                            autoArea.getInternalName(),
                            autoArea.getHierarchy(),
                            autoArea.getUrl()));
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.error("Error instantiating " + cls.getName(), e);
                }
            }
        }

        return plugins;
    }

    @Override
    public void initializeSearch(Search search, ToolPageContext page) {

        // hook to initialize Search within the context of an AbstractSearchPage

        for (Class<? extends AbstractSearchPage> cls : ClassFinder.findConcreteClasses(AbstractSearchPage.class)) {
            if (!Application.Static.getInstance(CmsTool.class)
                .as(CmsToolModification.class)
                .getDisabledCustomPlugins()
                .contains(cls.getName())) {
                try {
                    AbstractSearchPage searchPage = cls.newInstance();
                    searchPage.initializeSearch(search);

                } catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.error("Error instantiating " + cls.getName(), e);
                }
            }
        }

        for (Class<? extends SearchInitializer> cls : ClassFinder.findConcreteClasses(SearchInitializer.class)) {

            try {
                SearchInitializer searchInitializer = cls.newInstance();
                searchInitializer.initializeSearch(search, page);
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Error instantiating " + cls.getName(), e);
            }
        }
    }
}
