package com.soyaldo.simplemessaging.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class Messenger {

    private final JavaPlugin plugin;
    private final FileConfiguration messages;

    public Messenger(JavaPlugin plugin, FileConfiguration messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    public void send(String name, String path) {
        send(name, path, new String[][]{});
    }

    public void send(String name, String path, String[][] replacements) {
        if (name.equals("console")) {
            CommandSender console = plugin.getServer().getConsoleSender();
            send(console, path, replacements);
        } else {
            Player player = plugin.getServer().getPlayerExact(name);
            if (player != null) send(player, path, replacements);
        }
    }

    public void send(CommandSender commandSender, String path) {
        send(commandSender, path, new String[][]{});
    }

    public void send(CommandSender commandSender, String path, String[][] replacements) {
        Object message = messages.get(path, "&8[&4&l!&8] &cEl mensaje &f&n" + path + "&r &cno existe.");
        String prefix = messages.getString("prefix", "");

        replacements = Arrays.copyOf(replacements, replacements.length + 1);
        replacements[replacements.length - 1] = new String[]{"%prefix%", prefix};

        sendRaw(commandSender, message, replacements);
    }

    public void sendRaw(CommandSender commandSender, Object message) {
        sendRaw(commandSender, message, new String[][]{});
    }

    public void sendRaw(CommandSender commandSender, Object message, String[][] replacements) {
        // If the message is null.
        if (message == null) {
            // Return the method.
            return;
        }
        // If the message is a String o List
        if (message.getClass().getSimpleName().equals("String")) {
            sendRaw(commandSender, (String) message, replacements);
        } else {
            sendRaw(commandSender, (List<String>) message, replacements);
        }
    }

    public static void sendRaw(CommandSender commandSender, List<String> messages) {
        // Use the sendRaw method with the empty replacements.
        sendRaw(commandSender, messages, new String[][]{});
    }

    public static void sendRaw(CommandSender commandSender, List<String> messages, String[][] replacements) {
        // If the message is null.
        if (messages == null) {
            // Return the method.
            return;
        }
        // Use the sendRaw in all list.
        for (String message : messages) {
            sendRaw(commandSender, message, replacements);
        }
    }

    public static void sendRaw(CommandSender commandSender, String message) {
        // Use the sendRaw method with the empty replacements.
        sendRaw(commandSender, message, new String[][]{});
    }

    public static void sendRaw(CommandSender commandSender, String message, String[][] replacements) {
        // If the message is null.
        if (message == null) {
            // Return the method.
            return;
        }

        // Replace all replacements.
        for (String[] replacement : replacements) {
            message = message.replace(replacement[0], replacement[1]);
        }

        // PlaceholderAPI
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }
        }

        // Send the message.
        commandSender.spigot().sendMessage(MineDown.translate(message));
    }

}