package com.soyaldo.simplemessaging.channel.interfaces;

public interface ChannelSubscriber {

    byte[] onMessageReceived(String fromServer, byte[] content);

}