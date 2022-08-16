package me.aa07.profilerdaemon.core;

import com.google.gson.Gson;
import java.util.ArrayList;
import me.aa07.profilerdaemon.core.database.DbCore;
import me.aa07.profilerdaemon.core.models.ProfilerHolder;
import me.aa07.profilerdaemon.core.processors.ProcProcessor;
import me.aa07.profilerdaemon.core.processors.SendmapsProcessor;
import org.apache.logging.log4j.Logger;

public class Worker {
    private ArrayList<String> queue;
    private Logger logger;
    private Object listLock;
    private Gson gson;

    private ProcProcessor procProcessor;
    private SendmapsProcessor sendmapsProcessor;

    public Worker(DbCore database, Logger logger) {
        this.logger = logger;

        procProcessor = new ProcProcessor(database, logger);
        sendmapsProcessor = new SendmapsProcessor(database, logger);

        queue = new ArrayList<String>();
        listLock = new Object();
        gson = new Gson();
    }

    public void run() {
        logger.info("Starting...");

        try {
            while (true) {
                String current_run = safePop();
                if (current_run == null) {
                    Thread.sleep(10000);
                    continue;
                }
                processEntry(current_run);

            }
        } catch (InterruptedException exception) {
            logger.fatal("Worker thread was interrupted!");
            exception.printStackTrace();
            System.exit(1);
        }
    }

    // Add something to the list in a thread-safe matter
    public void safeAdd(String data) {
        synchronized (listLock) {
            queue.add(data);
        }
    }

    // Extract one item from the list in a thread-safe matter
    private String safePop() {
        String output = "";
        synchronized (listLock) {
            if (queue.size() > 0) {
                output = queue.get(0);
                queue.remove(0);
            } else {
                return null;
            }
        }

        return output;
    }

    private void processEntry(String entry) {
        long start = System.currentTimeMillis();
        logger.info(String.format("Processing entry with size %s bytes", entry.length()));

        ProfilerHolder holder = gson.fromJson(entry, ProfilerHolder.class);
        logger.info(String.format("Round ID: %s | Profile size: %s bytes", holder.roundId, holder.profilerData.length()));

        procProcessor.processData(holder);
        sendmapsProcessor.processData(holder);

        long duration = System.currentTimeMillis() - start;
        logger.info(String.format("All processing complete within %sms", duration));
    }
}
