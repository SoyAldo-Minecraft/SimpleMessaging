package com.soyaldo.simplemessaging.runnables;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.soyaldo.simplemessaging.channel.ChannelManager;
import com.soyaldo.simplemessaging.channel.models.Channel;
import com.soyaldo.simplemessaging.message.MessageManager;
import com.soyaldo.simplemessaging.message.enums.MessageType;
import com.soyaldo.simplemessaging.message.types.Message;
import com.soyaldo.simplemessaging.node.Node;
import com.soyaldo.simplemessaging.redis.RedisManager;
import com.soyaldo.simplemessaging.utils.BytesAndStrings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

@RequiredArgsConstructor
@Getter
public class SubscribeRunnable implements Runnable {

    private final Node node;

    @Override
    public void run() {
        JavaPlugin javaPlugin = node.getJavaPlugin();
        RedisManager redisManager = node.getRedisManager();
        try {
            redisManager.getJedisPool().getResource().subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String c, String bytes) {
                    ByteArrayDataInput receivedMessage = ByteStreams.newDataInput(BytesAndStrings.translate(bytes));
                    String channelName = receivedMessage.readUTF();
                    String messageId = receivedMessage.readUTF();
                    String messageType = receivedMessage.readUTF();
                    String serverFrom = receivedMessage.readUTF();
                    String serverTo = receivedMessage.readUTF();
                    String content = receivedMessage.readUTF();
                    // Getting the node name.
                    String nodeName = node.getNodeSettings().getName();
                    // Checking if the received message is from this node.
                    if (nodeName.equals(serverFrom)) {
                        if (node.getNodeSettings().isDebug()) {
                            javaPlugin.getLogger().info("Un mensaje fue recibido pero fue desde este servidor y fue omitido.");
                        }
                        return;
                    }

                    if (nodeName.equals(serverTo) || serverTo.equals("all")) {
                        ChannelManager channelManager = node.getChannelManager();
                        if (channelManager.existChannel(channelName)) {
                            Channel channel = channelManager.getChannel(channelName);
                            if (messageType.equals(MessageType.MESSAGE.toString())) {
                                channel.sendMessage(node, messageId, serverFrom, BytesAndStrings.translate(content));
                            } else {
                                MessageManager messageManager = node.getMessageManager();
                                if (messageManager.existMessage(messageId)) {
                                    Message message = messageManager.getMessage(messageId);
                                    message.response(serverFrom, BytesAndStrings.translate(content));
                                    messageManager.removeMessage(message.getId());
                                }
                            }
                        }
                    } else {
                        if (node.getNodeSettings().isDebug()) {
                            javaPlugin.getLogger().info("El mensaje recibido no es para este servidor y fue omitido.");
                            javaPlugin.getLogger().info("El mensaje es para: " + serverTo);
                            javaPlugin.getLogger().info("Este servidor es: " + nodeName);
                        }
                    }
                }
            }, node.getNodeSettings().getChannel());
        } catch (JedisConnectionException ignore) {
            if (node.getNodeSettings().isDebug()) {
                javaPlugin.getLogger().warning("Subscriber connection closed.");
            }
        }
    }

}