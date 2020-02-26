package brightspot.core.tool;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletException;

import com.psddev.cms.db.Content;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.tool.Dashboard;
import com.psddev.cms.tool.DashboardWidget;
import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.JspUtils;
import com.psddev.dari.util.StringUtils;

@Recordable.DisplayName("Search Widget")
public class SearchDashboardWidget extends DashboardWidget {

    @Required
    private String heading;

    @Where(Content.TYPE_PREDICATE_STRING)
    private Set<ObjectType> types;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public Set<ObjectType> getTypes() {
        if (types == null) {
            types = new LinkedHashSet<>();
        }
        return types;
    }

    public void setTypes(Set<ObjectType> types) {
        this.types = types;
    }

    @Override
    public void writeHtml(ToolPageContext page, Dashboard dashboard) throws IOException, ServletException {
        page.writeStart("div", "class", "widget SearchWidget");
        {
            page.writeStart("h1", "class", "SearchWidget-heading");
            page.writeHtml(getHeading());
            page.writeEnd();

            String searchPath = StringUtils.addQueryParameters(
                page.toolPath(CmsTool.class, "/WEB-INF/search.jsp"),
                Search.TYPES_PARAMETER,
                getTypes().stream().map(ObjectType::getId).collect(Collectors.toList()));

            JspUtils.include(
                page.getRequest(),
                page.getResponse(),
                page,
                searchPath,
                "name", "fullScreen",
                "newJsp", "/content/edit.jsp",
                "newTarget", "_top",
                "resultJsp", "/misc/searchResult.jsp");
        }
        page.writeEnd();
    }
}
