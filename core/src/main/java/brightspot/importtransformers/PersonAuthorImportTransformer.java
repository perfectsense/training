package brightspot.importtransformers;

import java.util.HashSet;
import java.util.Set;

import brightspot.author.PersonAuthor;
import brightspot.image.WebImage;
import brightspot.importapi.ImportTransformer;
import brightspot.importtransformers.element.ImportElementUtil;
import brightspot.social.facebook.FacebookSocialEntityData;
import brightspot.social.instagram.InstagramSocialEntityData;
import brightspot.social.linkedin.LinkedInSocialEntityData;
import brightspot.social.linkedin.LinkedInUserEntity;
import brightspot.social.pinterest.PinterestSocialEntityData;
import brightspot.social.tumblr.TumblrSocialEntityData;
import brightspot.social.twitter.TwitterSocialEntityData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.Seo;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonAuthorImportTransformer extends ImportTransformer<PersonAuthor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonAuthorImportTransformer.class);

    private static final String NAME_FIELD = "name";
    private static final String FIRSTNAME_FIELD = "firstName";
    private static final String LASTNAME_FIELD = "lastName";
    private static final String IMAGE_FIELD = "image";
    private static final String TITLE_FIELD = "title";
    private static final String EMAIL_FIELD = "email";
    private static final String FULL_BIO_FIELD = "fullBiography";
    private static final String SHORT_BIO_FIELD = "shortBiography";
    private static final String AFFILIATION_FIELD = "affiliation";
    private static final String FACEBOOK_USERNAME_FIELD = "facebookUsername";
    private static final String LINKEDIN_USERNAME_FIELD = "linkedInUsername";
    private static final String INSTAGRAM_USERNAME_FIELD = "instagramUsername";
    private static final String PINTEREST_USERNAME_FIELD = "pinterestUsername";
    private static final String TUMBLR_USERNAME_FIELD = "tumblrUsername";
    private static final String TWITTER_USERNAME_FIELD = "twitterUsername";

    private static final String PROMO_IMAGE_FIELD = "promoImage";
    private static final String PROMO_DESCRIPTION_FIELD = "promoDescription";
    private static final String SEO_TITLE_FIELD = "seoTitle";
    private static final String SEO_DESCRIPTION_FIELD = "seoDescription";
    private static final String SEO_KEYWORDS_FIELD = "seoKeywords";

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(FIRSTNAME_FIELD)
    private String firstName;

    @JsonProperty(LASTNAME_FIELD)
    private String lastName;

    @JsonProperty(IMAGE_FIELD)
    private WebImageImportTransformer imageReference;

    @JsonProperty(TITLE_FIELD)
    private String title;

    @JsonProperty(EMAIL_FIELD)
    private String email;

    @JsonProperty(FULL_BIO_FIELD)
    private String fullBiography;

    @JsonProperty(SHORT_BIO_FIELD)
    private String shortBiography;

    @JsonProperty(AFFILIATION_FIELD)
    private String affiliation;

    @JsonProperty(FACEBOOK_USERNAME_FIELD)
    private String facebookUsername;

    @JsonProperty(LINKEDIN_USERNAME_FIELD)
    private String linkedInUsername;

    @JsonProperty(INSTAGRAM_USERNAME_FIELD)
    private String instagramUsername;

    @JsonProperty(PINTEREST_USERNAME_FIELD)
    private String pinterestUsername;

    @JsonProperty(TUMBLR_USERNAME_FIELD)
    private String tumblrUsername;

    @JsonProperty(TWITTER_USERNAME_FIELD)
    private String twitterUsername;

    @JsonProperty(PROMO_IMAGE_FIELD)
    private WebImageImportTransformer promoImageReference;

    @JsonProperty(PROMO_DESCRIPTION_FIELD)
    private String promoDescription;

    @JsonProperty(SEO_TITLE_FIELD)
    private String seoTitle;

    @JsonProperty(SEO_DESCRIPTION_FIELD)
    private String seoDescription;

    @JsonProperty(SEO_KEYWORDS_FIELD)
    private Set<String> seoKeywords;

    @Override
    public String getExternalId() {
        return ObjectUtils.firstNonBlank(this.externalId, this.getIdAsString(), this.name);
    }

    @Override
    public PersonAuthor transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getName()),
            "name not provided for PersonAuthor with externalId [" + this.getExternalId() + "]");

        PersonAuthor personAuthor = (PersonAuthor) this.createNewObject();

        personAuthor.setName(ImportElementUtil.processInlineRichText(this.getName(), this));
        personAuthor.setFirstName(ImportElementUtil.processInlineRichText(this.getFirstName(), this));
        personAuthor.setLastName(ImportElementUtil.processInlineRichText(this.getLastName(), this));

        if (this.getImageReference() != null) {
            WebImage image = this.getImageReference().findOrCreate(this);
            if (image != null) {
                personAuthor.setImage(image);
            }
        }

        personAuthor.setTitle(ImportElementUtil.processInlineRichText(this.getTitle(), this));
        personAuthor.setEmail(this.getEmail());

        personAuthor.setFullBiography(ImportElementUtil.processRichText(this.getFullBiography(), this));
        personAuthor.setShortBiography(ImportElementUtil.processInlineRichText(this.getShortBiography(), this));

        personAuthor.setAffiliation(ImportElementUtil.processInlineRichText(this.getAffiliation(), this));

        personAuthor.as(FacebookSocialEntityData.class).setFacebookUsername(this.getFacebookUsername());
        personAuthor.as(InstagramSocialEntityData.class).setInstagramUsername(this.getInstagramUsername());
        personAuthor.as(PinterestSocialEntityData.class).setPinterestUsername(this.getPinterestUsername());
        personAuthor.as(TumblrSocialEntityData.class).setTumblrUsername(this.getTumblrUsername());
        personAuthor.as(TwitterSocialEntityData.class).setTwitterUsername(this.getTwitterUsername());

        if (StringUtils.isNotBlank(this.getLinkedInUsername())) {
            LinkedInUserEntity linkedInUserEntity = new LinkedInUserEntity();
            linkedInUserEntity.setLinkedInUsername(this.getLinkedInUsername());
            personAuthor.as(LinkedInSocialEntityData.class).setLinkedInEntity(linkedInUserEntity);
        }

        ImportTransformerUtil.setPromoImage(personAuthor, this.getPromoImageReference(), this);
        personAuthor.asPagePromotableWithOverridesData()
            .setPromoDescription(ImportElementUtil.processInlineRichText(this.getPromoDescription(), this));

        personAuthor.asSeoWithFieldsData().setTitle(this.getSeoTitle());
        personAuthor.asSeoWithFieldsData().setDescription(this.getSeoDescription());
        personAuthor.as(Seo.ObjectModification.class).setKeywords(this.getSeoKeywords());

        return personAuthor;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        if (StringUtils.isBlank(this.getName())) {
            return null;
        }
        return PredicateParser.Static.parse(PersonAuthor.class.getName() + "/name = \"" + this.getName() + "\"");
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public WebImageImportTransformer getImageReference() {
        return imageReference;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getFullBiography() {
        return fullBiography;
    }

    public String getShortBiography() {
        return shortBiography;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getFacebookUsername() {
        return facebookUsername;
    }

    public String getLinkedInUsername() {
        return linkedInUsername;
    }

    public String getInstagramUsername() {
        return instagramUsername;
    }

    public String getPinterestUsername() {
        return pinterestUsername;
    }

    public String getTumblrUsername() {
        return tumblrUsername;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public WebImageImportTransformer getPromoImageReference() {
        return promoImageReference;
    }

    public String getPromoDescription() {
        return promoDescription;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public Set<String> getSeoKeywords() {
        if (seoKeywords == null) {
            seoKeywords = new HashSet<>();
        }
        return seoKeywords;
    }
}
