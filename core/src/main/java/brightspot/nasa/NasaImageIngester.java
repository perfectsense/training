package brightspot.nasa;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import brightspot.image.WebImage;
import com.psddev.cms.db.Content;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.RepeatingTask;
import com.psddev.dari.util.StorageItem;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NasaImageIngester extends RepeatingTask {

    public NasaImageIngester() {
        super("RSS", "Nasa Task");
    }

    @Override
    protected DateTime calculateRunTime(DateTime currentTime) {
        return everyMinute(currentTime);
    }

    @Override
    protected void doRepeatingTask(DateTime runTime) throws Exception {
        String feedurl = IngestGlobalSettings.getSettings()
            .getFeedUrl();
        Boolean debug = IngestGlobalSettings.getSettings().isEnable();
        if (IngestGlobalSettings.getSettings().isEnable()) {
            WebImage latestWebImage = Query.from(WebImage.class)
                .sortDescending(IngestedImageModification.class.getName() + "/ingest.ingestPublishedDate")
                .first();

            IngestedImageModification modification = latestWebImage.as(IngestedImageModification.class);
            Date date = Optional.ofNullable(modification)
                .map(IngestedImageModification::getIngestPublishedDate)
                .orElse(null);
            Set<RssClient.RssItem> items = RssClient.getItems(IngestGlobalSettings.getSettings()
                .getFeedUrl(), 10);
            for (RssClient.RssItem rssItem : items) {
                if (date == null || date.after(rssItem.getPubDate()) || date.equals(rssItem.getPubDate())) {
                    continue;
                }
                WebImage webImage = new WebImage();
                String imageUrl = "";
                if (!StringUtils.isBlank(rssItem.image)) {
                    imageUrl = rssItem.image;
                }
                webImage.setInternalName(rssItem.title);
                webImage.setDateUploaded(new Date());
                webImage.setCaptionOverride(rssItem.getDescription());
                webImage.setFile(StorageItem.Static.createUrl(imageUrl));
                webImage.setCreditOverride("NASA Image of the Day");
                webImage.as(IngestedImageModification.class).setIngestPublishedDate(rssItem.getPubDate());
                Content.Static.publish(webImage, IngestGlobalSettings.getSettings().getSite(),
                    IngestGlobalSettings.getSettings().getUser());
            }
        }
    }

    private static final Logger LOGGER =
        LoggerFactory.getLogger(NasaImageIngester.class);
}