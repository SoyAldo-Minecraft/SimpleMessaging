package com.soyaldo.simplemessaging.channel.models;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.soyaldo.simplemessaging.SimpleMessaging;
import com.soyaldo.simplemessaging.channel.interfaces.ChannelSubscriber;
import com.soyaldo.simplemessaging.message.enums.MessageType;
import com.soyaldo.simplemessaging.utils.BytesAndStrings;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Channel {

    private final String name;
    private final List<ChannelSubscriber> channelSubscribers = new ArrayList<>();

    public boolean existChannelSubscriber(ChannelSubscriber channelSubscriber) {
        return channelSubscribers.contains(channelSubscriber);
    }

    public void addChannelSubscriber(ChannelSubscriber channelSubscriber) {
        channelSubscribers.add(channelSubscriber);
    }

    public void removeChannelSubscriber(ChannelSubscriber channelSubscriber) {
        channelSubscribers.remove(channelSubscriber);
    }

    public void sendMessage(SimpleMessaging plugin, String messageId, String fromServer, byte[] content) {
        for (ChannelSubscriber channelSubscriber : channelSubscribers) {
            byte[] response = channelSubscriber.onMessageReceived(fromServer, content);
            if (response != null) {
                ByteArrayDataOutput output = ByteStreams.newDataOutput();
                output.writeUTF(getName());
                output.writeUTF(messageId);
                output.writeUTF(MessageType.RESPONSE.toString());
                output.writeUTF(plugin.getServerName());
                output.writeUTF(fromServer);
                output.writeUTF(BytesAndStrings.translate(response));
                plugin.getRedisManager().sendMessage(output.toByteArray());
            }
        }
    }

}