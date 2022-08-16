/*
 * This file is generated by jOOQ.
 */
package me.aa07.profilerdaemon.database;


import me.aa07.profilerdaemon.database.tables.SendmapsSamples;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables in paradise_profilerdaemon.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index SENDMAPS_SAMPLES_FK1_PROCID_SENDMAPS_PROCS_ID = Internal.createIndex(DSL.name("FK1_procId_sendmaps_procs.id"), SendmapsSamples.SENDMAPS_SAMPLES, new OrderField[] { SendmapsSamples.SENDMAPS_SAMPLES.PROCID }, false);
}
