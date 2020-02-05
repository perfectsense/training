package brightspot.core.article;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("List")
public class ListBody extends Body {

    @ToolUi.Unlabeled
    private List<ListItem> items;

    public List<ListItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }

    @Override
    public String getPlainTextPreview() {

        return getItems().stream()
            .filter(i -> !ObjectUtils.isBlank(i.getBody()))
            .map(ListItem::getBody)
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::getFirstBodyParagraph)
            .filter(p -> !ObjectUtils.isBlank(p))
            .findFirst()
            .orElse(null);
    }

    @Override
    public long getCharacterCount() {

        return getItems().stream()
            .map(ListItem::getBody)
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText)
            .mapToLong(String::length)
            .sum();
    }
}
