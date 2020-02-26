package brightspot.core.page.opengraph.article;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.core.person.AuthorableData;
import brightspot.core.section.Section;
import brightspot.core.section.SectionableData;
import brightspot.core.tag.Tag;
import brightspot.core.tag.TaggableData;
import brightspot.core.tool.DirectoryItemUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.facebook.OpenGraphArticleMetaView;

public class OpenGraphArticleMetaViewModel extends ViewModel<OpenGraphArticle> implements OpenGraphArticleMetaView {

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getModifiedTime() {
        return Optional.ofNullable(model.as(Content.class))
            .map(Content::getUpdateDate)
            .map(Date::toInstant)
            .map(inst -> inst.atOffset(ZoneOffset.UTC))
            .map(OffsetDateTime::toLocalDateTime)
            .map(ldt -> ldt.format(DateTimeFormatter.ISO_DATE_TIME))
            .orElse(null);
    }

    @Override
    public CharSequence getPublishedTime() {
        return Optional.ofNullable(model.as(Content.class))
            .map(Content::getPublishDate)
            .map(Date::toInstant)
            .map(inst -> inst.atOffset(ZoneOffset.UTC))
            .map(OffsetDateTime::toLocalDateTime)
            .map(ldt -> ldt.format(DateTimeFormatter.ISO_DATE_TIME))
            .orElse(null);
    }

    @Override
    public Iterable<? extends CharSequence> getAuthorUrls() {
        return Optional.ofNullable(model.as(AuthorableData.class))
            .map(AuthorableData::getAuthors)
            .flatMap(authors -> Optional.ofNullable(authors.stream()))
            .orElseGet(Stream::empty)
            .map(author -> DirectoryItemUtils.getCanonicalUrl(site, author))
            .collect(Collectors.toList());
    }

    @Override
    public CharSequence getExpirationTime() {
        return null;
    }

    @Override
    public CharSequence getSection() {
        return Optional.ofNullable(model.as(SectionableData.class))
            .map(SectionableData::getSection)
            .map(Section::getDisplayName)
            .orElse(null);
    }

    @Override
    public Iterable<? extends CharSequence> getTags() {
        return Optional.ofNullable(model.as(TaggableData.class))
            .map(TaggableData::getTags)
            .map(tags -> tags.stream()
                .map(Tag::getDisplayName)
                .collect(Collectors.toList()))
            .orElse(null);
    }
}
