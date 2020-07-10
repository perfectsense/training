package brightspot.core.tool;

import java.util.Arrays;
import java.util.List;

import com.psddev.cms.rte.RichTextToolbar;
import com.psddev.cms.rte.RichTextToolbarAction;
import com.psddev.cms.rte.RichTextToolbarItem;
import com.psddev.cms.rte.RichTextToolbarSeparator;
import com.psddev.cms.rte.RichTextToolbarStyle;

public class MediumRichTextToolbar implements RichTextToolbar {

    @Override
    public List<RichTextToolbarItem> getItems() {
        return Arrays.asList(
            RichTextToolbarStyle.BOLD,
            RichTextToolbarStyle.ITALIC,
            RichTextToolbarStyle.UNDERLINE,
            RichTextToolbarStyle.STRIKETHROUGH,
            RichTextToolbarStyle.SUPERSCRIPT,
            RichTextToolbarStyle.SUBSCRIPT,
            RichTextToolbarAction.CLEAR,

            RichTextToolbarSeparator.BLOCK,
            RichTextToolbarStyle.ALIGN_LEFT,
            RichTextToolbarStyle.ALIGN_CENTER,
            RichTextToolbarStyle.ALIGN_RIGHT,

            RichTextToolbarSeparator.BLOCK,
            RichTextToolbarStyle.UL,
            RichTextToolbarStyle.OL,
            RichTextToolbarAction.INDENT,
            RichTextToolbarAction.OUTDENT,

            RichTextToolbarSeparator.INLINE,
            RichTextToolbarStyle.HTML,
            RichTextToolbarStyle.LINK,
            RichTextToolbarItem.UPLOAD,
            RichTextToolbarItem.ELEMENTS,

            RichTextToolbarSeparator.BLOCK,
            RichTextToolbarAction.TABLE,

            RichTextToolbarSeparator.INLINE,
            RichTextToolbarAction.KEYBOARD,

            RichTextToolbarSeparator.INLINE,
            RichTextToolbarAction.FULLSCREEN
        );
    }
}
