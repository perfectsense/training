package brightspot.core.footer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import brightspot.core.logo.Logo;
import brightspot.core.navigation.Navigation;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.util.ObjectUtils;

public class PageFooter
    extends Content
    implements Footer {

    private static final DateTimeFormatter FMT_YEAR = DateTimeFormatter
        .ofPattern("YYYY");
    private static final String YEAR_TOKEN = "$YYYY";

    @IgnoredIfEmbedded
    @Required
    private String name;

    private Logo logo;
    private Navigation navigation;

    private List<FooterModuleType> content;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Note(
        "If published in this text, the special '$YYYY' date token will be replaced with the value "
            + "for the current year.")
    @DisplayName("Copyright/Disclaimer")
    private String disclaimer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public String getDisclaimer() {
        if (!ObjectUtils.isBlank(disclaimer)) {
            disclaimer = replaceDateToken(disclaimer);
        }

        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public List<FooterModuleType> getContent() {

        if (content == null) {
            content = new ArrayList<>();
        }

        return content;
    }

    public void setContent(List<FooterModuleType> content) {
        this.content = content;
    }

    private static String replaceDateToken(String text) {
        if (text != null && text.contains(YEAR_TOKEN)) {
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Instant instant = (new Date()).toInstant();
            LocalDateTime localDateTime = instant.atZone(defaultZoneId)
                .toLocalDateTime();
            text = text.replace(YEAR_TOKEN, FMT_YEAR.format(localDateTime));
        }
        return text;
    }
}
