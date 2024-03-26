package com.soyaldo.simplemessaging;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.soyaldo.simplemessaging.channel.ChannelManager;
import com.soyaldo.simplemessaging.channel.interfaces.ChannelSubscriber;
import com.soyaldo.simplemessaging.channel.models.Channel;
import com.soyaldo.simplemessaging.message.MessageManager;
import com.soyaldo.simplemessaging.message.types.Message;
import com.soyaldo.simplemessaging.node.Node;
import com.soyaldo.simplemessaging.node.NodeSettings;
import com.soyaldo.simplemessaging.redis.RedisManager;
import com.soyaldo.simplemessaging.utils.BytesAndStrings;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class API {

    private final Node node;

    public void sendMessage(String channel, Message message) {
        // Getting the node settings.
        NodeSettings nodeSettings = node.getNodeSettings();
        // Getting the message manager.
        MessageManager messageManager = node.getMessageManager();
        // Adding the message on the message manager.
        messageManager.addMessage(message);
        // Getting the java plugin.
        JavaPlugin javaPlugin = node.getJavaPlugin();
        // Starting async scheduler with the message fallback.
        javaPlugin.getServer().getScheduler().runTaskLaterAsynchronously(javaPlugin, () -> {
            if (messageManager.existMessage(message.getId())) {
                message.fallback();
                messageManager.removeMessage(message.getId());
            }
        }, nodeSettings.getMessageResponseLimit() * 20L);
        // Creating the byte array with the message data.
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(channel);
        output.writeUTF(message.getId());
        output.writeUTF(message.getType().toString());
        output.writeUTF(nodeSettings.getName());
        output.writeUTF(message.getServerTo());
        output.writeUTF(BytesAndStrings.translate(message.getContent()));
        // Send the message.
        RedisManager redisManager = node.getRedisManager();
        redisManager.sendMessage(output.toByteArray());
    }

    public void addChannelSubscribe(String channelName, ChannelSubscriber channelSubscriber) {
        ChannelManager channelManager = node.getChannelManager();
        Channel channel;
        if (!channelManager.existChannel(channelName)) {
            channelManager.addChannel(new Channel(channelName));
        }
        channel = channelManager.getChannel(channelName);
        channel.addChannelSubscriber(channelSubscriber);
    }

    public void removeChannelSubscribe(String channelName, ChannelSubscriber channelSubscriber) {
        ChannelManager channelManager = node.getChannelManager();
        if (channelManager.existChannel(channelName)) {
            Channel channel = channelManager.getChannel(channelName);
            channel.removeChannelSubscriber(channelSubscriber);
            if (channel.getChannelSubscribers().isEmpty()) {
                channelManager.removeChannel(channelName);
            }
        }
    }

}