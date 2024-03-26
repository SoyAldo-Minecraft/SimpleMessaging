package com.soyaldo.simplemessaging;

import com.soyaldo.simplemessaging.bukkit.commands.AdminCommand;
import com.soyaldo.simplemessaging.channel.models.Channel;
import com.soyaldo.simplemessaging.node.Node;
import com.soyaldo.simplemessaging.node.NodeSettings;
import com.soyaldo.simplemessaging.utils.Copyright;
import com.soyaldo.simplemessaging.utils.Yaml;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class SimpleMessaging extends JavaPlugin {

    // Files
    private final Yaml settings = new Yaml(this, "settings", getResource("settings.yml"));
    private final Yaml messages = new Yaml(this, "messages", getResource("messages.yml"));
    // Node
    private Node node;
    // API
    @Getter
    public static API api;

    @Override
    public void onEnable() {
        // Files
        settings.register();
        messages.register();
        // Loading the new node settings.
        NodeSettings nodeSettings = NodeSettings.loadFromConfiguration(settings.getFileConfiguration());
        // Creating a new node instance.
        node = new Node(this, nodeSettings);
        node.register();
        // API
        api = new API(node);
        // Commands
        new AdminCommand(this).registerCommand(this);
        // Copyright
        Copyright.sendVersionStatusFromConsole(this, "&aEnabled");
    }

    @Override
    public void onDisable() {
        // Copyright
        Copyright.sendVersionStatusFromConsole(this, "&cDisabled");
    }

    public void onReload() {
        // Files
        settings.reload();
        messages.reload();
        // Loading the new node settings.
        NodeSettings nodeSettings = NodeSettings.loadFromConfiguration(settings.getFileConfiguration());
        // Creating temporal channels.
        List<Channel> channels = new ArrayList<>(node.getChannelManager().getChannels().values());
        // Creating a new node instance.
        node = new Node(this, nodeSettings);
        node.register();
        // Adding temporal channels again.
        channels.forEach(channel -> node.getChannelManager().addChannel(channel));
        // API
        api = new API(node);
    }

}