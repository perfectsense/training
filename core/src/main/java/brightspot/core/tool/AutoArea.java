package brightspot.core.tool;

/**
 * Interface for {@link com.psddev.cms.tool.PageServlet}s to be automatically instantiated and added to the
 * application's menu.
 */
public interface AutoArea extends AutoPlugin {

    /**
     * Example: My Custom Search
     */
    String getDisplayName();

    /**
     * Example: dashboard.myCustomSearch
     */
    String getInternalName();

    /**
     * Example: dashboard/myCustomSearch
     */
    String getHierarchy();

    /**
     * Example: /cms/myCustomSearch Note, this should align with the RoutingFilter annotation, example:
     * RoutingFilter.Path(application = "cms", value = "/myCustomSearch")
     */
    String getUrl();

}
