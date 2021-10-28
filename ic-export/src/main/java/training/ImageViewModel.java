package training;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import brightspot.core.image.Image;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.text.StringEscapeUtils;

@JsonView
@ViewInterface
public class ImageViewModel extends ExportViewModel<Image> implements ExportEntryView {

    @ViewKey("internalName")
    public String getInternalName() {
        return ObjectUtils.to(String.class, model.getState().get("title"));
    }

    @ViewKey("file")
    public Object getFile() {
        return CollectionUtils.getByPath(model.getState().getSimpleValues(), "file");
    }

    @ViewKey("altTextOverride")
    public String getAltTextOverride() {
        return ObjectUtils.to(String.class, model.getState().get("altText"));
    }

    @ViewKey("captionOverride")
    public CharSequence getCaptionOverride() {
        return ExportUtils.processRichText(
            model,
            i -> ObjectUtils.to(String.class, i.getState().get("caption")));
    }

    @ViewKey("creditOverride")
    public CharSequence getCreditOverride() {
        return ExportUtils.processRichText(
            model,
            i -> ObjectUtils.to(String.class, i.getState().get("credit")));
    }

    @ViewKey("sourceOverride")
    public String getSourceOverride() {
        return StringEscapeUtils.escapeHtml4(ObjectUtils.to(String.class, model.getState().get("source")));
    }

    @ViewKey("copyrightNoticeOverride")
    public String getCopyrightNoticeOverride() {
        return StringEscapeUtils.escapeHtml4(ObjectUtils.to(String.class, model.getState().get("copyrightNotice")));
    }

    @ViewKey("dateTaken")
    public Long getDateTaken() {
        return Optional.ofNullable(model.getDateTaken())
            .map(Date::getTime)
            .orElse(null);
    }

    @ViewKey("dateUploaded")
    public Long getDateUploaded() {
        return Optional.ofNullable(model.getDateUploaded())
            .map(Date::getTime)
            .orElse(null);
    }

    @ViewKey("keywords")
    public List<String> getKeywords() {
        return new ArrayList<>(model.getKeywords());
    }

    @ViewKey("width")
    public Integer getWidth() {
        return model.getWidth();
    }

    @ViewKey("height")
    public Integer getHeight() {
        return model.getHeight();
    }

    @ViewKey("location")
    public Map<String, Double> getLocation() {
        return Optional.ofNullable(model.getLocation())
            .map(l -> ImmutableMap.of(
                "x", l.getX(),
                "y", l.getY()))
            .orElse(null);
    }

    @ViewKey("hasTags.tags")
    public Iterable<RefView> getTags() {
        return createViews(RefView.class, model.asTaggableData().getTags());
    }
}
