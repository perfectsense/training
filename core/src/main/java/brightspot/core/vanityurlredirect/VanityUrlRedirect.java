package brightspot.core.vanityurlredirect;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Global;
import com.psddev.cms.db.Managed;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

import static brightspot.core.tool.ToolUserUtils.*;

/**
 * A VanityUrlRedirect object consists of at least one site path to send to one destination url. A redirect can be
 * permanent or temporary.
 */
@Recordable.LabelFields({ "getSearchableText", "destination" })
public class VanityUrlRedirect extends Record implements Global, Managed {

    @Indexed
    @ToolUi.Placeholder(dynamicText = "${content.namePlaceholder}", editable = true)
    private String name;

    @Required
    @Indexed
    @ToolUi.Note("Only requires path e.g. /mypage")
    private Set<String> localUrls;

    @Required
    @Indexed
    @ToolUi.Placeholder(value = "http://", editable = true)
    private String destination;

    private Boolean temporary;

    public String getName() {
        if (name == null) {
            return getNamePlaceholder();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getNamePlaceholder() {
        StringBuilder s = new StringBuilder();
        if (!localUrls.isEmpty()) {
            s.append(localUrls.iterator().next()).append(" -> ");
        }
        if (destination != null) {
            s.append(destination);
        }
        return s.toString();
    }

    public Set<String> getLocalUrls() {
        if (localUrls == null) {
            return new HashSet<>();
        }
        return localUrls;
    }

    public void setLocalUrls(Set<String> localUrls) {
        this.localUrls = localUrls;
    }

    /**
     * Ensures that the destination is in the form of a valid URL.
     */
    @Override
    protected void onValidate() {
        if (destination != null) {
            try {
                URL test = new URL(destination);
            } catch (MalformedURLException e) {
                getState().addError(getState().getField("destination"), ("Invalid URL, ").concat(e.getMessage()));
            }
        }
    }

    /**
     * Adds valid Site path(s) for the given published {@code locaUrls}, ensuring that each is prefixed with a leading
     * slash character.
     */
    @Override
    protected void beforeSave() {
        super.beforeSave();

        Set<String> temp = new HashSet<>();
        for (String url : localUrls) {
            url = StringUtils.ensureStart(url, "/");
            temp.add(url);

            Site owner = getCurrentToolUser() != null ? getCurrentToolUser().getCurrentSetSite() : null;
            as(Directory.Data.class).addPath(owner, url, Directory.PathType.PERMALINK);
            as(Site.ObjectModification.class).setOwner(owner);
        }
        localUrls = temp;

        if (name == null) {
            name = getNamePlaceholder();
        }
    }

    public boolean isTemporary() {
        return Boolean.TRUE.equals(temporary);
    }

    public void setTemporary(boolean temporary) {
        this.temporary = Boolean.TRUE.equals(temporary) ? temporary : null;
    }

    @Override
    public String createManagedEditUrl(ToolPageContext page) {
        return page.cmsUrl("admin/vanity-url-redirect", "id", this.getId());
    }

    /**
     * Concatenates all localUrls into a singular string in order to enable advanced search to work for localUrls that
     * do not appear in the name.
     * <p>
     * Called via class annotation @Recordable.getLabelFields()
     */
    @Indexed
    @ToolUi.Hidden
    public String getSearchableText() {
        StringBuilder s = new StringBuilder("");
        if (localUrls != null) {
            localUrls.forEach(url -> s.append(url).append(" "));
        }
        return s.toString();
    }

    @Override
    public String getLabel() {
        return getName();
    }

}
