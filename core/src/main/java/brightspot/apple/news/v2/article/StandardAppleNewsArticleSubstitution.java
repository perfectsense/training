package brightspot.apple.news.v2.article;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import brightspot.apple.news.v2.channel.AppleNewsChannel;
import brightspot.apple.news.v2.channel.cms.AppleNewsChannelSiteSettings;
import brightspot.apple.news.v2.section.AppleNewsSection;
import brightspot.apple.news.v2.section.StandardAppleNewsSection;
import brightspot.apple.news.v2.section.mapping.AppleNewsSectionMappingPage;
import brightspot.article.Article;
import brightspot.article.ArticleQuickCreateTarget;
import brightspot.author.HasAuthors;
import brightspot.quickcreate.QuickCreateSource;
import brightspot.section.HasSecondarySections;
import brightspot.section.HasSection;
import brightspot.section.Section;
import brightspot.tag.HasTags;
import brightspot.tag.Tag;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.Substitution;

public class StandardAppleNewsArticleSubstitution extends StandardAppleNewsArticle implements
    ArticleQuickCreateTarget,
    Substitution {

    @Override
    public void onCopy(Object source) {

        if (source instanceof Article) {

            Article article = (Article) source;
            setTitle(article.getHeadline());
            setDescription(article.getSubheadline());

            Optional.ofNullable(article.getLead())
                .filter(AppleNewsArticleLead.class::isInstance)
                .map(AppleNewsArticleLead.class::cast)
                .ifPresent(this::setLead);
        }

        // Authors.
        Optional.ofNullable(source)
            .map(State::getInstance)
            .filter(state -> state.isInstantiableTo(HasAuthors.class))
            .map(state -> state.as(HasAuthors.class).getAuthors())
            .ifPresent(this::setAuthors);

        Set<String> keywords = new HashSet<>();

        // Tags.
        Optional.ofNullable(source)
            .map(State::getInstance)
            .filter(state -> state.isInstantiableTo(HasTags.class))
            .map(state -> state.as(HasTags.class).getTags())
            .ifPresent(tags -> tags.stream()
                .map(Tag::getTagDisplayNamePlainText)
                .forEach(keywords::add));

        // SEO.
        Optional.ofNullable(source)
            .filter(Recordable.class::isInstance)
            .map(Recordable.class::cast)
            .map(recordable -> recordable.as(Seo.ObjectModification.class).findKeywords())
            .filter(seoKeywords -> !seoKeywords.isEmpty())
            .map(ArrayList::new)
            .ifPresent(keywords::addAll);

        setKeywords(new ArrayList<>(keywords));

        // Section mapping.
        Set<Section> allSections = new HashSet<>();

        Optional.ofNullable(source)
            .filter(HasSection.class::isInstance)
            .map(HasSection.class::cast)
            .map(HasSection::getSectionParent)
            .ifPresent(allSections::add);

        Optional.ofNullable(source)
            .filter(HasSecondarySections.class::isInstance)
            .map(HasSecondarySections.class::cast)
            .map(HasSecondarySections::getSecondarySections)
            .ifPresent(allSections::addAll);

        Set<StandardAppleNewsSection> appleNewsSections = Optional.of(allSections)
            .map(sections -> sections.stream()
                .map(section -> AppleNewsSectionMappingPage
                        .getAppleNewsSectionBySection(
                                this.as(Site.ObjectModification.class).getOwner(),
                                section))
                .filter(Objects::nonNull)
                .filter(appleNewsSection -> appleNewsSection.isInstantiableTo(StandardAppleNewsSection.class))
                .map(appleNewsSection -> appleNewsSection.as(StandardAppleNewsSection.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()))
            .orElseGet(() -> Optional.ofNullable(AppleNewsChannelSiteSettings.get(
                as(Site.ObjectModification.class).getOwner(),
                AppleNewsChannelSiteSettings::getDefaultChannel))
                .map(AppleNewsChannel::getAllSections)
                .flatMap(sections -> StreamSupport.stream(sections.spliterator(), false)
                    .filter(AppleNewsSection::isDefaultSection)
                    .findFirst())
                .filter(section -> section.isInstantiableTo(StandardAppleNewsSection.class))
                .map(section -> section.as(StandardAppleNewsSection.class))
                .map(section -> new HashSet<>(Collections.singletonList(section)))
                .orElse(null));

        if (appleNewsSections != null) {

            this.setSections(new ArrayList<>(appleNewsSections));
        }
    }

    // --- QuickCreateTarget support ---

    @Override
    public boolean allowsMultipleCopies(
        ToolPageContext page,
        QuickCreateSource sourceContent) {

        // There should always only be one Apple News article per source content.
        return false;
    }
}
