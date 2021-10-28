package training.rte;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

final class ConverterMapping {

    static final Map<String, RichTextConverter> CONVERTERS;

    static {
        ImmutableMap.Builder<String, RichTextConverter> builder = ImmutableMap.builder();
        builder
            .put("a", new LinkRTEConverter())
            .put("brightspot-cms-external-content", new ECRTEConverter())
            .put("bsp-carousel", new CarouselRTEConverter())
            .put("bsp-image", new ImageRTEConverter())
            .put("bsp-quote", new QuoteRTEConverter())
            .put("bsp-video", new VideoRTEConverter())
            .put("h2", new HeadingTwoConverter())
            .put("h3", new HeadingThreeConverter())
        ;
        CONVERTERS = builder.build();
    }
}
