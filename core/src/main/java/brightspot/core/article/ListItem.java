package brightspot.core.article;

import brightspot.core.tool.LargeRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class ListItem extends Record {

    private String headline;

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class, lines = 10)
    private String body;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
