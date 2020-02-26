package brightspot.core.social;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.styleguide.core.social.SocialBarView;
import com.psddev.styleguide.core.social.SocialBarViewItemsField;
import com.psddev.styleguide.core.social.SocialLinkView;

/**
 * ViewModel of {@link Recordable}, intended for {@link SocialEntity} and {@link SiteSettings} only.
 */
public class SocialBarViewModel extends ViewModel<Recordable> implements SocialBarView {

    private List<SocialLinkView> items;

    @Override
    protected boolean shouldCreate() {

        if (!model.isInstantiableTo(SocialEntity.class) && !model.isInstantiableTo(SiteSettings.class)) {
            return false;
        }

        items = getItemsPrivate();

        if (items == null || items.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * This streams the {@link SocialService} entries and creates a {@link SocialLinkView} for each item that contains a
     * URL.
     *
     * @return Iterable of SocialLinkViews
     */
    @Override
    public Iterable<? extends SocialBarViewItemsField> getItems() {
        return items;
    }

    private List<SocialLinkView> getItemsPrivate() {
        return Query.from(SocialService.class).selectAll().stream()
            .map(s -> s.toView(model.as(SocialEntityData.class)))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
