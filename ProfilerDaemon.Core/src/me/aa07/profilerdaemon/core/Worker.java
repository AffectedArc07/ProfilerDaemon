package me.aa07.profilerdaemon.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import me.aa07.profilerdaemon.core.database.DbCore;
import me.aa07.profilerdaemon.core.models.ProcData;
import me.aa07.profilerdaemon.core.models.ProfilerHolder;
import me.aa07.profilerdaemon.database.Tables;
import me.aa07.profilerdaemon.database.tables.records.ProcsRecord;
import me.aa07.profilerdaemon.database.tables.records.SamplesRecord;
import org.apache.logging.log4j.Logger;

public class Worker {
    private ArrayList<String> queue;
    private DbCore database;
    private Logger logger;
    private Object listLock;
    private Gson gson;

    public Worker(DbCore database, Logger logger) {
        this.database = database;
        this.logger = logger;
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

        List<ProcData> proc_list = new Gson().fromJson(
            holder.profilerData, new TypeToken<List<ProcData>>() {}.getType()
        );

        logger.info(String.format("Procs logged: %s", proc_list.size()));

        for (ProcData procdata : proc_list) {
            long proc_id = getProcId(procdata.name);
            SamplesRecord sr = database.jooq().newRecord(Tables.SAMPLES);
            sr.setRoundid(holder.roundId);
            sr.setSampletime(database.now());
            sr.setProcid(proc_id);
            sr.setSelf(procdata.self);
            sr.setTotal(procdata.total);
            sr.setReal(procdata.real);
            sr.setOver(procdata.over);
            sr.setCalls(procdata.calls);
            sr.store();
        }

        long duration = System.currentTimeMillis() - start;
        logger.info(String.format("Processing complete within %sms", duration));
    }

    // Gets the proc ID from the DB, inserting if necessary
    private long getProcId(String procname) {
        if (!database.jooq().fetchExists(database.jooq().select(Tables.PROCS.ID).from(Tables.PROCS).where(Tables.PROCS.PROCPATH.eq(procname)))) {
            // We dont exist, make us
            logger.info(String.format("%s did not exist in the DB. It does now.", procname));

            ProcsRecord record = database.jooq().newRecord(Tables.PROCS);
            record.setProcpath(procname);
            record.store();
            return record.getId();
        }

        // We do exist, just grab
        return database.jooq().select(Tables.PROCS.ID).from(Tables.PROCS).where(Tables.PROCS.PROCPATH.eq(procname)).fetchOne().value1();
    }
}
