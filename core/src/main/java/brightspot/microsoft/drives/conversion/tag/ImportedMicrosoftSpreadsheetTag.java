package brightspot.microsoft.drives.conversion.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import brightspot.microsoft.drives.conversion.MicrosoftDrivesSyncable;
import brightspot.microsoft.drives.conversion.spreadsheet.AbstractImportedMicrosoftSpreadsheet;
import brightspot.microsoft.drives.conversion.spreadsheet.MicrosoftDrivesSpreadsheetConverter;
import brightspot.microsoft.drives.conversion.spreadsheet.MicrosoftDrivesSpreadsheetFieldMapping;
import brightspot.tag.TagPage;
import com.psddev.cms.db.Draft;
import com.psddev.cms.db.ToolUser;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Middleman record connecting a {@link MicrosoftDrivesSpreadsheetConverter} to the {@link TagPage} that resulted from
 * the conversion process.
 */
public class ImportedMicrosoftSpreadsheetTag extends AbstractImportedMicrosoftSpreadsheet<TagPage> {

    public static final List<String> INCOMING_FIELDS = Arrays.asList("displayName", "parent", "description");

    private transient Map<String, String> csvRecord;

    public Map<String, String> getCsvRecord() {
        return csvRecord;
    }

    public void setCsvRecord(Map<String, String> csvRecord) {
        this.csvRecord = csvRecord;
    }

    @Override
    public List<Record> buildRecords(
        MicrosoftDrivesSpreadsheetConverter converter,
        Map<String, String> csvRecord,
        ToolUser user,
        Set<Record> recordsToPublish) {

        if (!TagPage.class.equals(ObjectUtils.getClassByName(converter.getConvertToType()))) {
            return Collections.emptyList();
        }

        List<Record> records = new ArrayList<>();
        setCsvRecord(csvRecord);
        State originalState = null;
        List<MicrosoftDrivesSpreadsheetFieldMapping> fieldMappings = converter.getFieldMappings();

        MicrosoftDrivesSpreadsheetFieldMapping tagMapping = MicrosoftDrivesSpreadsheetFieldMapping.findMapping(
            fieldMappings,
            "displayName");
        TagPage tag = findTag(tagMapping, recordsToPublish);
        if (tag == null) {
            tag = createTag(tagMapping);
        } else {
            if (!tag.getState().isNew()) {
                originalState = tag.getState();
                tag = (TagPage) tag.clone();
            }
        }

        if (tag == null) {
            return records;
        }

        MicrosoftDrivesSpreadsheetFieldMapping parentMapping = MicrosoftDrivesSpreadsheetFieldMapping.findMapping(
            fieldMappings,
            "parent");
        TagPage parentTag = findTag(parentMapping, recordsToPublish);
        if (parentTag == null) {
            parentTag = createTag(parentMapping);
            if (parentTag != null) {
                records.add(parentTag);
            }
        }

        if (parentTag != null) {
            tag.setParent(parentTag);
        }

        Optional.ofNullable(MicrosoftDrivesSpreadsheetFieldMapping.findMapping(fieldMappings, "description"))
            .map(MicrosoftDrivesSpreadsheetFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .ifPresent(tag::setDescription);

        if (originalState != null) {
            Map<String, Map<String, Object>> diffMap = Draft.findDifferences(
                Database.Static.getDefault().getEnvironment(),
                originalState.getSimpleValues(),
                tag.getState().getSimpleValues());
            if (!diffMap.isEmpty()) {
                Draft draft = new Draft();
                draft.setObjectType(originalState.getType());
                draft.setObjectId(originalState.getId());
                draft.setDifferences(diffMap);
                draft.setName(MicrosoftDrivesSyncable.generateMicrosoftDrivesReimportName());
                draft.setOwner(user);
                records.add(draft);
            }
        } else {
            records.add(tag);
        }

        return records;
    }

    @Override
    public List<String> getFieldsToConvert() {
        return INCOMING_FIELDS;
    }

    private TagPage findTag(MicrosoftDrivesSpreadsheetFieldMapping fieldMapping, Set<Record> recordsToPublish) {
        String name = Optional.ofNullable(fieldMapping)
            .map(MicrosoftDrivesSpreadsheetFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .orElse(null);

        if (name == null) {
            return null;
        }

        return Query.from(TagPage.class)
            .where("microsoft.drives.importKey = ?", name)
            .findFirst()
            .orElseGet(() -> recordsToPublish.stream()
                .filter(TagPage.class::isInstance)
                .map(TagPage.class::cast)
                .filter(s -> s.getDisplayName().equals(name))
                .findFirst()
                .orElse(null));
    }

    private TagPage createTag(MicrosoftDrivesSpreadsheetFieldMapping fieldMapping) {
        return Optional.ofNullable(fieldMapping)
            .map(MicrosoftDrivesSpreadsheetFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .map(name -> ObjectUtils.build(new TagPage(), tag -> {
                tag.setInternalName(name);
                tag.setDisplayName(name);
                MicrosoftDrivesTagPageModification tagPageModification = tag.as(MicrosoftDrivesTagPageModification.class);
                tagPageModification.setOriginalImportedMicrosoftSpreadsheetTag(this);
                tagPageModification.setImportKey(name);
            }))
            .orElse(null);
    }
}
