package brightspot.importtransformers;

import java.io.IOException;

import brightspot.author.PersonAuthor;
import brightspot.image.WebImage;
import brightspot.importapi.ImportTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonAuthorImportTransformer extends ImportTransformer<PersonAuthor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonAuthorImportTransformer.class);

    private String name;

    private String firstName;

    private String lastName;

    private String imageUrl;

    private String title;

    private String email;

    private String fullBiography;

    private String shortBiography;

    private String affiliation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullBiography() {
        return fullBiography;
    }

    public void setFullBiography(String fullBiography) {
        this.fullBiography = fullBiography;
    }

    public String getShortBiography() {
        return shortBiography;
    }

    public void setShortBiography(String shortBiography) {
        this.shortBiography = shortBiography;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public PersonAuthor transform() throws Exception {
        PersonAuthor personAuthor = new PersonAuthor();

        personAuthor.setName(name);
        personAuthor.setFirstName(firstName);
        personAuthor.setLastName(lastName);

        if (!org.apache.commons.lang3.StringUtils.isBlank(imageUrl)) {
            WebImage image = findByExternalId(WebImage.class, imageUrl, (url) -> {
                try {
                    WebImage w = new WebImage();
                    w.setFile(createStorageItemFromUrl(url));
                    return w;
                } catch (IOException ex) {
                    return null;
                }
            });

            if (image != null) {
                personAuthor.setImage(image);
            }
        }

        personAuthor.setTitle(title);
        personAuthor.setEmail(email);

        personAuthor.setFullBiography(fullBiography);
        personAuthor.setShortBiography(shortBiography);
        personAuthor.setAffiliation(affiliation);

        return personAuthor;
    }
}
