package com.soyaldo.simplemessaging.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Copyright {

    public static void sendVersion(JavaPlugin javaPlugin, CommandSender commandSender) {
        sendVersionStatus(javaPlugin, commandSender, "");
    }

    public static void sendVersionStatusFromConsole(JavaPlugin javaPlugin, String status) {
        sendVersionStatus(javaPlugin, javaPlugin.getServer().getConsoleSender(), status);
    }

    public static void sendVersionStatus(JavaPlugin plugin, CommandSender commandSender, String status) {
        List<String> version = new ArrayList<>();
        version.add("&4»");
        version.add("&4» &c" + plugin.getName() + " " + status);
        version.add("&4»");
        version.add("&4» &cVersion: &f" + plugin.getDescription().getVersion());
        version.add("&4» &cAuthor: &fSoyAldo");
        version.add("&4» &cWebsite: &fhttps://soyaldo.com/plugins/" + plugin.getName().toLowerCase());
        version.add("&4»");
        version.add("&4» &cI love the bread!");
        version.add("&4»");
        version.forEach(line -> commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', line)));
    }

}