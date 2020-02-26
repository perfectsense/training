package brightspot.core.article;

import java.util.Map;
import java.util.Optional;

import brightspot.core.tool.LargeRichTextToolbar;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Recordable.DisplayName("Rich Text")
public class RichTextBody extends Body implements Interchangeable {

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class, lines = 10)
    @ToolUi.Unlabeled
    private String richText;

    @Relocate
    public String getRichText() {
        relocateLegacyEmbeds();
        return richText;
    }

    public void setRichText(String richText) {
        this.richText = richText;
    }

    @Override
    public String getPlainTextPreview() {

        return Optional.ofNullable(getRichText())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::getFirstBodyParagraph)
            .map(RichTextUtils::richTextToPlainText)
            .orElse(null);
    }

    @Override
    public boolean loadTo(Object newObject) {
        if (newObject instanceof ListBody && !StringUtils.isBlank(getRichText())) {
            ListItem listItem = new ListItem();
            listItem.setBody(getRichText());
            ((ListBody) newObject).getItems().add(listItem);
            return true;
        }
        return false;
    }

    @Override
    public long getCharacterCount() {

        return Optional.ofNullable(getRichText())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText)
            .map(String::length)
            .orElse(0);
    }

    // if any social RTEs exist in rich text body, call relocateLegacySocialEmbed
    private void relocateLegacyEmbeds() {
        for (String tagName : new String[]{"bsp-facebook", "bsp-instagram", "bsp-tweet"}) {
            if (richText != null && richText.contains(tagName)) {
                richText = relocateLegacySocialEmbed(tagName);
            }
        }
    }

    // converts given RTE into generically embedded element
    private String relocateLegacySocialEmbed(String tagName) {
        if (tagName == null) {
            return null;
        }
        Document richTextDoc = RichTextUtils.documentFromRichText(richText);
        String serviceName = StringUtils.removeStart(tagName, "bsp-");

        for (Element element : richTextDoc.select(tagName)) {
            // create new element of type `brightspot-cms-external-content`
            Element replacementElem = new Element("brightspot-cms-external-content");

            // carry over existing attributes
            element.attributes().asList().forEach(e -> {
                String key = e.getKey();
                if (!"data-state".equals(key)) {
                    replacementElem.attr(key, e.getValue());
                }
            });

            // extricate url from data-state attribute
            Map stateData = ObjectUtils.to(Map.class, ObjectUtils.fromJson(element.attr("data-state")));
            if (stateData != null) {
                Map postData = ObjectUtils.to(Map.class, stateData.get("tweet".equals(serviceName) ? serviceName : (serviceName + "Post")));
                String url = "";
                if (postData != null) {
                    Object urlData = postData.get("postUrl");
                    url = Optional.ofNullable(urlData)
                            .map(Object::toString)
                            .map(String::trim)
                            .orElse("");
                }
                replacementElem.attr("url", url);
                replacementElem.text(url);
            }

            // relocate
            element.replaceWith(replacementElem);
        }
        return richTextDoc.html();
    }
}
