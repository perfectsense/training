package brightspot.article;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.author.AuthoringPageViewModel;
import brightspot.image.ImageSchemaData;
import brightspot.l10n.CurrentLocale;
import brightspot.page.AbstractContentPageViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import brightspot.permalink.Permalink;
import brightspot.rte.heading.HeadingTwo;
import brightspot.seo.PersonSchemaViewModel;
import brightspot.update.LastUpdatedProvider;
import brightspot.util.DateTimeUtils;
import brightspot.util.RichTextUtils;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.page.MainContent;
import com.psddev.cms.rte.LineBreakRichTextPreprocessor;
import com.psddev.cms.rte.RichTextPreprocessor;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.article.ArticlePageView;
import com.psddev.styleguide.listicle.ListiclePageView;
import com.psddev.styleguide.listicle.ListiclePageViewIntroField;
import com.psddev.styleguide.listicle.ListiclePageViewItemsField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorBiographyField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewContributorsField;
import com.psddev.styleguide.page.CreativeWorkPageViewHeadlineField;
import com.psddev.styleguide.page.CreativeWorkPageViewPeopleField;
import com.psddev.styleguide.page.CreativeWorkPageViewSubHeadlineField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class ListiclePageViewModel extends AbstractContentPageViewModel<Listicle> implements ListiclePageView,
        PageEntryView {

    private static final Map<String, ObjectType> CONCRETE_TAG_TYPES = RichTextElement.getConcreteTagTypes();

    private static final String DATE_FORMAT_KEY = "dateFormat";

    @CurrentLocale
    private Locale locale;

    @MainContent
    private Object mainObject;

    @CurrentSite
    private Site site;

    @CurrentPageViewModel(AuthoringPageViewModel.class)
    AuthoringPageViewModel authoringPage;

    private String intro = null;

    private Iterable<? extends ListiclePageViewItemsField> listicleItems = new ArrayList<>();

    @Override
    protected void onCreate(ViewResponse response) {

        listicleItems = Optional.ofNullable(model.getBody())
                .map(body -> preprocessRichText(body, new LineBreakRichTextPreprocessor()))
                .map(RichTextUtils::documentFromRichText)
                .map(doc -> {

                    Database db = model.getState().getDatabase();
                    ObjectType headingType = ObjectType.getInstance(HeadingTwo.class);

                    ArrayList<ListiclePageViewItemsField> items = new ArrayList<>();
                    String[] itemHeading = new String[1];
                    boolean creatingIntro = false; // not currently creating intro
                    boolean shouldCreateIntro = true; // attempt to create intro
                    Element itemBody = new Document("");

                    for (Node ogNode : doc.body().childNodes()) {
                        Node node = ogNode.clone();

                        if (node instanceof Element) {
                            Element element = (Element) node;
                            String tag = element.tagName();
                            ObjectType type = CONCRETE_TAG_TYPES.get(tag);

                            if (headingType.equals(type)) {

                                // Close the current item, if the intro is being built save as intro
                                if (creatingIntro) {
                                    creatingIntro = false; // no longer creating intro
                                    shouldCreateIntro = false; // do not attempt to create intro
                                    intro = itemBody.html();
                                    itemBody.empty();
                                } else if (!StringUtils.isBlank(itemHeading[0]) && itemBody.childNodeSize() > 0) {

                                    boolean firstEntry = intro == null;
                                    items.add(
                                        createView(
                                            ListiclePageViewItemsField.class,
                                            new ListicleItem(itemHeading[0], firstEntry, itemBody.html(), model)
                                        )
                                    );
                                    itemBody.empty();
                                } else if (shouldCreateIntro) {
                                    // Edge case, heading is the first element in the body. lock
                                    shouldCreateIntro = false; // do not attempt to create intro
                                }

                                HeadingTwo heading = (HeadingTwo) RichTextElement.fromElement(db, element);
                                itemHeading[0] = heading.getText();
                                continue;
                            }
                        }

                        // Not a heading, add node to item stack
                        itemBody.appendChild(node);

                        // Toggle flag to indicate itemBody is being constructed for intro
                        if (shouldCreateIntro) {
                            creatingIntro = true;
                        }

                    }

                    // edge case, the body does not contain a heading.. ie all intro
                    if (creatingIntro) {

                        intro = itemBody.html();

                    } else if (itemBody.childNodeSize() > 0) {

                        items.add(
                            createView(
                                ListiclePageViewItemsField.class,
                                new ListicleItem(itemHeading[0], false, itemBody.html(), model)
                            )
                        );
                    }

                    // Finally add the intro as the first stack item if nav is hidden or numbering should start at top
                    if (model.isStartNumberingAtTop() && intro != null) {
                        items.add(0,
                            createView(
                                ListiclePageViewItemsField.class,
                                new ListicleItem(null, true, intro, model)
                            )
                        );
                        intro = null;
                    }

                    return items;
                })
                .orElse(null);

    }

    @Override
    public Iterable<? extends ListiclePageViewIntroField> getIntro() {
        return Optional.ofNullable(intro)
            .map(introText -> RichTextUtils.buildHtml(
                model,
                model -> introText,
                e -> createView(ListiclePageViewIntroField.class, e))
            )
            .orElse(null);
    }

    @Override
    public Iterable<? extends ListiclePageViewItemsField> getItems() {
        return listicleItems;
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        // TODO this shouldn't even be present on ListiclePageView
        return null;
    }

    @JsonLdNode("author")
    public Iterable<? extends PersonSchemaViewModel> getPersonData() {
        return model.getAuthors().stream()
                .map(a -> createView(PersonSchemaViewModel.class, a))
                .collect(Collectors.toList());
    }

    @JsonLdNode("image")
    public Iterable<ImageSchemaData> getImageData() {
        return page.getImageData();
    }

    @JsonLdNode("mainEntityOfPage")
    public Map<String, Object> getMainEntityOfPageData() {
        return ImmutableMap.of(
                "@type", "WebPage",
                "@id", Permalink.getPermalink(page.getSite(), model)
        );
    }

    @JsonLdNode
    @Override
    public CharSequence getDateModified() {
        // Plain text
        return DateTimeUtils.format(
                LastUpdatedProvider.getMostRecentUpdateDate(model), ArticlePageView.class,
                DATE_FORMAT_KEY, page.getSite(), locale, PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @JsonLdNode("dateModified")
    @Override
    public CharSequence getDateModifiedISO() {
        return Optional.ofNullable(ObjectUtils.firstNonNull(LastUpdatedProvider.getMostRecentUpdateDate(model), model.getPublishDate()))
                .map(Date::toInstant)
                .map(Instant::toString)
                .orElse(null);
    }

    @JsonLdNode
    @Override
    public CharSequence getDatePublished() {
        // Plain text
        return DateTimeUtils.format(model.getPublishDate(), ArticlePageView.class, DATE_FORMAT_KEY, page.getSite(),
                locale, PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @JsonLdNode("datePublished")
    @Override
    public CharSequence getDatePublishedISO() {
        return Optional.ofNullable(model.getPublishDate())
                .map(Date::toInstant)
                .map(Instant::toString)
                .orElse(null);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewHeadlineField> getHeadline() {
        return RichTextUtils.buildInlineHtml(
                model,
                Listicle::getHeadline,
                e -> createView(CreativeWorkPageViewHeadlineField.class, e));
    }

    @Override
    public CharSequence getSource() {

        // TODO need HasSource model 2021-04-06
        return null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSubHeadlineField> getSubHeadline() {
        return RichTextUtils.buildInlineHtml(
                model,
                Listicle::getSubheadline,
                e -> createView(CreativeWorkPageViewSubHeadlineField.class, e));
    }

    @JsonLdNode("headline")
    public CharSequence getTruncatedHeadline() {
        String seoTitle = model.getSeoTitle();
        if (seoTitle != null && seoTitle.length() >= 110) {
            seoTitle = seoTitle.substring(0, 109);
        }
        return seoTitle;
    }

    @JsonLdNode("description")
    public CharSequence getDescriptionJsonLd() {
        return RichTextUtils.richTextToPlainText(model.getSubheadline());
    }

    // Authoring Entity

    @Override
    public Iterable<? extends CreativeWorkPageViewAuthorBiographyField> getAuthorBiography() {
        return authoringPage.getAuthorBiography(CreativeWorkPageViewAuthorBiographyField.class);
    }

    @Override
    public Map<String, ?> getAuthorImage() {
        return authoringPage.getAuthorImageAttributes();
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewAuthorNameField> getAuthorName() {
        return authoringPage.getAuthorName(CreativeWorkPageViewAuthorNameField.class);
    }

    @Override
    public CharSequence getAuthorUrl() {
        return authoringPage.getAuthorUrl();
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewContributorsField> getContributors() {
        return authoringPage.getContributors(CreativeWorkPageViewContributorsField.class);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewPeopleField> getPeople() {
        return authoringPage.getAuthors(CreativeWorkPageViewPeopleField.class);
    }

    /**
     * Preprocesses rich text.
     *
     * @param richText the rich text to process.
     * @param preprocessors the list of preprocessors to use.
     * @return the preprocessed rich text.
     */
    public static String preprocessRichText(String richText, RichTextPreprocessor... preprocessors) {

        if (preprocessors == null) {
            return richText;
        }

        Document document = RichTextUtils.documentFromRichText(richText);

        Element body = document.body();

        for (RichTextPreprocessor preprocessor : preprocessors) {
            preprocessor.preprocess(body);
        }

        richText = document.body().html();

        return richText;
    }

}
