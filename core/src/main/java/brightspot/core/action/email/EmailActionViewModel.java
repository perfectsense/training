package brightspot.core.action.email;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.action.actionbar.ActionBar;
import brightspot.core.action.actionbar.ActionBarViewModel;
import brightspot.core.share.Shareable;
import brightspot.core.share.ShareableData;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpRequestAttribute;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.core.link.MailToLinkView;

/**
 * Produces a {@link MailToLinkView} for a given {@link EmailAction}. Used in the context of an {@link ActionBar}.
 */
public class EmailActionViewModel extends ViewModel<EmailAction> implements MailToLinkView {

    private static final String HREF_PREFIX = "mailto:";

    @HttpRequestAttribute(ActionBar.ACTION_BAR_CONTEXT_ATTRIBUTE)
    protected Recordable targetObject;

    @CurrentSite
    protected Site site;

    private String getURL() {
        return Optional.ofNullable(targetObject.as(Shareable.class))
            .map(shareable -> shareable.getShareableUrl(site))
            .map(url -> ActionBarViewModel.appendTrackingParameter(site, url, "email"))
            .map(CharSequence::toString)
            .orElse(null);
    }

    @Override
    public CharSequence getCc() {
        return model.getEmailCcs().stream().collect(Collectors.joining(";", "", ""));
    }

    @Override
    public CharSequence getBcc() {
        return model.getEmailBccs().stream().collect(Collectors.joining(";", "", ""));
    }

    @Override
    public CharSequence getEmailBody() {
        return StringUtils.encodeUri(
            Arrays.stream(new String[] {
                model.getEmailBody(),
                Optional.ofNullable(targetObject.as(Shareable.class))
                    .map(Shareable::asShareableData)
                    .map(ShareableData::getShareTitle).orElse(null),
                getURL(),
                Optional.ofNullable(targetObject.as(Shareable.class))
                    .map(Shareable::asShareableData)
                    .map(ShareableData::getShareDescription).orElse(null)
            })
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n\n", "", "")));
    }

    @Override
    public CharSequence getSubject() {
        return StringUtils.encodeUri(model.getEmailSubject());
    }

    @Override
    public CharSequence getLinkBody() {
        return "Email";
    }

    @Override
    public CharSequence getEmail() {
        return model.getEmailTo();
    }
}
