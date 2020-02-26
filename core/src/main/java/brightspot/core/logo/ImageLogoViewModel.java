package brightspot.core.logo;

import java.util.Map;
import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.link.Link;
import brightspot.core.link.Target;
import com.psddev.cms.db.Site;
import com.psddev.cms.image.ImageSize;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.page.PageLogoView;

public class ImageLogoViewModel
    extends ViewModel<ImageLogo>
    implements PageLogoView {

    @CurrentSite
    protected Site site;

    @Override
    public Map<String, ?> getImage() {
        return Optional.ofNullable(model.getImageOption())
            .map(ImageOption::getImageOptionFile)
            .map(ImageSize::getAttributes)
            .orElse(null);
    }

    @Override
    public CharSequence getAlt() {
        return Optional.ofNullable(model.getImageOption())
            .map(ImageOption::getImageOptionAltText)
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
