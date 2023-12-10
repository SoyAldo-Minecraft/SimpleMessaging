package com.soyaldo.simplemessaging.channel;

import com.soyaldo.simplemessaging.channel.models.Channel;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class ChannelManager {

    private final HashMap<String, Channel> channels = new HashMap<>();

    public void registerManager() {
        reloadManager();
    }

    public void reloadManager() {

    }

    public boolean existChannel(String channelName) {
        return channels.containsKey(channelName);
    }

    public void addChannel(Channel channel) {
        channels.put(channel.getName(), channel);
    }

    public void removeChannel(String channelName) {
        channels.remove(channelName);
    }

    public Channel getChannel(String channelName) {
        return channels.get(channelName);
    }

}