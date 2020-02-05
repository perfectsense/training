package brightspot.core.promo;

import java.util.Date;
import java.util.Optional;

import brightspot.core.image.Image;
import brightspot.core.link.Linkable;
import brightspot.core.link.Target;
import brightspot.core.person.Authorable;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.DataModelWrapper;
import com.psddev.dari.util.StringUtils;

public class PromoWrapper implements DataModelWrapper {

    protected final Object asset;
    protected final Promo overrides;
    protected String title;
    protected String description;
    protected Object media;
    protected String target;

    public PromoWrapper(Object asset, Promo overrides) {
        if (overrides == null) {
            throw new IllegalArgumentException("Overrides cannot be null!");
        }

        this.asset = asset;
        this.overrides = overrides;

        title = overrides.getTitle();
        description = overrides.getDescription();
        media = overrides.getImage();

        if (asset instanceof Promotable) {
            Promotable promotable = (Promotable) asset;
            if (StringUtils.isBlank(title)) {
                title = promotable.getPromotableTitle();
            }

            if (StringUtils.isBlank(description)) {
                description = promotable.getPromotableDescription();
            }

            if (media == null) {
                media = promotable.getPromotableImage();
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl(Site site) {
        return asset instanceof Linkable
            ? ((Linkable) asset).getLinkableUrl(site)
            : null;
    }

    public Object getMedia() {
        return media;
    }

    public String getTarget() {
        if (StringUtils.isBlank(target)) {
            target = Optional.ofNullable(overrides)
                .map(Promo::getLinkTarget)
                .map(Target::getValue)
                .orElse(null);
        }
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return asset instanceof Promotable
            ? ((Promotable) asset).getPromotableType()
            : null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMedia(Object image) {
        this.media = image;
    }

    public String getDuration() {
        return asset instanceof Promotable
            ? ((Promotable) asset).getPromotableDuration()
            : null;
    }

    public String getCategory() {
        return asset instanceof Promotable
            ? ((Promotable) asset).getPromotableCategory()
            : null;
    }

    public String getCategoryUrl(Site site) {
        return asset instanceof Promotable
            ? ((Promotable) asset).getPromotableCategoryUrl(site)
            : null;
    }

    public Date getDate() {
        return asset instanceof Promotable
            ? ((Promotable) asset).getPromotableDate()
            : null;
    }

    public String getAuthorName() {
        return Authorable.getAuthorName(asset);
    }

    public String getAuthorUrl(Site site) {
        return Authorable.getAuthorUrl(site, asset);
    }

    public Optional<Image> getAuthorImage() {
        return Authorable.getAuthorImage(asset);
    }

    public Object getAsset() {
        return asset;
    }

    @Override
    public Object unwrap() {
        return overrides;
    }
}
