package training;

import java.util.Locale;

import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;

@JsonView
@ViewInterface
public interface ExportEntryView {

    // --- Globals ---

    @ViewKey("l10n.locale")
    default String getLocale() {
        return Locale.US.toLanguageTag();
    }
}
