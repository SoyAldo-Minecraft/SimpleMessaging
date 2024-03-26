package com.soyaldo.simplemessaging.node;

import com.soyaldo.simplemessaging.channel.ChannelManager;
import com.soyaldo.simplemessaging.message.MessageManager;
import com.soyaldo.simplemessaging.redis.RedisManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
@Getter
public class Node {

    private final JavaPlugin javaPlugin;
    // Node settings.
    private final NodeSettings nodeSettings;
    // Managers
    private RedisManager redisManager;
    private ChannelManager channelManager;
    private MessageManager messageManager;

    public void register() {
        // Initialize a new instance.
        redisManager = new RedisManager(this);
        channelManager = new ChannelManager();
        messageManager = new MessageManager();
        // Managers
        redisManager.registerManager();
        channelManager.registerManager();
        messageManager.registerManager();
    }

}