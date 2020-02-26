package brightspot.facebook.action;

import brightspot.core.action.actionbar.ActionBarItem;
import brightspot.facebook.FacebookSettingsModification;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

/**
 * Facebook Share Action model. Object that is added to Action Bar. ViewModel in {@link FacebookShareActionViewModel}
 */
@Recordable.DisplayName("Facebook Share")
@ToolUi.NoteHtml("<span data-dynamic-html='${content.getNoteHtml(toolPageContext)}'></span>")
public class FacebookShareAction extends Record implements ActionBarItem {

    @Override
    public String getLabel() {
        return "Facebook";
    }

    public String getNoteHtml(ToolPageContext page) {
        if (StringUtils.isBlank(SiteSettings.get(
            page.getSite(),
            item -> item.as(FacebookSettingsModification.class).getFacebookAppId()))) {
            return "Configuration of a Facebook app ID is required.";
        } else {
            return "No options.";
        }
    }
}
