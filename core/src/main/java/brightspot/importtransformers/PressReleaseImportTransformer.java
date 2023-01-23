package brightspot.importtransformers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.image.ImageLead;
import brightspot.image.ImageLeadSubstitution;
import brightspot.image.WebImage;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.ImportingDatabase;
import brightspot.importtransformers.richtext.BodyElement;
import brightspot.importtransformers.richtext.BodyTransformUtil;
import brightspot.pressrelease.PressRelease;
import brightspot.section.SectionPage;
import brightspot.tag.TagPage;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.Utils;
import org.apache.commons.lang3.StringUtils;

public class PressReleaseImportTransformer extends ImportTransformer<PressRelease> {

    private String headline;

    private String subheadline;

    private String leadUrl;

    private List<BodyElement> body;

    private String section;

    private Set<String> tags;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSubheadline() {
        return subheadline;
    }

    public void setSubheadline(String subheadline) {
        this.subheadline = subheadline;
    }

    public String getLeadUrl() {
        return leadUrl;
    }

    public void setLeadUrl(String leadUrl) {
        this.leadUrl = leadUrl;
    }

    public List<BodyElement> getBody() {
        if (body == null) {
            body = new ArrayList<>();
        }
        return body;
    }

    public void setBody(List<BodyElement> body) {
        this.body = body;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

    @Override
    public PressRelease transform() throws Exception {

        PressRelease pressRelease = new PressRelease();

        pressRelease.setHeadline(headline);
        pressRelease.setSubHeadline(subheadline);

        Optional.ofNullable(this.getBody())
                .map(b -> BodyTransformUtil.transform(body, this))
                .ifPresent(pressRelease::setBody);

        if (!StringUtils.isBlank(leadUrl)) {
            WebImage image = findByExternalId(WebImage.class, leadUrl, (url) -> {
                try {
                    WebImage w = new WebImage();
                    w.setFile(createStorageItemFromUrl(leadUrl));
                    return w;
                } catch (IOException ex) {
                    return null;
                }
            });
            if (image != null) {
                ImageLead imageLead = new ImageLead();
                imageLead.setImage(image);
                pressRelease.setLead(imageLead.as(ImageLeadSubstitution.class));
            }
        }

        Site site = ((ImportingDatabase) Database.Static.getDefault()).getSite();

        if (section != null) {
            SectionPage sectionPage = findByExternalId(SectionPage.class, section, (sn) -> {
                String displayName = Utils.toLabel(sn);
                SectionPage s = Query.from(SectionPage.class).where("displayName = ?", displayName)
                        .and("cms.site.owner = ?", site).first();
                if (s == null) {
                    s = new SectionPage();
                    s.setDisplayName(displayName);
                }
                return s;
            });
            pressRelease.asHasSectionWithFieldData().setSection(sectionPage);
        }

        if (tags != null) {
            for (String tagName : tags) {
                TagPage tag = findByExternalId(TagPage.class, tagName, (tn) -> {
                    String displayName = Utils.toLabel(tn);
                    TagPage t = Query.from(TagPage.class).where("tag.getTagDisplayNamePlainText = ?", displayName)
                            .and("cms.site.owner = ?", site).resolveToReferenceOnly().first();
                    if (t == null) {
                        t = new TagPage();
                        t.setDisplayName(displayName);
                    }
                    return t;
                });
                pressRelease.getTags().add(tag);
            }
        }

        return pressRelease;
    }
}
