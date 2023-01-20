package brightspot.module.list.podcast;

import brightspot.itemstream.SiteItemsQueryModifiableWithField;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import brightspot.promo.page.PagePromotableDynamicQueryModifiable;
import com.psddev.dari.util.Substitution;

public class DynamicPodcastEpisodeItemStreamSubstitution extends DynamicPodcastEpisodeItemStream implements
    Substitution,
    LocaleDynamicQueryModifiable,
    PagePromotableDynamicQueryModifiable,
    SiteItemsQueryModifiableWithField {

}
