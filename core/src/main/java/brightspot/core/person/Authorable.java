package brightspot.core.person;

import java.util.ArrayList;
import java.util.Optional;

import brightspot.core.byline.Byline;
import brightspot.core.image.Image;
import brightspot.core.image.ImageOption;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.StorageItem;

public interface Authorable extends Recordable {

    default AuthorableData asAuthorableData() {
        return as(AuthorableData.class);
    }

    static Optional<Byline> getByline(Object object) {
        return Optional.ofNullable(object)
            .map(State::getInstance)
            .map(state -> state.as(AuthorableData.class))
            .map(AuthorableData::getByline);
    }

    static Optional<Author> getPrimaryAuthor(Object object) {
        return Optional.ofNullable(object)
            .map(State::getInstance)
            .map(state -> state.as(AuthorableData.class))
            .map(AuthorableData::getAuthors)
            .orElse(getByline(object)
                .map(Byline::getAuthors)
                .orElse(new ArrayList<>()))
            .stream()
            .findFirst();
    }

    static Optional<StorageItem> getAuthorImageFile(Object object) {
        return getPrimaryAuthor(object)
            .map(AbstractPerson::getImage)
            .map(ImageOption::getImageOptionFile);
    }

    static Optional<Image> getAuthorImage(Object object) {
        return getAuthorImageFile(object)
            .map((file) -> {
                Image image = new Image();
                image.setFile(file);
                image.setAltText(getAuthorName(object));
                return image;
            });
    }

    static String getAuthorUrl(Site site, Object object) {
        return getPrimaryAuthor(object)
            .map(a -> a.getPromotableUrl(site))
            .orElse(null);
    }

    static String getAuthorName(Object object) {
        return getPrimaryAuthor(object)
            .map(Record::getLabel)
            .orElse(null);
    }

    static String getAuthorBiography(Object object) {
        return getPrimaryAuthor(object)
            .map(AbstractPerson::getShortBiography)
            .orElse(null);
    }
}
