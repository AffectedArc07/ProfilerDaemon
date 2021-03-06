/*
 * This file is generated by jOOQ.
 */
package me.aa07.profilerdaemon.database;


import me.aa07.profilerdaemon.database.tables.Procs;
import me.aa07.profilerdaemon.database.tables.Samples;
import me.aa07.profilerdaemon.database.tables.records.ProcsRecord;
import me.aa07.profilerdaemon.database.tables.records.SamplesRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * paradise_profilerdaemon.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ProcsRecord> KEY_PROCS_PRIMARY = Internal.createUniqueKey(Procs.PROCS, DSL.name("KEY_procs_PRIMARY"), new TableField[] { Procs.PROCS.ID }, true);
    public static final UniqueKey<ProcsRecord> KEY_PROCS_PROCPATH = Internal.createUniqueKey(Procs.PROCS, DSL.name("KEY_procs_procpath"), new TableField[] { Procs.PROCS.PROCPATH }, true);
    public static final UniqueKey<SamplesRecord> KEY_SAMPLES_PRIMARY = Internal.createUniqueKey(Samples.SAMPLES, DSL.name("KEY_samples_PRIMARY"), new TableField[] { Samples.SAMPLES.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<SamplesRecord, ProcsRecord> FK1_PROCID_PROCS_ID = Internal.createForeignKey(Samples.SAMPLES, DSL.name("FK1_procId_procs.id"), new TableField[] { Samples.SAMPLES.PROCID }, Keys.KEY_PROCS_PRIMARY, new TableField[] { Procs.PROCS.ID }, true);
}
