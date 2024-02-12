package com.soyaldo.simplemessaging.redis;

import com.soyaldo.simplemessaging.SimpleMessaging;
import com.soyaldo.simplemessaging.runnables.SubscribeRunnable;
import com.soyaldo.simplemessaging.utils.BytesAndStrings;
import com.soyaldo.simplemessaging.utils.redis.RedisInfo;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisAccessControlException;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisManager {

    private final SimpleMessaging plugin;

    @Getter
    private RedisInfo redisInfo;
    @Getter
    private JedisPool jedisPool;
    @Getter
    private Thread suscriberThread;

    public RedisManager(SimpleMessaging plugin) {
        this.plugin = plugin;
    }

    public void registerManager() {
        reloadManager();
    }

    public void reloadManager() {
        if (jedisPool != null) jedisPool.close();
        if (suscriberThread != null) suscriberThread.interrupt();

        FileConfiguration settings = plugin.getSettings().getFileConfiguration();
        // RedisInfo
        String host = settings.getString("redis.host", "localhost");
        int port = settings.getInt("redis.port", 6379);
        String password = settings.getString("redis.password", "");
        int maxConnections = settings.getInt("redis.port", 10);
        int maxIdleConnections = settings.getInt("redis.maxIdleConnections", 5);
        redisInfo = new RedisInfo(host, port, password, maxConnections, maxIdleConnections);

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxConnections);
        jedisPoolConfig.setMaxIdle(maxIdleConnections);

        // Connect
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            try {
                jedisPool = new JedisPool(jedisPoolConfig, host, port, 2000, password);

                // Subscriber thread
                try {
                    Jedis jedis = jedisPool.getResource();
                    suscriberThread = new Thread(new SubscribeRunnable(plugin, jedis));
                    suscriberThread.start();
                } catch (Exception e) {
                    plugin.getDebugger().error("Error starting subscriber thread.");
                }
            } catch (JedisConnectionException | JedisAccessControlException e) {
                plugin.getDebugger().error("Error to connect to redis server: " + e.getMessage());
            }
        }, 20L);
    }

    public void sendMessage(byte[] bytes) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish("simpleMessaging", BytesAndStrings.translate(bytes));
        } catch (Exception e) {
            plugin.getDebugger().error("Error sending the message.");
        }
    }

}