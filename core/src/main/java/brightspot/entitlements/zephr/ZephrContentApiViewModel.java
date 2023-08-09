package brightspot.entitlements.zephr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import brightspot.author.Author;
import brightspot.author.HasAuthors;
import brightspot.section.HasSection;
import brightspot.section.Section;
import brightspot.sponsoredcontent.ContentSponsor;
import brightspot.sponsoredcontent.HasSponsor;
import brightspot.tag.HasTags;
import brightspot.tag.Tag;
import com.psddev.cms.api.ApiRequest;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Singleton;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.web.WebRequest;

/**
 * ViewModel to pass data to the {@link ZephrContentApi}.
 */
@ViewInterface
@JsonView
public class ZephrContentApiViewModel extends ViewModel<Record> implements ZephrContentApiEntryView {

    private static final String ZEPHR_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DELIMITER = ",";

    public Map<String, Object> getExtras() {
        State state = model.getState();
        return getCustomVariables(state);
    }

    public Map<String, Object> getPrimarySectionExtras() {
        Section parentSection = model.as(HasSection.class).getSectionParent();
        if (parentSection != null) {
            return getCustomVariables(parentSection.getState());
        }
        return null;
    }

    public Map<String, Object> getSiteExtras() {
        SiteSettings site = ObjectUtils.firstNonNull(
            WebRequest.getCurrent().as(ApiRequest.class).getSite(),
            Singleton.getInstance(CmsTool.class));
        if (site != null) {
            return getCustomVariables(site.getState());
        }
        return null;
    }

    private Map<String, Object> getCustomVariables(State state) {
        Set<ObjectField> fieldSet = state.getType().getFields()
            .stream()
            .filter(field -> field.as(ZephrContentApiCustomVariableObjectFieldModification.class)
                .isZephrCustomVariable())
            .collect(Collectors.toSet());
        return fieldSet.stream()
            .collect(Collectors.toMap(
                ObjectField::getInternalName,
                field -> {
                    Object value = state.getByPath(field.getInternalName());
                    if (value instanceof Collection) {
                        return ((Collection) value).stream()
                            .map(this::customValueToValidOutput)
                            .collect(Collectors.joining(DELIMITER));
                    } else {
                        return customValueToValidOutput(value);
                    }
                }));
    }

    private String customValueToValidOutput(Object value) {
        if (value instanceof Recordable) {
            return ((Recordable) value).getState().getId().toString();
        } else if (value instanceof Date) {
            return new SimpleDateFormat(ZEPHR_DATE_TIME_FORMAT).format(value);
        } else {
            return ObjectUtils.to(String.class, value);
        }
    }

    public String getUpdateDate() {
        return Optional.ofNullable(model.as(Content.ObjectModification.class).getUpdateDate())
            .map(date -> {
                TimeZone tz = TimeZone.getTimeZone("UTC");
                DateFormat df = new SimpleDateFormat(ZEPHR_DATE_TIME_FORMAT);
                df.setTimeZone(tz);
                return df.format(date);
            })
            .orElse(null);
    }

    public String getPublishDate() {
        return Optional.ofNullable(model.as(Content.ObjectModification.class).getPublishDate())
            .map(date -> {
                TimeZone tz = TimeZone.getTimeZone("UTC");
                DateFormat df = new SimpleDateFormat(ZEPHR_DATE_TIME_FORMAT);
                df.setTimeZone(tz);
                return df.format(date);
            })
            .orElse(null);
    }

    public String getUpdateUser() {
        return Optional.ofNullable(model.as(Content.ObjectModification.class).getUpdateUser())
            .map(ToolUser::getUsername)
            .orElse(null);
    }

    public String getPublishUser() {
        return Optional.ofNullable(model.as(Content.ObjectModification.class).getPublishUser())
            .map(ToolUser::getUsername)
            .orElse(null);
    }

    public String getContentType() {
        return model.getState().getType().getInternalName();
    }

    public String getContentId() {
        return model.getId().toString();
    }

    public String getTagNames() {
        return Optional.of(model)
            .filter(HasTags.class::isInstance)
            .map(HasTags.class::cast)
            .map(HasTags::getTags)
            .map(tags -> tags.stream().map(Tag::getTagDisplayNamePlainText).collect(Collectors.toList()))
            .map(list -> String.join(DELIMITER, list))
            .orElse(null);
    }

    public String getTagIds() {
        return Optional.of(model)
            .filter(HasTags.class::isInstance)
            .map(HasTags.class::cast)
            .map(HasTags::getTags)
            .map(tags -> tags.stream()
                .map(Tag::getState)
                .map(State::getId)
                .map(UUID::toString)
                .collect(Collectors.toList()))
            .map(list -> String.join(DELIMITER, list))
            .orElse(null);
    }

    public String getPrimarySection() {
        return Optional.of(model)
            .filter(HasSection.class::isInstance)
            .map(HasSection.class::cast)
            .map(HasSection::getSectionParent)
            .map(Section::getSectionDisplayNamePlainText)
            .orElse(null);
    }

    public String getPrimarySectionId() {
        return Optional.of(model)
            .filter(HasSection.class::isInstance)
            .map(HasSection.class::cast)
            .map(HasSection::getSectionParent)
            .map(Recordable::getState)
            .map(State::getId)
            .map(UUID::toString)
            .orElse(null);
    }

    public String getPrimarySectionAncestorNames() {
        return Optional.of(model)
            .filter(HasSection.class::isInstance)
            .map(HasSection.class::cast)
            .map(HasSection::getSectionAncestors)
            .map(sections -> sections.stream()
                .map(Section::getSectionDisplayNamePlainText)
                .collect(Collectors.toList()))
            .map(list -> String.join(DELIMITER, list))
            .orElse(null);
    }

    public String getPrimarySectionAncestorIds() {
        return Optional.of(model)
            .filter(HasSection.class::isInstance)
            .map(HasSection.class::cast)
            .map(HasSection::getSectionAncestors)
            .map(tags -> tags.stream()
                .map(Section::getState)
                .map(State::getId)
                .map(UUID::toString)
                .collect(Collectors.toList()))
            .map(list -> String.join(DELIMITER, list))
            .orElse(null);
    }

    public String getAuthorNames() {
        return Optional.of(model)
            .filter(HasAuthors.class::isInstance)
            .map(HasAuthors.class::cast)
            .map(HasAuthors::getAuthors)
            .map(authors -> authors.stream().map(Author::getName).collect(Collectors.toList()))
            .map(list -> String.join(DELIMITER, list))
            .orElse(null);
    }

    public String getAuthorIds() {
        return Optional.of(model)
            .filter(HasAuthors.class::isInstance)
            .map(HasAuthors.class::cast)
            .map(HasAuthors::getAuthors)
            .map(authors -> authors.stream()
                .map(Author::getState)
                .map(State::getId)
                .map(UUID::toString)
                .collect(Collectors.toList()))
            .map(list -> String.join(DELIMITER, list))
            .orElse(null);
    }

    public String getSponsorName() {
        return Optional.of(model)
            .filter(HasSponsor.class::isInstance)
            .map(HasSponsor.class::cast)
            .map(HasSponsor::getSponsor)
            .map(ContentSponsor::getDisplayName)
            .orElse(null);
    }

    public String getSponsorId() {
        return Optional.of(model)
            .filter(HasSponsor.class::isInstance)
            .map(HasSponsor.class::cast)
            .map(HasSponsor::getSponsor)
            .map(ContentSponsor::getState)
            .map(State::getId)
            .map(UUID::toString)
            .orElse(null);
    }
}
