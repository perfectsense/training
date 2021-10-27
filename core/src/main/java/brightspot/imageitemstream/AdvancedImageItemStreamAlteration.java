package brightspot.imageitemstream;

import java.util.List;

import com.psddev.cms.db.ToolUi;
import com.psddev.crosslinker.db.Crosslinkable;
import com.psddev.dari.db.Alteration;

public class AdvancedImageItemStreamAlteration extends Alteration<AdvancedImageItemStream> {

    @Crosslinkable.Crosslinked
    @ToolUi.Unlabeled
    private List<WebImageItem> items;
}
