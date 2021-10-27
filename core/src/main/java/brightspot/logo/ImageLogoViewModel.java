package brightspot.logo;

import java.util.Map;
import java.util.Optional;

import brightspot.image.WebImage;
import brightspot.link.Link;
import brightspot.link.Target;
import com.psddev.cms.db.Site;
import com.psddev.cms.image.ImageSize;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.page.PageLogoView;

public class ImageLogoViewModel
    extends ViewModel<ImageLogo>
    implements PageLogoView {

    @CurrentSite
    protected Site site;

    @Override
    public Map<String, ?> getImage() {
        return Optional.ofNullable(model.getImage())
            .map(WebImage::getFile)
            .map(ImageSize::getAttributes)
            .orElse(null);
    }

    @Override
    public CharSequence getAlt() {
        return Optional.ofNullable(model.getImage())
            .map(WebImage::getAltText)
            .orElse(null);
    }

    @Override
    public CharSequence getHref() {
        return Optional.ofNullable(model.getLink())
            .map(link -> link.getLinkUrl(site))
            .orElse(null);
    }

    @Override
    public CharSequence getTarget() {
        return Optional.ofNullable(model.getLink())
            .map(Link::getTarget)
            .map(Target::getValue)
            .orElse(null);
    }
}
