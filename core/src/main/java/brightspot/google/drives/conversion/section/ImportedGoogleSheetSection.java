package brightspot.google.drives.conversion.section;

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
import brightspot.section.SectionPage;
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
 * Middleman record connecting a {@link GoogleDriveFileSheetConverter} to the {@link SectionPage} that resulted from the
 * conversion process.
 */
public class ImportedGoogleSheetSection extends AbstractImportedGoogleSheet<SectionPage> {

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

        if (!SectionPage.class.equals(ObjectUtils.getClassByName(converter.getConvertToType()))) {
            return Collections.emptyList();
        }

        List<Record> records = new ArrayList<>();
        setCsvRecord(csvRecord);
        State originalState = null;
        List<GoogleSheetsFieldMapping> fieldMappings = converter.getFieldMappings();

        GoogleSheetsFieldMapping sectionMapping = GoogleSheetsFieldMapping.findMapping(
            fieldMappings,
            "displayName");
        SectionPage section = findSection(sectionMapping, recordsToPublish);
        if (section == null) {
            section = createSection(sectionMapping);
        } else {
            if (!section.getState().isNew()) {
                originalState = section.getState();
                section = (SectionPage) section.clone();
            }
        }

        if (section == null) {
            return records;
        }

        GoogleSheetsFieldMapping parentMapping = GoogleSheetsFieldMapping.findMapping(
            fieldMappings,
            "parent");
        SectionPage parentSection = findSection(parentMapping, recordsToPublish);
        if (parentSection == null) {
            parentSection = createSection(parentMapping);
            if (parentSection != null) {
                records.add(parentSection);
                section.setParent(parentSection);
            }
        } else {
            section.setParent(parentSection);
        }

        Optional.ofNullable(GoogleSheetsFieldMapping.findMapping(fieldMappings, "description"))
            .map(GoogleSheetsFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .ifPresent(section::setDescription);

        if (originalState != null) {
            Map<String, Map<String, Object>> diffMap = Draft.findDifferences(
                Database.Static.getDefault().getEnvironment(),
                originalState.getSimpleValues(),
                section.getState().getSimpleValues());
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
            records.add(section);
        }

        return records;
    }

    @Override
    public List<String> getFieldsToConvert() {
        return INCOMING_FIELDS;
    }

    private SectionPage findSection(GoogleSheetsFieldMapping fieldMapping, Set<Record> recordsToPublish) {
        String name = Optional.ofNullable(fieldMapping)
            .map(GoogleSheetsFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .orElse(null);

        if (name == null) {
            return null;
        }

        return Query.from(SectionPage.class)
                .where("google.drive.importKey = ?", name)
                .findFirst()
                .orElseGet(() -> recordsToPublish.stream()
                        .filter(SectionPage.class::isInstance)
                        .map(SectionPage.class::cast)
                        .filter(s -> s.getDisplayName().equals(name))
                        .findFirst()
                        .orElse(null));
    }

    private SectionPage createSection(GoogleSheetsFieldMapping fieldMapping) {
        return Optional.ofNullable(fieldMapping)
            .map(GoogleSheetsFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .map(name -> ObjectUtils.build(new SectionPage(), section -> {
                section.setInternalName(name);
                section.setDisplayName(name);
                GoogleDriveSectionPageModification sectionPageModification = section.as(
                    GoogleDriveSectionPageModification.class);
                sectionPageModification.setOriginalImportedGoogleSheetSection(this);
                sectionPageModification.setImportKey(name);
            }))
                .orElse(null);
    }
}
