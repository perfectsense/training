package brightspot.nasa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.psddev.dari.db.Record;
import com.psddev.dari.util.StringUtils;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class RssClient extends Record {

    public static Set<RssItem> getItems(String url, int maxItems) throws FeedException, IOException {
        Set<RssItem> itemsSet = new LinkedHashSet<>();

        if (!StringUtils.isBlank(url)) {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(120000);
            urlConnection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
            urlConnection.setRequestProperty("x-bsp", "rss");

            // get a stream to read data from
            try (BufferedReader urlReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                SyndFeed feed = new SyndFeedInput().build(urlReader);

                for (Object entryObject : feed.getEntries()) {
                    SyndEntry entry = (SyndEntry) entryObject;

                    RssItem item = new RssItem();
                    item.setGuid(cleanString(entry.getUri()));
                    item.setTitle(cleanString(entry.getTitle()));
                    item.setLink(cleanString(entry.getLink()));
                    item.setSource(entry.getSource() != null ? cleanString(entry.getSource().getUri()) : null);
                    item.setPubDate(entry.getPublishedDate());

                    if (entry.getDescription() != null) {
                        item.setDescription(cleanString(entry.getDescription().getValue()));
                    }

                    item.setImage(entry.getEnclosures().stream()
                        .map(syndEnclosure -> syndEnclosure.getUrl())
                        .findFirst()
                        .orElse(null));

                    itemsSet.add(item);

                    if (itemsSet.size() >= maxItems) {
                        break;
                    }
                }
            }
        }
        return itemsSet;
    }

    /**
     * Cleans a string by removing any misplaced "\t", "\n", additional spacing characters
     *
     * @return
     */
    public static String cleanString(String str) {
        if (str == null) {
            return str;
        }

        str = str.replace("\t", " ");
        str = str.replace("\n", "");
        str = str.trim();
        return str;
    }

    public static class RssItem {

        String title;
        String link;
        String source;
        String guid;
        Date pubDate;
        String description;
        String image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public Date getPubDate() {
            return pubDate;
        }

        public void setPubDate(Date pubDate) {
            this.pubDate = pubDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            if (!StringUtils.isBlank(image)) {
                return image;
            }
            if (!StringUtils.isBlank(description)) {
                Document document = Jsoup.parse(description);
                Element element = document.select("img").first();
                if (element != null) {
                    return element.absUrl("src");
                }
            }
            return null;
        }

    }
}
