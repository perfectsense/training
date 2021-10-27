package brightspot.article;

import com.psddev.cms.db.ToolUi;

@ToolUi.CompatibleTypes({Article.class, Listicle.class})
@ToolUi.IconName("format_list_numbered")
public class Listicle extends AbstractRichTextArticle {

    public static final String TAB_OVERRIDES = "Overrides";

    @ToolUi.Tab(TAB_OVERRIDES)
    @ToolUi.Note("Only applicable if item navigation is visible.")
    private boolean startNumberingAtTop;

    public boolean isStartNumberingAtTop() {
        return startNumberingAtTop;
    }

    public void setStartNumberingAtTop(boolean startNumberingAtTop) {
        this.startNumberingAtTop = startNumberingAtTop;
    }
}
