package brightspot.itemstream.page;

import brightspot.itemstream.SiteItemsQueryModifiableWithField;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import brightspot.module.list.page.DynamicPageItemStream;
import com.psddev.dari.util.Substitution;

public class DynamicPageItemStreamSubstitution extends DynamicPageItemStream implements
        Substitution,
        LocaleDynamicQueryModifiable,
        SiteItemsQueryModifiableWithField {

}
