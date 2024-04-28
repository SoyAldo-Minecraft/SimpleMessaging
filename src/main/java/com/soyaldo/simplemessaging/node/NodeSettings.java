package com.soyaldo.simplemessaging.node;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class NodeSettings {

    // Debug
    private boolean debug = false;
    // Node name.
    private final String name;
    private final String channel;
    // Message response limite.
    private int messageResponseLimit = 5;
    // Redis settings.
    private String redisHost = "localhost";
    private int redisPort = 6379;
    private String redisPassword = "";
    private int redisMaxConnections = 10;
    private int redisMaxIdleConnections = 5;

    public static NodeSettings loadFromConfiguration(FileConfiguration configuration) {
        // Getting debug
        boolean debug = configuration.getBoolean("debug", false);
        // Getting the name.
        String name = configuration.getString("name", "");
        // If the name is empty.
        if (name.isEmpty()) {
            // Generate a random name.
            name = UUID.randomUUID().toString().split("-")[0];
        }
        // Getting the channel.
        String channel = configuration.getString("channel", "");
        // If the name is empty.
        if (name.isEmpty()) {
            // Generate a random name.
            channel = "simpleMessaging";
        }
        // Creating the node settings.
        NodeSettings nodeSettings = new NodeSettings(name, channel);
        // Set the settings.
        nodeSettings.setDebug(debug);
        nodeSettings.setMessageResponseLimit(configuration.getInt("messageResponseLimit", 5));
        nodeSettings.setRedisHost(configuration.getString("redis.host", "localhost"));
        nodeSettings.setRedisPort(configuration.getInt("redis.port", 6379));
        nodeSettings.setRedisPassword(configuration.getString("redis.password", ""));
        nodeSettings.setRedisMaxConnections(configuration.getInt("redis.maxConnections", 10));
        nodeSettings.setRedisMaxIdleConnections(configuration.getInt("redis.maxIdleConnections", 5));
        // Returning the node settings.
        return nodeSettings;
    }

}