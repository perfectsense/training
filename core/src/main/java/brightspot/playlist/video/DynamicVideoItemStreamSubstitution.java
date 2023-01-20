package brightspot.playlist.video;

import brightspot.itemstream.MemoizationDynamicQueryModifiable;
import brightspot.itemstream.SiteItemsQueryModifiableWithField;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import com.psddev.dari.util.Substitution;

public class DynamicVideoItemStreamSubstitution extends DynamicVideoItemStream implements
        Substitution,
        LocaleDynamicQueryModifiable,
        MemoizationDynamicQueryModifiable,
        SiteItemsQueryModifiableWithField {

}
