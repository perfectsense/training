package bex.training.alphasort;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public interface AlphaSortOverride extends AlphaSortable {

    String getAlphaSortValueFallback();

    default AlphaSortOverrideData asAlphaSortOverrideData() {
        return as(AlphaSortOverrideData.class);
    }

    @Override
    default String getAlphaSortValue() {
        return Optional.ofNullable(asAlphaSortOverrideData().getAlphaSortValue())
                .filter(StringUtils::isNotBlank)
                .orElseGet(this::getAlphaSortValueFallback);
    }
}
