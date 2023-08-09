package brightspot.google.drives.conversion.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import brightspot.google.drive.sheets.AbstractImportedGoogleSheet;
import brightspot.google.drive.sheets.GoogleDriveFileSheetConverter;
import brightspot.google.drive.sheets.GoogleSheetsFieldMapping;
import brightspot.tag.TagPage;
import com.psddev.cms.db.Draft;
import com.psddev.cms.db.ToolUser;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.google.drive.GoogleDriveSyncable;
import org.apache.commons.lang3.StringUtils;

/**
 * Middleman record connecting a {@link GoogleDriveFileSheetConverter} to the {@link TagPage} that resulted from the
 * conversion process.
 */
public class ImportedGoogleSheetTag extends AbstractImportedGoogleSheet<TagPage> {

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
            GoogleDriveFileSheetConverter converter,
            Map<String, String> csvRecord,
            ToolUser user,
            Set<Record> recordsToPublish) {

        if (!TagPage.class.equals(ObjectUtils.getClassByName(converter.getConvertToType()))) {
            return Collections.emptyList();
        }

        List<Record> records = new ArrayList<>();
        setCsvRecord(csvRecord);
        State originalState = null;
        List<GoogleSheetsFieldMapping> fieldMappings = converter.getFieldMappings();

        GoogleSheetsFieldMapping tagMapping = GoogleSheetsFieldMapping.findMapping(
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

        GoogleSheetsFieldMapping parentMapping = GoogleSheetsFieldMapping.findMapping(
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

        Optional.ofNullable(GoogleSheetsFieldMapping.findMapping(fieldMappings, "description"))
            .map(GoogleSheetsFieldMapping::getColumn)
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
                draft.setName(GoogleDriveSyncable.generateReimportName());
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

    private TagPage findTag(GoogleSheetsFieldMapping fieldMapping, Set<Record> recordsToPublish) {
        String name = Optional.ofNullable(fieldMapping)
            .map(GoogleSheetsFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .orElse(null);

        if (name == null) {
            return null;
        }

        return Query.from(TagPage.class)
                .where("google.drive.importKey = ?", name)
                .findFirst()
                .orElseGet(() -> recordsToPublish.stream()
                        .filter(TagPage.class::isInstance)
                        .map(TagPage.class::cast)
                        .filter(s -> s.getDisplayName().equals(name))
                        .findFirst()
                        .orElse(null));
    }

    private TagPage createTag(GoogleSheetsFieldMapping fieldMapping) {
        return Optional.ofNullable(fieldMapping)
            .map(GoogleSheetsFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .map(name -> ObjectUtils.build(new TagPage(), tag -> {
                tag.setInternalName(name);
                tag.setDisplayName(name);
                GoogleDriveTagPageModification tagPageModification = tag.as(GoogleDriveTagPageModification.class);
                tagPageModification.setOriginalImportedGoogleSheetTag(this);
                tagPageModification.setImportKey(name);
            }))
                .orElse(null);
    }
}
