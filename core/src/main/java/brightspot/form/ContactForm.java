package brightspot.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.form.action.Action;
import brightspot.form.action.DataCollectionAction;
import brightspot.form.field.required.RequireableData;
import brightspot.form.field.text.Email;
import brightspot.form.field.text.LongText;
import brightspot.form.field.text.ShortText;
import brightspot.form.field.text.TextField;
import brightspot.form.field.text.TextFieldType;
import brightspot.form.item.FormItem;
import brightspot.rte.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.HtmlNote;

public class ContactForm extends PredefinedForm {

    @HtmlNote("Displayed to the user upon successful submission of the form.")
    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String successMessage;

    @HtmlNote("Displayed to the user when the form submission contains one or more validation errors.")
    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String errorMessage;

    @Override
    protected List<Action> createPredefinedActions() {
        return new ArrayList<>(Collections.singletonList(new DataCollectionAction()));
    }

    @Override
    protected List<FormItem> createPredefinedItems() {
        return Stream.of(
            createTextField("First Name", new ShortText(), true),
            createTextField("Last Name", new ShortText(), true),
            createTextField("Email", new Email(), true),
            createTextField("Subject", new ShortText(), true),
            createTextField("Body", new LongText(), true))
            .collect(Collectors.toList());
    }

    @Override
    public String getSuccessMessage() {
        return successMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    protected FormItem createTextField(String label, TextFieldType type, boolean isRequired) {
        TextField textField = new TextField();
        textField.setFieldLabel(label);
        textField.as(RequireableData.class).setRequired(isRequired);
        textField.setType(type);
        return textField;
    }
}
