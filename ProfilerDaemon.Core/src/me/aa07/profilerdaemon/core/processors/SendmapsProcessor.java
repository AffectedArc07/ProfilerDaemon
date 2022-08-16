package me.aa07.profilerdaemon.core.processors;

import com.google.gson.reflect.TypeToken;
import java.util.List;
import me.aa07.profilerdaemon.core.database.DbCore;
import me.aa07.profilerdaemon.core.models.ProfilerHolder;
import me.aa07.profilerdaemon.core.models.SendmapsProcData;
import me.aa07.profilerdaemon.database.Tables;
import me.aa07.profilerdaemon.database.tables.records.SendmapsProcsRecord;
import me.aa07.profilerdaemon.database.tables.records.SendmapsSamplesRecord;
import org.apache.logging.log4j.Logger;

public class SendmapsProcessor extends BaseProcessor {
    public SendmapsProcessor(DbCore database, Logger logger) {
        super(database, logger, "SendmapsProcessor");
    }

    @Override
    protected void doProcessing(ProfilerHolder holder) {
        List<SendmapsProcData> proc_list = gson.fromJson(
            holder.sendmapsData, new TypeToken<List<SendmapsProcData>>() {}.getType()
        );

        log(String.format("Procs logged: %s", proc_list.size()));

        for (SendmapsProcData procdata : proc_list) {
            long proc_id = getProcId(procdata.name);
            SendmapsSamplesRecord sr = database.jooq().newRecord(Tables.SENDMAPS_SAMPLES);
            sr.setRoundid(holder.roundId);
            sr.setSampletime(database.now());
            sr.setProcid(proc_id);
            sr.setValue(procdata.value);
            sr.setCalls(procdata.calls);
            sr.store();
        }
    }

    @Override
    protected long getProcId(String procname) {
        if (!database.jooq().fetchExists(database.jooq().select(Tables.SENDMAPS_PROCS.ID).from(Tables.SENDMAPS_PROCS).where(Tables.SENDMAPS_PROCS.PROCPATH.eq(procname)))) {
            // We dont exist, make us
            log(String.format("%s did not exist in the DB. It does now.", procname));

            SendmapsProcsRecord record = database.jooq().newRecord(Tables.SENDMAPS_PROCS);
            record.setProcpath(procname);
            record.store();
            return record.getId();
        }

        // We do exist, just grab
        return database.jooq().select(Tables.SENDMAPS_PROCS.ID).from(Tables.SENDMAPS_PROCS).where(Tables.SENDMAPS_PROCS.PROCPATH.eq(procname)).fetchOne().value1();
    }
}
