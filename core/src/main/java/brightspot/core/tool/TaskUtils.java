package brightspot.core.tool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Consumer;
import java.util.function.Function;

import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Application;
import com.psddev.dari.db.AsyncDatabaseWriter;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.SqlDatabase;
import com.psddev.dari.db.WriteOperation;
import com.psddev.dari.util.AsyncConsumer;
import com.psddev.dari.util.AsyncProducer;
import com.psddev.dari.util.AsyncQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TaskUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskUtils.class);

    private TaskUtils() {
    }

    /**
     * Process all objects that match the query with the given consumer and WriteOperation in an asynchronous task.
     *
     * @param db Database.
     * @param taskName Base task name.
     * @param logger Informational messages are sent to this function. Never null.
     * @param type Content type. Never null.
     * @param query The query to process. Never null.
     * @param processor Process the record and return true from this function to invoke the WriteOperation. Null will
     * send all records through to the writer.
     * @param writeOperation The WriteOperation to invoke. May be null.
     * @param numProcessors Number of processors to create.
     * @param numWriters Number of writers to create.
     * @param batchSize Passed to the AsyncDatabaseWriters.
     * @param commitEventually Passed to the AsyncDatabaseWriters.
     * @param finished This is executed after all other tasks have completed. May be null.
     */
    public static <T extends Recordable> void asyncProcessQuery(
        Database db,
        String taskName,
        Consumer<String> logger,
        Class<T> type,
        Query query,
        final Function<T, Boolean> processor,
        final WriteOperation writeOperation,
        int numProcessors,
        int numWriters,
        int batchSize,
        boolean commitEventually,
        Runnable finished) {

        String name = ObjectType.getInstance(type).getDisplayName();
        try {
            if (!query.timeout(300d).hasMoreThan(0)) {
                logger.accept("0 records, skipping.");
                if (finished != null) {
                    finished.run();
                }
                return;
            }
        } catch (RuntimeException e) {
            logger.accept(e.getMessage());
        }

        Database database = db != null ? db : Database.Static.getDefault();
        AsyncQueue<T> queue = new AsyncQueue<>(new ArrayBlockingQueue<>(batchSize * numProcessors));
        AsyncQueue<T> writeQueue =
            writeOperation != null ? new AsyncQueue<>(new ArrayBlockingQueue<>(batchSize * numWriters)) : null;

        query.option(SqlDatabase.USE_JDBC_FETCH_SIZE_QUERY_OPTION, false);
        query.noCache();
        // Reimplementation of AsyncDatabaseReader that uses batchSize as the argument to iterable().
        new AsyncProducer<T>(taskName + " (Read)", queue) {

            private final Iterator<?> iterator;

            {
                this.iterator = query.using(database).iterable(batchSize * numWriters).iterator();
            }

            @Override
            protected T produce() {
                Object obj = iterator.hasNext() ? iterator.next() : null;
                if (type.isInstance(obj)) {
                    @SuppressWarnings("unchecked")
                    T value = (T) obj;
                    return value;

                } else {
                    return null;
                }
            }
        }.submit();

        CyclicBarrier running = new CyclicBarrier(numProcessors, () -> {
            if (writeQueue != null) {
                writeQueue.closeAutomatically();
            }
            if (finished != null) {
                finished.run();
            }
        });

        if (writeOperation != null) {
            for (int i = 0; i < numWriters; i++) {
                new AsyncDatabaseWriter<>(
                    taskName + " (Write)",
                    writeQueue,
                    database,
                    writeOperation,
                    batchSize,
                    commitEventually).submit();
            }
        }

        for (int i = 0; i < numProcessors; i++) {
            new AsyncConsumer<T>(taskName + " (Process)", queue) {

                @Override
                protected void consume(T item) throws Exception {
                    if (processor == null || processor.apply(item)) {
                        if (writeOperation != null) {
                            writeQueue.add(item);
                        }
                    }
                }

                @Override
                protected void finished() {
                    try {
                        running.await();
                    } catch (InterruptedException | BrokenBarrierException ignored) {
                        // ignored
                    }
                }

            }.submit();
        }

        queue.closeAutomatically();

        logger.accept("Started " + taskName + " tasks.");
    }

    public static boolean isRunningOnTaskHost() {
        CmsTool globalSettings = Application.Static.getInstance(CmsTool.class);
        if (globalSettings != null) {
            String defaultTaskHost = globalSettings.as(CmsToolModification.class).getDefaultTaskHost();
            if (!StringUtils.isEmpty(defaultTaskHost)) {
                try {
                    InetAddress localAddress = InetAddress.getLocalHost();
                    InetAddress allowedAddress = InetAddress.getByName(defaultTaskHost);
                    if (localAddress.getHostAddress().equals(allowedAddress.getHostAddress())) {
                        return true;
                    }
                } catch (UnknownHostException e) {
                    LOGGER.warn("Exception finding host name; message: " + e.getMessage());
                }
            }
        }
        LOGGER.debug("Returning false for TaskUtils#isRunningOnTaskHost(). Check default task host.");
        return false;
    }
}
