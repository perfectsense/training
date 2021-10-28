package training;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@JsonView
@ViewInterface
public abstract class ExportViewModel<T extends Recordable> extends ViewModel<T> {

    private static final UUID TOOL_USER_ID = UUID.nameUUIDFromBytes(
        "Inspire Confidence import".getBytes(StandardCharsets.UTF_8));

    // --- Globals ---

    @ViewKey("cms.content.publishDate")
    public final Long getPublishDate() {
        return Optional.ofNullable(model.as(Content.ObjectModification.class).getPublishDate())
            .map(Date::getTime)
            .orElse(null);
    }

    @ViewKey("cms.content.publishUser")
    public final RefView getPublishUser() {
        if (model.as(Content.ObjectModification.class).getPublishUser() == null) {
            return null;
        }

        ToolUser user = new ToolUser();
        user.getState().setId(TOOL_USER_ID);
        return createView(RefView.class, user);
    }

    @ViewKey("cms.content.updateDate")
    public final Long getUpdateDate() {
        return Optional.ofNullable(model.as(Content.ObjectModification.class).getUpdateDate())
            .map(Date::getTime)
            .orElse(null);

    }

    @ViewKey("cms.content.updateUser")
    public final RefView getUpdateUser() {
        if (model.as(Content.ObjectModification.class).getUpdateUser() == null) {
            return null;
        }

        ToolUser user = new ToolUser();
        user.getState().setId(TOOL_USER_ID);
        return createView(RefView.class, user);
    }

    @ViewKey("import.paths")
    public final Iterable<PathViewModel> getPaths() {
        Set<Directory.Path> paths = model.as(Directory.Data.class).getPaths();
        if (paths.isEmpty()) {
            return null;
        }
        return createViews(PathViewModel.class, paths);
    }

    @ViewKey(Site.OWNER_FIELD)
    public final RefView getOwnerSite() {
        Site owner = model.as(Site.ObjectModification.class).getOwner();
        if (owner == null) {
            return null;
        }

        if (!ExportUtils.IC_SITE_ID.equals(owner.getId())) {
            owner = Query.findById(Site.class, ExportUtils.IC_SITE_ID);
        }

        return createView(RefView.class, owner);
    }

    @ViewKey("_id")
    public String getId() {
        return model.getState().getId().toString();
    }

    @ViewKey("_type")
    public String getType() {
        return ExportUtils.getExportType(model);
    }
}
