package com.soyaldo.simplemessaging.redis;

import com.soyaldo.simplemessaging.SimpleMessaging;
import com.soyaldo.simplemessaging.runnables.SubscribeRunnable;
import com.soyaldo.simplemessaging.utils.BytesAndStrings;
import com.soyaldo.simplemessaging.utils.redis.Redis;
import com.soyaldo.simplemessaging.utils.redis.RedisInfo;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.exceptions.JedisAccessControlException;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisManager {

    private final SimpleMessaging plugin;

    @Getter
    private RedisInfo redisInfo;
    @Getter
    private Redis sender;
    @Getter
    private Redis subscriber;
    @Getter
    private Thread suscriberThread;

    public RedisManager(SimpleMessaging plugin) {
        this.plugin = plugin;
    }

    public void registerManager() {
        reloadManager();
    }

    public void reloadManager() {
        if (sender != null) sender.disconnect();
        if (subscriber != null) subscriber.disconnect();
        if (suscriberThread != null) suscriberThread.interrupt();
        FileConfiguration settings = plugin.getSettings().getFileConfiguration();
        // RedisInfo
        String host = settings.getString("redis.host", "localhost");
        int port = settings.getInt("redis.port", 6379);
        String password = settings.getString("redis.password", "");
        redisInfo = new RedisInfo(host, port, password);
        // Connect
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            try {
                // Sender connection
                sender = new Redis(redisInfo);
                sender.connect();
                // Subscriber connection
                subscriber = new Redis(redisInfo);
                subscriber.connect();
                // Subscriber thread
                suscriberThread = new Thread(new SubscribeRunnable(plugin));
                suscriberThread.start();
            } catch (JedisConnectionException | JedisAccessControlException e) {
                plugin.getDebugger().error("Error to connect to redis server: " + e.getMessage());
            }
        }, 20L);
    }

    public void sendMessage(byte[] bytes) {
        plugin.getRedisManager().getSender().getJedis().publish("simpleMessaging", BytesAndStrings.translate(bytes));
    }

}