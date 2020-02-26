package brightspot.core.video;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import brightspot.core.page.PageElementSupplier;
import brightspot.core.site.FrontEndSettings;
import brightspot.core.site.VideoFrontendSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.PlainText;
import com.psddev.styleguide.core.promo.PromoView;

public abstract class AbstractPlayerViewModel<M extends VideoProvider> extends ViewModel<M> {

    @CurrentSite
    protected Site site;

    private String playerId = null;

    private VideoFrontendSettings videoFrontendSettings;

    protected Optional<Dimensions> dimensions;

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);
        dimensions = Optional.ofNullable(model.getVideoMetaData())
            .map(VideoMetaData::getVideoProvider)
            .filter(provider -> provider.getOriginalVideoWidth() != null || provider.getOriginalVideoHeight() != null)
            .map(provider -> new Dimensions(provider.getOriginalVideoWidth(), provider.getOriginalVideoHeight()));
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public VideoFrontendSettings getVideoFrontendSettings() {
        if (videoFrontendSettings == null) {
            videoFrontendSettings = ObjectUtils.firstNonNull(FrontEndSettings.get(
                site,
                FrontEndSettings::getVideoFrontendSettings), new VideoFrontendSettings());
        }

        return videoFrontendSettings;
    }

    public boolean checkOption(Option option) {
        VideoMetaData videoMetaData = model.getVideoMetaData();

        if ((videoMetaData != null) && videoMetaData.getOptions().contains(option)) {
            return true;
        }
        return getVideoFrontendSettings().getOptions().contains(option);
    }

    public Iterable<? extends PromoView> getCard() {
        VideoMetaData video = model.getVideoMetaData();

        if ((video != null) && checkOption(Option.CARD)) {
            return Arrays.asList(new PromoView.Builder()
                .title(video.getHeadline())
                .description(video.getSubHeadline())
                .url(PlainText.of("#"))
                .build());
        }
        return null;
    }

    public CharSequence getAspectRatio() {
        return dimensions
            .map(Dimensions::getAspectRatioLabel)
            .orElse(null);
    }

    public Double getAspectRatioValue() {
        return dimensions
            .map(Dimensions::getAspectRatio)
            .map(BigDecimal::new)
            .map(e -> e.setScale(2, RoundingMode.HALF_UP))
            .map(BigDecimal::doubleValue)
            .orElse(null);
    }

    public Number getSeekSeconds() {
        // TODO
        return null;
    }

    public CharSequence getPlayerId() {
        if (playerId == null) {
            playerId = "f" + UUID.randomUUID().toString().replace("-", "");
        }

        return playerId;
    }

    public CharSequence getVideoId() {
        return model.getVideoId();
    }

    public CharSequence getVideoTitle() {
        return model.getVideoTitleFallback();
    }

    public <T> Iterable<T> getExtraAttributes(Class<T> viewClass) {
        Collection<T> extraAttributes = new ArrayList<>();

        for (T extraAttribute : createViews(
            viewClass,
            PageElementSupplier.get(VideoProviderExtraAttributes.class, site, model))) {
            extraAttributes.add(extraAttribute);
        }

        return extraAttributes;
    }

    public Boolean getMuted() {
        return checkOption(Option.MUTED);
    }

    public Boolean getAutoplay() {
        return checkOption(Option.AUTOPLAY);
    }
}
