package me.aa07.profilerdaemon.core.processors;

import com.google.gson.Gson;
import me.aa07.profilerdaemon.core.database.DbCore;
import me.aa07.profilerdaemon.core.models.ProfilerHolder;
import org.apache.logging.log4j.Logger;

public abstract class BaseProcessor {
    protected DbCore database;
    protected Logger logger;
    protected Gson gson;
    private String processorName;

    public BaseProcessor(DbCore database, Logger logger, String processorName) {
        this.database = database;
        this.logger = logger;
        this.processorName = processorName;

        gson = new Gson();
    }

    protected void log(String txt) {
        logger.info(String.format("[%s] %s", processorName, txt));
    }

    public void processData(ProfilerHolder holder) {
        long start = System.currentTimeMillis();
        log("Starting processing");
        doProcessing(holder);
        long duration = System.currentTimeMillis() - start;
        log(String.format("Processing complete within %sms", duration));
    }

    protected abstract void doProcessing(ProfilerHolder holder);

    protected abstract long getProcId(String procname);
}
