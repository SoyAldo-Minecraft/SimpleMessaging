package com.soyaldo.simplemessaging.commands;

import com.soyaldo.simplemessaging.SimpleMessaging;
import com.soyaldo.simplemessaging.utils.Command;
import com.soyaldo.simplemessaging.utils.Copyright;
import com.soyaldo.simplemessaging.utils.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class AdminCommand extends Command {

    private final SimpleMessaging plugin;

    public AdminCommand(SimpleMessaging plugin) {
        super("simplemessaging");
        this.plugin = plugin;
    }

    private void onExecute(CommandSender sender, String[] args) {
        Messenger messenger = plugin.getMessenger();

        if (!sender.hasPermission("simplemessaging.admin")) {
            messenger.send(sender, "noPermission");
            return;
        }

        if (args.length == 0) {
            messenger.send(sender, "emptySubCommand");
            return;
        }

        String subCommand = args[0];

        switch (subCommand.toLowerCase()) {
            case "help": {
                messenger.send(sender, "help");
                break;
            }
            case "reload": {
                plugin.onReload();
                messenger.send(sender, "reloaded");
                break;
            }
            case "version": {
                Copyright.sendVersion(plugin, sender);
                break;
            }
            default: {
                messenger.send(sender, "invalidSubCommand", new String[][]{
                        {"%subCommand%", subCommand}
                });
            }
        }
    }

    private String onTabComplete(CommandSender requester, int position) {
        if (!requester.hasPermission("simplemessaging.admin")) {
            return "";
        }
        if (position == 1) {
            return "reload,version";
        }
        return "";
    }

    @Override
    public void onPlayerExecute(Player sender, String[] args) {
        onExecute(sender, args);
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String[] args) {
        onExecute(sender, args);
    }

    @Override
    public String onPlayerTabComplete(Player requester, int position, String[] previousArguments) {
        return onTabComplete(requester, position);
    }

    @Override
    public String onConsoleTabComplete(ConsoleCommandSender requester, int position, String[] previousArguments) {
        return onTabComplete(requester, position);
    }

}