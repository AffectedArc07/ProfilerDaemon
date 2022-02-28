package me.aa07.profilerdaemon.core;

import com.moandjiezana.toml.Toml;
import java.io.File;
import me.aa07.profilerdaemon.core.config.ConfigHolder;
import me.aa07.profilerdaemon.core.database.DbCore;
import me.aa07.profilerdaemon.core.redis.RedisManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Core {
    private Logger logger;
    private ConfigHolder configuration;
    private DbCore dbCore;
    private Worker worker;
    private Thread workerThread;
    private RedisManager redisManager;
    private Thread redisthread;

    public void run() {
        logger = LogManager.getLogger(Core.class);
        logger.info("Starting up");
        logger.info("Loading configuration...");
        try {
            File configfile = new File("config.toml");
            configuration = new Toml().read(configfile).to(ConfigHolder.class);
        } catch (Exception exception) {
            logger.fatal("Failed to load config.toml!");
            exception.printStackTrace();
            return;
        }

        dbCore = new DbCore(configuration.database, logger);

        setupThreads();
        launchAll();
    }

    private void setupThreads() {
        worker = new Worker(dbCore, logger);
        workerThread = new Thread(worker::run, "worker");
        redisManager = new RedisManager(configuration.redis, worker, logger);
        redisthread = new Thread(redisManager::run, "redis");
    }

    private void launchAll() {
        workerThread.start();
        redisthread.start();
    }
}
