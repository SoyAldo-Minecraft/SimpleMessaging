package com.soyaldo.simplemessaging;

import com.soyaldo.simplemessaging.channel.interfaces.ChannelSubscriber;
import com.soyaldo.simplemessaging.message.types.Message;
import com.soyaldo.simplemessaging.node.Node;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class API {

    private final Node node;

    public void sendMessage(String channel, Message message) {
        node.sendMessage(channel, message);
    }

    public void addChannelSubscribe(String channelName, ChannelSubscriber channelSubscriber) {
        node.addChannelSubscribe(channelName, channelSubscriber);
    }

    public void removeChannelSubscribe(String channelName, ChannelSubscriber channelSubscriber) {
        node.removeChannelSubscribe(channelName, channelSubscriber);
    }

}