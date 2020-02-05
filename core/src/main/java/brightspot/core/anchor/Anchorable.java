package brightspot.core.anchor;

import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

public interface Anchorable extends Recordable {

    String INTERNAL_NAME = "brightspot.core.anchor.Anchorable";
    String ANCHOR_PREFIX = "anchor-";

    String getAnchorableAnchor();

    static String toAnchor(String anchorTitle) {
        return anchorTitle != null ? ANCHOR_PREFIX + StringUtils.toNormalized(anchorTitle) : null;
    }
}
