package brightspot.microsoft.drives.conversion.section;

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
import brightspot.section.SectionPage;
import com.psddev.cms.db.Draft;
import com.psddev.cms.db.ToolUser;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Middleman record connecting a {@link MicrosoftDrivesSpreadsheetConverter} to the {@link SectionPage} that resulted
 * from the conversion process.
 */
public class ImportedMicrosoftSpreadsheetSection extends AbstractImportedMicrosoftSpreadsheet<SectionPage> {

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

        if (!SectionPage.class.equals(ObjectUtils.getClassByName(converter.getConvertToType()))) {
            return Collections.emptyList();
        }

        List<Record> records = new ArrayList<>();
        setCsvRecord(csvRecord);
        State originalState = null;
        List<MicrosoftDrivesSpreadsheetFieldMapping> fieldMappings = converter.getFieldMappings();

        MicrosoftDrivesSpreadsheetFieldMapping sectionMapping = MicrosoftDrivesSpreadsheetFieldMapping.findMapping(
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

        MicrosoftDrivesSpreadsheetFieldMapping parentMapping = MicrosoftDrivesSpreadsheetFieldMapping.findMapping(
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

        Optional.ofNullable(MicrosoftDrivesSpreadsheetFieldMapping.findMapping(fieldMappings, "description"))
            .map(MicrosoftDrivesSpreadsheetFieldMapping::getColumn)
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
                draft.setName(MicrosoftDrivesSyncable.generateMicrosoftDrivesReimportName());
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

    private SectionPage findSection(MicrosoftDrivesSpreadsheetFieldMapping fieldMapping, Set<Record> recordsToPublish) {
        String name = Optional.ofNullable(fieldMapping)
            .map(MicrosoftDrivesSpreadsheetFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .orElse(null);

        if (name == null) {
            return null;
        }

        return Query.from(SectionPage.class)
            .where("microsoft.drives.importKey = ?", name)
            .findFirst()
            .orElseGet(() -> recordsToPublish.stream()
                .filter(SectionPage.class::isInstance)
                .map(SectionPage.class::cast)
                .filter(s -> s.getDisplayName().equals(name))
                .findFirst()
                .orElse(null));
    }

    private SectionPage createSection(MicrosoftDrivesSpreadsheetFieldMapping fieldMapping) {
        return Optional.ofNullable(fieldMapping)
            .map(MicrosoftDrivesSpreadsheetFieldMapping::getColumn)
            .map(getCsvRecord()::get)
            .filter(StringUtils::isNotBlank)
            .map(name -> ObjectUtils.build(new SectionPage(), section -> {
                section.setInternalName(name);
                section.setDisplayName(name);
                MicrosoftDrivesSectionPageModification sectionPageModification = section.as(
                    MicrosoftDrivesSectionPageModification.class);
                sectionPageModification.setOriginalImportedMicrosoftSpreadsheetSection(this);
                sectionPageModification.setImportKey(name);
            }))
            .orElse(null);
    }
}
