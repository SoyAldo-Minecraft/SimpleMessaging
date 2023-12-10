package com.soyaldo.simplemessaging;

import com.soyaldo.simplemessaging.commands.AdminCommand;
import com.soyaldo.simplemessaging.channel.ChannelManager;
import com.soyaldo.simplemessaging.message.MessageManager;
import com.soyaldo.simplemessaging.redis.RedisManager;
import com.soyaldo.simplemessaging.utils.Copyright;
import com.soyaldo.simplemessaging.utils.Messenger;
import com.soyaldo.simplemessaging.utils.Yaml;
import com.soyaldo.simplemessaging.utils.debugger.Debuggable;
import com.soyaldo.simplemessaging.utils.debugger.Debugger;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class SimpleMessaging extends JavaPlugin implements Debuggable {

    // API
    @Getter
    public static API api;
    // Files
    @Getter
    private final Yaml settings = new Yaml(this, "settings", getResource("settings.yml"));
    @Getter
    private final Yaml messages = new Yaml(this, "messages", getResource("messages.yml"));
    // Debug
    private boolean debug = false;
    @Getter
    private final Debugger debugger = new Debugger(this);
    // Messenger
    @Getter
    private Messenger messenger;
    // Information
    @Getter
    private String serverName;
    @Getter
    private int responseLimit;
    // Managers
    @Getter
    private final RedisManager redisManager = new RedisManager(this);
    @Getter
    private final ChannelManager channelManager = new ChannelManager();
    @Getter
    private final MessageManager messageManager = new MessageManager();

    @Override
    public void onEnable() {
        // API
        api = new API(this);
        // Files
        settings.register();
        messages.register();
        // Debug
        debug = settings.getFileConfiguration().getBoolean("debug", false);
        // Messenger
        messenger = new Messenger(this, messages.getFileConfiguration());
        // Information
        serverName = settings.getFileConfiguration().getString("serverName", "");
        if (serverName.isEmpty()) serverName = UUID.randomUUID().toString().split("-")[0];
        responseLimit = settings.getFileConfiguration().getInt("message.responseTimeLimit", 3);
        // Managers
        redisManager.registerManager();
        channelManager.registerManager();
        messageManager.registerManager();
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
        // Debug
        debug = settings.getFileConfiguration().getBoolean("debug", false);
        // Messenger
        messenger = new Messenger(this, messages.getFileConfiguration());
        // Information
        serverName = settings.getFileConfiguration().getString("serverName", "");
        if (serverName.isEmpty()) serverName = UUID.randomUUID().toString().split("-")[0];
        responseLimit = settings.getFileConfiguration().getInt("message.responseTimeLimit", 3);
        // Managers
        redisManager.reloadManager();
        channelManager.reloadManager();
        messageManager.reloadManager();
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public CommandSender getReceptor() {
        return getServer().getConsoleSender();
    }

}