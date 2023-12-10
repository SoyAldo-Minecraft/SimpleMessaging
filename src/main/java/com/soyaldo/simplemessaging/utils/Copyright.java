package com.soyaldo.simplemessaging.utils;

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
        List<String> versionStatus = new ArrayList<>();
        versionStatus.add("&4»");
        versionStatus.add("&4» &c" + plugin.getName() + " " + status);
        versionStatus.add("&4»");
        versionStatus.add("&4» &cVersion: &f" + plugin.getDescription().getVersion());
        versionStatus.add("&4» &cAuthor: &fSoyAldo");
        versionStatus.add("&4» &cWebsite: &fhttps://soyaldo.com/plugins/" + plugin.getName().toLowerCase());
        versionStatus.add("&4»");
        versionStatus.add("&4» &cCreated for &fmc.natublock.net");
        versionStatus.add("&4»");
        Messenger.sendRaw(commandSender, versionStatus);
    }

}