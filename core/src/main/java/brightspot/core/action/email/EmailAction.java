package brightspot.core.action.email;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.action.actionbar.ActionBarItem;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * An {@link ActionBarItem} used for sharing content via email.
 */
@Recordable.DisplayName("E-Mail")
public class EmailAction extends Record implements ActionBarItem {

    private static final String PREFIX = "mailto:";

    private String emailTo;
    private List<String> emailCcs;
    private List<String> emailBccs;
    private String emailSubject;
    private String emailBody;

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public List<String> getEmailCcs() {
        if (emailCcs == null) {
            emailCcs = new ArrayList<>();
        }
        return emailCcs;
    }

    public void setEmailCcs(List<String> emailCcs) {
        this.emailCcs = emailCcs;
    }

    public List<String> getEmailBccs() {
        if (emailBccs == null) {
            emailBccs = new ArrayList<>();
        }
        return emailBccs;
    }

    public void setEmailBccs(List<String> emailBccs) {
        this.emailBccs = emailBccs;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    @Override
    public String getLabel() {
        return "E-Mail";
    }
}
