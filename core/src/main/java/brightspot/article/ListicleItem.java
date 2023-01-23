package brightspot.article;

import brightspot.image.WebImageAsset;
import com.psddev.dari.db.Recordable;

public interface ListicleItem extends Recordable {

    WebImageAsset getFirstImage();

    long getTextCharacterCount();

    String getFullContentEncoded();

    String getSuggestableText();
}
