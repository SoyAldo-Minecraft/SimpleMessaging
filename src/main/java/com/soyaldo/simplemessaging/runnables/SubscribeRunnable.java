package com.soyaldo.simplemessaging.runnables;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.soyaldo.simplemessaging.SimpleMessaging;
import com.soyaldo.simplemessaging.channel.ChannelManager;
import com.soyaldo.simplemessaging.channel.models.Channel;
import com.soyaldo.simplemessaging.message.MessageManager;
import com.soyaldo.simplemessaging.message.enums.MessageType;
import com.soyaldo.simplemessaging.message.types.Message;
import com.soyaldo.simplemessaging.redis.RedisManager;
import com.soyaldo.simplemessaging.utils.BytesAndStrings;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class SubscribeRunnable implements Runnable {

    private final SimpleMessaging plugin;

    public SubscribeRunnable(SimpleMessaging plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        RedisManager redisManager = plugin.getRedisManager();
        try {
            redisManager.getSubscriber().getJedis().subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String c, String bytes) {
                    ByteArrayDataInput receivedMessage = ByteStreams.newDataInput(BytesAndStrings.translate(bytes));
                    String channelName = receivedMessage.readUTF();
                    String messageId = receivedMessage.readUTF();
                    String messageType = receivedMessage.readUTF();
                    String serverFrom = receivedMessage.readUTF();
                    String serverTo = receivedMessage.readUTF();
                    String content = receivedMessage.readUTF();

                    if (plugin.getServerName().equals(serverFrom)) {
                        plugin.getDebugger().info("Un mensaje fue recibido pero fue desde este servidor y fue omitido.");
                        return;
                    }

                    if (plugin.getServerName().equals(serverTo) || serverTo.equals("all")) {
                        ChannelManager channelManager = plugin.getChannelManager();
                        if (channelManager.existChannel(channelName)) {
                            Channel channel = channelManager.getChannel(channelName);
                            if (messageType.equals(MessageType.MESSAGE.toString())) {
                                channel.sendMessage(plugin, messageId, serverFrom, BytesAndStrings.translate(content));
                            } else {
                                MessageManager messageManager = plugin.getMessageManager();
                                if (messageManager.existMessage(messageId)) {
                                    Message message = messageManager.getMessage(messageId);
                                    message.response(serverFrom, BytesAndStrings.translate(content));
                                    messageManager.removeMessage(message.getId());
                                }
                            }
                        }
                    } else {
                        plugin.getDebugger().info("El mensaje recibido no es para este servidor y fue omitido.");
                        plugin.getDebugger().info("El mensaje es para: " + serverTo);
                        plugin.getDebugger().info("Este servidor es: " + plugin.getServerName());
                    }
                }
            }, "simpleMessaging");
        } catch (JedisConnectionException ignore) {
            redisManager.getSubscriber().connect();
        }
    }

}