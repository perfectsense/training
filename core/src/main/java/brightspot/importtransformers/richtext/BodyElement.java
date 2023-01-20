package brightspot.importtransformers.richtext;

import java.io.IOException;
import java.util.Optional;

import brightspot.image.WebImage;
import brightspot.importapi.ImportTransformer;
import brightspot.module.iframe.IframeEmbed;
import brightspot.rte.iframe.IframeEmbedRichTextElement;
import brightspot.rte.image.ImageRichTextElement;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BodyElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(BodyElement.class);

    static final String TYPE_FIELD = "type";
    static final String TEXT_FIELD = "text";
    static final String NAME_FIELD = "name";
    static final String SRC_FIELD = "src";
    static final String WIDTH_FIELD = "width";
    static final String HEIGHT_FIELD = "height";
    static final String ALT_TEXT_FIELD = "altText";
    static final String CREDIT_FIELD = "credit";
    static final String CAPTION_FIELD = "caption";

    protected static final String DATA_ATTR = "data-state";

    private String type;

    private String text;

    private String name;

    private String src;

    private String width;

    private String height;

    private String altText;

    private String credit;

    private String caption;

    public String transform(ImportTransformer parentTransformer) {

        BodyElement.Type elementType = Optional.ofNullable(this.getType())
                .map(String::toUpperCase)
                .map(value -> {
                    try {
                        return BodyElement.Type.valueOf(value);
                    } catch (Exception ex) {
                        LOGGER.error("Unhandled Type: {}", value);
                        return null;
                    }
                }).orElse(null);

        if (elementType == null) {
            return null;
        }

        switch (elementType) {
            case IFRAME: return transformIframeElement();
            case IMAGE: return transformImageElement(parentTransformer);
            case TEXT:
            default: return transformTextElement();
        }
    }

    private String transformTextElement() {
        return Optional.ofNullable(this.getText())
                .orElse(null);
    }

    private String transformIframeElement() {

        if (StringUtils.isBlank(this.getSrc())) {
            return null;
        }

        IframeEmbed iframe = ImportTransformer.findByExternalId(IframeEmbed.class, this.getSrc(), (url) -> {
            IframeEmbed w = new IframeEmbed();
            w.setUrl(url);
            return w;
        });

        iframe.setIframeHeight(BodyTransformUtil.parseInteger(this.getHeight()));
        iframe.setIframeWidth(BodyTransformUtil.parseNumericString(this.getWidth()));

        iframe.setIframeName(this.getName());
        iframe.setInternalName(ObjectUtils.firstNonBlank(this.getName(), this.getSrc()));

        IframeEmbedRichTextElement iframeRte = new IframeEmbedRichTextElement();
        iframeRte.setShared(iframe);

        Element bspIframe = new Element(Tag.valueOf(IframeEmbedRichTextElement.TAG_NAME), "");
        bspIframe.attr(DATA_ATTR, iframeRte.toAttributes().get(DATA_ATTR));
        bspIframe.text(iframeRte.toBody());

        return bspIframe.toString();

    }

    private String transformImageElement(ImportTransformer parentTransformer) {

        if (StringUtils.isBlank(this.getSrc())) {
            return null;
        }

        WebImage image = ImportTransformer.findByExternalId(WebImage.class, this.getSrc(), (url) -> {
            try {
                WebImage w = new WebImage();
                w.setFile(parentTransformer.createStorageItemFromUrl(url));
                return w;
            } catch (IOException ex) {
                return null;
            }
        });

        image.setAltTextOverride(this.getAltText());
        image.setCreditOverride(this.getCredit());
        image.setCaptionOverride(this.getCaption());

        image.setHeight(BodyTransformUtil.parseInteger(this.getHeight()));
        image.setWidth(BodyTransformUtil.parseInteger(this.getWidth()));

        ImageRichTextElement imageRte = new ImageRichTextElement();
        imageRte.setImage(image);

        Element bspImage = new Element(Tag.valueOf(ImageRichTextElement.TAG_NAME), "");
        bspImage.attr(DATA_ATTR, imageRte.toAttributes().get(DATA_ATTR));
        bspImage.text(imageRte.toBody());

        return bspImage.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public enum Type {
        TEXT, IFRAME, IMAGE
    }
}
