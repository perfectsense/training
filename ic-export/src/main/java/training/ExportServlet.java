package training;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import brightspot.core.article.Article;
import brightspot.core.gallery.Gallery;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.PageRequest;
import com.psddev.cms.view.ViewTemplateLoader;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.RoutingFilter;
import com.psddev.dari.web.WebRequest;

@RoutingFilter.Path("/export-training")
public class ExportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Site site = Query.from(Site.class)
            .where("_id = ?", ExportUtils.IC_SITE_ID)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Inspire Confidence site not found"));

        ViewTemplateLoader templateLoader = WebRequest.getCurrent()
            .as(PageRequest.class)
            .getViewTemplateLoader();

        List<Map<String, Object>> recordData = new ArrayList<>();

        Query.from(Article.class)
            .where("cms.site.owner = ?", site)
            .and(Directory.Static.hasPathPredicate())
            .noCache()
            .selectAll()
            .forEach(a -> Optional.ofNullable(ExportUtils.export(a, templateLoader))
                .ifPresent(recordData::add));

        Query.from(Gallery.class)
            .where("cms.site.owner = ?", site)
            .and(Directory.Static.hasPathPredicate())
            .noCache()
            .selectAll()
            .forEach(g -> Optional.ofNullable(ExportUtils.export(g, templateLoader))
                .ifPresent(recordData::add));

        Queue<UUID> refs = WebRequest.getCurrent().as(ExportRefsWebExtension.class).getRefs();
        while (!refs.isEmpty()) {
            Query.from(Record.class)
                .where("_id = ?", refs.remove())
                .findFirst()
                .map(r -> ExportUtils.export(r, templateLoader))
                .ifPresent(recordData::add);
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(ObjectUtils.toJson(recordData));
    }
}
