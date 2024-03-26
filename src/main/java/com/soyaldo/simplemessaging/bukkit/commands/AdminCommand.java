package com.soyaldo.simplemessaging.bukkit.commands;

import com.soyaldo.simplemessaging.SimpleMessaging;
import com.soyaldo.simplemessaging.utils.Command;
import com.soyaldo.simplemessaging.utils.Copyright;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminCommand extends Command {

    private final SimpleMessaging plugin;

    public AdminCommand(SimpleMessaging plugin) {
        super("simplemessaging");
        this.plugin = plugin;
    }

    private void onExecute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("simplemessaging.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("noPermission")));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("emptySubCommand")));
            return;
        }

        String subCommand = args[0];

        switch (subCommand.toLowerCase()) {
            case "help": {
                List<String> help = plugin.getMessages().getStringList("help");
                help.forEach(line -> sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line)));
                break;
            }
            case "reload": {
                plugin.onReload();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("reloaded")));
                break;
            }
            case "version": {
                Copyright.sendVersion(plugin, sender);
                break;
            }
            default: {
                String message = plugin.getMessages().getString("invalidSubCommand");
                message = message.replace("%subCommand%", subCommand);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
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