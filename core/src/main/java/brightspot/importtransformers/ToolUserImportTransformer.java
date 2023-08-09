package brightspot.importtransformers;

import brightspot.importapi.ImportTransformer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.ToolUser;
import com.psddev.dari.db.CompoundPredicate;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class ToolUserImportTransformer extends ImportTransformer<ToolUser> {

    private static final String NAME_FIELD = "name";
    private static final String USERNAME_FIELD = "username";
    private static final String EMAIL_FIELD = "email";

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(USERNAME_FIELD)
    private String username;

    @JsonProperty(EMAIL_FIELD)
    private String email;

    @Override
    public ToolUser transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNoneBlank(this.getName(), this.getEmail()),
            "name or email not provided for ToolUser with externalId [" + this.getExternalId() + "]");

        ToolUser toolUser = new ToolUser();

        toolUser.setName(this.getName());
        toolUser.setEmail(this.getEmail());

        if (StringUtils.isNotBlank(this.getUsername())) {
            toolUser.setUsername(this.getUsername());
        }

        return toolUser;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        if (StringUtils.isBlank(this.getName())) {
            return null;
        }
        Predicate emailPredicate = PredicateParser.Static.parse(
            ToolUser.class.getName() + "/email = " + this.getEmail());
        if (StringUtils.isBlank(this.getUsername())) {
            return emailPredicate;
        }
        return CompoundPredicate.combine(
            PredicateParser.OR_OPERATOR,
            emailPredicate,
            PredicateParser.Static.parse(ToolUser.class.getName() + "/username = " + this.getUsername()));
    }

    @Override
    public String getExternalId() {
        return ObjectUtils.firstNonBlank(this.externalId, this.getIdAsString(), this.email);
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
