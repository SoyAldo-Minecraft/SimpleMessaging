package com.soyaldo.simplemessaging.utils.debugger;

import org.bukkit.command.CommandSender;

public interface Debuggable {

    boolean isDebug();
    String getName();
    CommandSender getReceptor();

}