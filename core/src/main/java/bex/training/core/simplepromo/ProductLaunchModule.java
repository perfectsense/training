package bex.training.core.simplepromo;

import java.util.Date;

import brightspot.core.image.ImageOption;
import brightspot.core.module.ModuleType;

public class ProductLaunchModule extends ModuleType {

    private String title;

    private String description;

    private ImageOption media;

    private Date endDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageOption getMedia() {
        return media;
    }

    public void setMedia(ImageOption media) {
        this.media = media;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
