package brightspot.core.tool;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.psddev.dari.util.ObjectUtils;
import org.joda.time.DateTime;
import org.threeten.bp.ZonedDateTime;

public class CronUtils {

    public static DateTime getNextExecutionTime(String cronExpression) {

        Cron cron = getCron(cronExpression);
        if (cron == null) {
            return null;
        }

        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        ZonedDateTime nextExecutionTime = executionTime.nextExecution(ZonedDateTime.now()).orNull();

        return nextExecutionTime != null
            ? new DateTime((nextExecutionTime.toInstant().toEpochMilli()))
            : null;
    }

    public static DateTime getPreviousExecutionTime(String cronExpression) {

        Cron cron = getCron(cronExpression);
        if (cron == null) {
            return null;
        }

        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        ZonedDateTime previousExecutionTime = executionTime.lastExecution(ZonedDateTime.now()).orNull();

        return previousExecutionTime != null
            ? new DateTime((previousExecutionTime.toInstant().toEpochMilli()))
            : null;
    }

    public static String getCronDescription(String cronExpression) {

        Cron cron = getCron(cronExpression);

        return cron != null
            ? CronDescriptor.instance().describe(cron)
            : null;
    }

    private static Cron getCron(String cronExpression) {

        if (ObjectUtils.isBlank(cronExpression)) {
            return null;
        }
        cronExpression = cronExpression.trim();

        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
        CronParser parser = new CronParser(cronDefinition);

        try {
            return parser.parse(cronExpression);
        } catch (Exception e) {
            return null;
        }
    }
}
