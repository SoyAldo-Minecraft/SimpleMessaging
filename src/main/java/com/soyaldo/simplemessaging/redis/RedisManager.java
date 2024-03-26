package com.soyaldo.simplemessaging.redis;

import com.soyaldo.simplemessaging.node.Node;
import com.soyaldo.simplemessaging.node.NodeSettings;
import com.soyaldo.simplemessaging.runnables.SubscribeRunnable;
import com.soyaldo.simplemessaging.utils.BytesAndStrings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisAccessControlException;
import redis.clients.jedis.exceptions.JedisConnectionException;

@RequiredArgsConstructor
@Getter
public class RedisManager {

    private final Node node;

    private JedisPool jedisPool;
    private Thread subscriberThread;

    public void registerManager() {
        reloadManager();
    }

    public void reloadManager() {
        if (jedisPool != null) jedisPool.close();
        if (subscriberThread != null) subscriberThread.interrupt();
        // Getting the java plugin.
        JavaPlugin javaPlugin = node.getJavaPlugin();
        // Starting the subscriber.
        javaPlugin.getServer().getScheduler().runTaskLater(javaPlugin, () -> {
            try {
                // Getting the node settings.
                NodeSettings nodeSettings = node.getNodeSettings();
                // Creating the jedis pool config.
                JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                jedisPoolConfig.setMaxTotal(nodeSettings.getRedisMaxConnections());
                jedisPoolConfig.setMaxIdle(nodeSettings.getRedisMaxIdleConnections());
                // Starting the jedis pool.
                jedisPool = new JedisPool(jedisPoolConfig, nodeSettings.getRedisHost(), nodeSettings.getRedisPort(), 2000, nodeSettings.getRedisPassword());
                // Trying start the subscriber thread.
                try {
                    subscriberThread = new Thread(new SubscribeRunnable(node));
                    subscriberThread.start();
                } catch (Exception e) {
                    javaPlugin.getLogger().warning("Error starting subscriber thread.");
                }
            } catch (JedisConnectionException | JedisAccessControlException e) {
                javaPlugin.getLogger().warning("Error to connect to redis server: " + e.getMessage());
            }
        }, 20L);
    }

    public void sendMessage(byte[] bytes) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(node.getNodeSettings().getChannel(), BytesAndStrings.translate(bytes));
        } catch (Exception e) {
            node.getJavaPlugin().getLogger().warning("Error sending the message.");
        }
    }

}