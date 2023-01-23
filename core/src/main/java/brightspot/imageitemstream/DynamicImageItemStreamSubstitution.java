package brightspot.imageitemstream;

import brightspot.itemstream.MemoizationDynamicQueryModifiable;
import brightspot.itemstream.SiteItemsQueryModifiableWithField;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import com.psddev.dari.util.Substitution;

public class DynamicImageItemStreamSubstitution extends DynamicImageItemStream implements
        Substitution,
        LocaleDynamicQueryModifiable,
        MemoizationDynamicQueryModifiable,
        SiteItemsQueryModifiableWithField {

}
