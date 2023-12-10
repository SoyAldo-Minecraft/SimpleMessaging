package com.soyaldo.simplemessaging;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.soyaldo.simplemessaging.channel.ChannelManager;
import com.soyaldo.simplemessaging.channel.interfaces.ChannelSubscriber;
import com.soyaldo.simplemessaging.channel.models.Channel;
import com.soyaldo.simplemessaging.message.MessageManager;
import com.soyaldo.simplemessaging.message.types.Message;
import com.soyaldo.simplemessaging.utils.BytesAndStrings;

public class API {

    private final SimpleMessaging plugin;

    public API(SimpleMessaging plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(String channel, Message message) {
        MessageManager messageManager = plugin.getMessageManager();
        messageManager.addMessage(message);
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (messageManager.existMessage(message.getId())) {
                ((Message) message).fallback();
                messageManager.removeMessage(message.getId());
            }
        }, plugin.getResponseLimit() * 20L);

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(channel);
        output.writeUTF(message.getId());
        output.writeUTF(message.getType().toString());
        output.writeUTF(plugin.getServerName());
        output.writeUTF(message.getServerTo());
        output.writeUTF(BytesAndStrings.translate(message.getContent()));
        plugin.getRedisManager().sendMessage(output.toByteArray());
    }

    public void addChannelSubscribe(String channelName, ChannelSubscriber channelSubscriber) {
        ChannelManager channelManager = plugin.getChannelManager();
        Channel channel;
        if (!channelManager.existChannel(channelName)) {
            channelManager.addChannel(new Channel(channelName));
        }
        channel = channelManager.getChannel(channelName);
        channel.addChannelSubscriber(channelSubscriber);
    }

    public void removeChannelSubscribe(String channelName, ChannelSubscriber channelSubscriber) {
        ChannelManager channelManager = plugin.getChannelManager();
        if (channelManager.existChannel(channelName)) {
            Channel channel = channelManager.getChannel(channelName);
            channel.removeChannelSubscriber(channelSubscriber);
            if (channel.getChannelSubscribers().isEmpty()) {
                channelManager.removeChannel(channelName);
            }
        }
    }

}