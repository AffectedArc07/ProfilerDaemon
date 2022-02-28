package me.aa07.profilerdaemon.core.redis;

import me.aa07.profilerdaemon.core.Worker;
import me.aa07.profilerdaemon.core.config.RedisConfig;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;


public class RedisManager extends JedisPubSub {
    private JedisPool pool;
    private RedisConfig config;
    private Logger logger;
    private Worker worker;

    public RedisManager(RedisConfig config, Worker worker, Logger logger) {
        this.config = config;
        this.worker = worker;
        this.logger = logger;
    }

    public void run() {
        logger.info("Starting...");
        pool = new JedisPool(config.host);
        logger.info("Connected. Subscribing...");
        pool.getResource().subscribe(this, "profilerdaemon.input");
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals("profilerdaemon.input")) {
            return; // Somehow
        }

        worker.safeAdd(message);
        logger.info("Queued a new profiler dump");
    }

}
