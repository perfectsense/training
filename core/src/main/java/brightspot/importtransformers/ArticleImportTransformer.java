package brightspot.importtransformers;

import java.util.HashSet;
import java.util.Set;

import brightspot.article.Article;
import brightspot.importapi.ImportTransformer;
import brightspot.section.SectionPage;
import brightspot.tag.TagPage;
import com.psddev.dari.util.Utils;

public class ArticleImportTransformer extends ImportTransformer<Article> {

    private String headline;

    private String subheadline;

    private String body;

    private String section;

    private Set<String> tags;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Set<String> getTags() {
        if (tags == null) {
            tags = new HashSet<>();
        }
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getSubheadline() {
        return subheadline;
    }

    public void setSubheadline(String subheadline) {
        this.subheadline = subheadline;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public Article transform() throws Exception {
        Article article = new Article();
        article.setHeadline(headline);
        article.setSubheadline(subheadline);
        article.setBody(body);

        if (tags != null) {
            for (String tagName : tags) {
                TagPage tag = findByExternalId(TagPage.class, tagName, (tn) -> {
                    TagPage t = new TagPage();
                    t.setDisplayName(Utils.toLabel(tn));
                    return t;
                });
                article.getTags().add(tag);
            }
        }

        if (section != null) {
            SectionPage sectionPage = findByExternalId(SectionPage.class, section, (sn) -> {
                SectionPage s = new SectionPage();
                s.setDisplayName(Utils.toLabel(sn));
                return s;
            });
            article.asHasSectionWithFieldData().setSection(sectionPage);
        }

        return article;
    }
}
