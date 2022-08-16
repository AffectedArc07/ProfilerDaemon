package me.aa07.profilerdaemon.core.processors;

import com.google.gson.reflect.TypeToken;
import java.util.List;
import me.aa07.profilerdaemon.core.database.DbCore;
import me.aa07.profilerdaemon.core.models.ProcData;
import me.aa07.profilerdaemon.core.models.ProfilerHolder;
import me.aa07.profilerdaemon.database.Tables;
import me.aa07.profilerdaemon.database.tables.records.ProcsRecord;
import me.aa07.profilerdaemon.database.tables.records.SamplesRecord;
import org.apache.logging.log4j.Logger;

public class ProcProcessor extends BaseProcessor {
    public ProcProcessor(DbCore database, Logger logger) {
        super(database, logger, "ProcProcessor");
    }

    @Override
    protected void doProcessing(ProfilerHolder holder) {
        List<ProcData> proc_list = gson.fromJson(
            holder.profilerData, new TypeToken<List<ProcData>>() {}.getType()
        );

        log(String.format("Procs logged: %s", proc_list.size()));

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
    }

    @Override
    protected long getProcId(String procname) {
        if (!database.jooq().fetchExists(database.jooq().select(Tables.PROCS.ID).from(Tables.PROCS).where(Tables.PROCS.PROCPATH.eq(procname)))) {
            // We dont exist, make us
            log(String.format("%s did not exist in the DB. It does now.", procname));

            ProcsRecord record = database.jooq().newRecord(Tables.PROCS);
            record.setProcpath(procname);
            record.store();
            return record.getId();
        }

        // We do exist, just grab
        return database.jooq().select(Tables.PROCS.ID).from(Tables.PROCS).where(Tables.PROCS.PROCPATH.eq(procname)).fetchOne().value1();
    }
}
