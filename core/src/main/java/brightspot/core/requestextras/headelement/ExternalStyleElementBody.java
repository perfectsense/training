package brightspot.core.requestextras.headelement;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Link")
public class ExternalStyleElementBody extends StyleElementBody {

    @ToolUi.NoteHtml("URL to CSS resource")
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
