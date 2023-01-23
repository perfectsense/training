package brightspot.logo;

import java.net.URL;
import java.util.Optional;
import javax.annotation.Nonnull;

import brightspot.image.WebImage;
import brightspot.link.ExternalLink;
import brightspot.link.Link;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.PageRequest;
import com.psddev.cms.ui.form.Note;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.web.WebRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Recordable.PreviewField("image/file")
public class ImageLogo extends Logo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageLogo.class);

    private static final String PATH_DELIMITER = "/";

    @Required
    private String internalName;

    @Required
    private WebImage image;

    @Note("Optional.  If not specified, will link to the site index page ('/').")
    private Link link;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public WebImage getImage() {
        return image;
    }

    public void setImage(WebImage image) {
        this.image = image;
    }

    public Link getLink() {
        return Optional.ofNullable(link)
            .orElseGet(this::getFallbackLink);
    }

    public Link getFallbackLink() {
        return ExternalLink.create(getCurrentSiteRelativePrimaryUrl());
    }

    public void setLink(Link link) {
        this.link = link;
    }

    @Override
    public String getLabel() {
        return getInternalName();
    }

    @Nonnull
    protected static String getCurrentSiteRelativePrimaryUrl() {

        if (!WebRequest.isAvailable()) {
            return PATH_DELIMITER;
        }

        return getRelativePrimaryUrl(WebRequest.getCurrent()
            .as(PageRequest.class)
            .getCurrentSite()
        );
    }

    @Nonnull
    private static String getRelativePrimaryUrl(Site site) {

        return Optional.ofNullable(site)
            .map(Site::getPrimaryUrl)
            .filter(StringUtils::isNotBlank)
            .map(primaryUrl -> {
                try {
                    return Optional.of(new URL(primaryUrl))
                        .map(URL::getPath)
                        .filter(StringUtils::isNotBlank)
                        .orElse(null);
                } catch (Exception e) {
                    LOGGER.warn("exception getting path from url: {}", primaryUrl, e);
                }
                return null;
            })
            .map(path -> StringUtils.appendIfMissing(path, PATH_DELIMITER))
            .orElse(PATH_DELIMITER);
    }
}
