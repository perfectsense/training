package brightspot.importtransformers.element;

import java.util.Optional;

import brightspot.importapi.ImportTransformer;
import brightspot.importapi.ImportingDatabase;
import brightspot.importapi.element.ImportElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Database;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

/**
 * An import element to convert from a legacy image into a BSP Image Embed. Matches to '_type': Image
 */
public class IframeImportElement extends ImportElement {

    private static final String SRC_FIELD = "src";
    private static final String NAME_FIELD = "name";
    private static final String WIDTH_FIELD = "width";
    private static final String HEIGHT_FIELD = "height";

    @JsonProperty(SRC_FIELD)
    private String src;

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(WIDTH_FIELD)
    private String width;

    @JsonProperty(HEIGHT_FIELD)
    private String height;

    public String transform(ImportTransformer<?> transformer) {

        if (StringUtils.isBlank(this.getSrc())) {
            return null;
        }

        String iframeSrc = this.getSrc().trim();

        Site importSite = ((ImportingDatabase) Database.Static.getDefault()).getSite();

        if (ImportElementUtil.isSupportedVideoPlayer(iframeSrc)) {
            return Optional.ofNullable(ImportElementUtil.createVideo(iframeSrc, importSite))
                .map(ImportElementUtil::createVideoElement)
                .map(Element::toString)
                .orElse(StringUtils.EMPTY);

        } else if (ImportElementUtil.isSupportedExternalContent(iframeSrc)) {
            return Optional.of(ImportElementUtil.createExternalContentElement(iframeSrc))
                .map(Element::toString)
                .orElse(StringUtils.EMPTY);

        } else {
            return Optional.ofNullable(ImportElementUtil.createIframe(
                    iframeSrc,
                    importSite,
                    this.getName(),
                    this.getHeight(),
                    this.getWidth()))
                .map(ImportElementUtil::createIframeElement)
                .map(Element::toString)
                .orElse(StringUtils.EMPTY);
        }
    }

    public String getSrc() {
        return src;
    }

    public String getName() {
        return name;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }
}
