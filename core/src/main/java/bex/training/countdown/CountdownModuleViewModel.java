package bex.training.countdown;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.stream.IntStream;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.countdown.CountdownModuleView;
import com.psddev.styleguide.countdown.CountdownModuleViewImageField;
import com.psddev.styleguide.countdown.CountdownModuleViewVillainsField;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.distribution.GammaDistribution;

public class CountdownModuleViewModel extends ViewModel<CountdownModule> implements CountdownModuleView {

    private static final int DAYS_PER_MONTH = 30;

    @Override
    protected boolean shouldCreate() {
        return model.getCountdown() != null;
    }

    @Override
    public CharSequence getDescription() {
        return RichTextUtils.buildInlineHtml(model.getState().getDatabase(), model.getDescription(), this::createView);
    }

    @Override
    public Number getEndDate() {
        Double gammaDistributionShape = model.getCountdown().getGammaDistributionShape();
        if (gammaDistributionShape == null || gammaDistributionShape <= 0) {
            return null;
        }

        GammaDistribution distribution = new GammaDistribution(gammaDistributionShape, 1.0);
        Double estimate = IntStream.rangeClosed(0, 99)
                .mapToObj(i -> Pair.of(i, distribution.density(i)))
                .sorted(Comparator.comparing(Pair::getValue, Comparator.reverseOrder()))
                .limit(2)
                .map(Pair::getLeft)
                .map(Double::valueOf)
                .reduce((x1, x2) -> (x1 + x2) / 2)
                .orElse(null);

        if (estimate == null) {
            return null;
        }

        return Instant.now().plus((long) (estimate * DAYS_PER_MONTH), ChronoUnit.DAYS).toEpochMilli();
    }

    @Override
    public Iterable<? extends CountdownModuleViewImageField> getImage() {
        return createViews(CountdownModuleViewImageField.class, model.getImage());
    }

    @Override
    public CharSequence getTitle() {
        return model.getTitle();
    }

    @Override
    public Iterable<? extends CountdownModuleViewVillainsField> getVillains() {
        return createViews(CountdownModuleViewVillainsField.class, model.getCountdown().getVillains());
    }
}
