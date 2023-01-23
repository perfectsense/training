package brightspot.footer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import brightspot.logo.Logo;
import brightspot.module.ModulePlacement;
import brightspot.module.promo.page.PagePromoModulePlacementInline;
import brightspot.navigation.Navigation;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public class PageFooter extends Content implements Footer {

    private static final DateTimeFormatter FMT_YEAR = DateTimeFormatter.ofPattern("yyyy");
    private static final String YEAR_TOKEN = "$YYYY";

    @IgnoredIfEmbedded
    @Required
    private String internalName;

    private Logo logo;
    private Navigation navigation;

    @DisplayName("Contents")
    @Embedded
    @Types({ PagePromoModulePlacementInline.class})
    private List<ModulePlacement> content;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Note(
        "If published in this text, the special '$YYYY' date token will be replaced with the value "
            + "for the current year.")
    @DisplayName("Copyright/Disclaimer")
    private String disclaimer;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    /**
     * @return rich text
     */
    public String getDisclaimer() {
        if (!RichTextUtils.isBlank(disclaimer)) {
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

    public List<ModulePlacement> getContent() {

        if (content == null) {
            content = new ArrayList<>();
        }

        return content;
    }

    public void setContent(List<ModulePlacement> content) {
        this.content = content;
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getInternalName();
    }

    // --- Utility ---

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
