package me.aa07.profilerdaemon.core.database;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import javax.sql.DataSource;
import me.aa07.profilerdaemon.core.config.DatabaseConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DbCore {
    private DatabaseConfiguration config;
    private DataSource dbcon;

    public DbCore(DatabaseConfiguration config, Logger logger) {
        this.config = config;
        // Suppress JOOQ console spam
        System.getProperties().setProperty("org.jooq.no-logo", "true");
        System.getProperties().setProperty("org.jooq.no-tips", "true");

        establishDb();
        logger.info("Ready to handle DB requests");
    }

    private DataSource openDataSource(String url, String username, String password) {
        BasicDataSource source = new BasicDataSource();
        source.addConnectionProperty("autoReconnect", "true");
        source.addConnectionProperty("allowMultiQueries", "true");
        source.addConnectionProperty("zeroDateTimeBehavior", "convertToNull");
        source.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl(url);
        source.setUsername(username);
        source.setPassword(password);
        source.setMaxTotal(8);
        source.setMaxIdle(8);
        source.setTimeBetweenEvictionRunsMillis(180 * 1000);
        source.setSoftMinEvictableIdleTimeMillis(180 * 1000);

        return source;
    }

    private void establishDb() {
        dbcon = openDataSource(String.format("jdbc:mysql://%s/%s", config.host, config.database), config.username, config.password);
    }

    // Get a DSL context
    public DSLContext jooq() {
        return DSL.using(dbcon, SQLDialect.MYSQL);
    }

    // Easy way for NOW() in SQL
    public LocalDateTime now() {
        return new Timestamp(new Date().getTime()).toLocalDateTime();
    }

}
