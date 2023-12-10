package com.soyaldo.simplemessaging.utils.debugger;

import com.soyaldo.simplemessaging.utils.Messenger;

public class Debugger {

    private final Debuggable debuggable;

    public Debugger(Debuggable debuggable) {
        this.debuggable = debuggable;
    }

    public void normal(String text) {
        normal(text, new String[][]{});
    }

    public void normal(String text, String[][] replacements) {
        String normalPrefix = "&f";
        raw(normalPrefix + text, replacements);
    }

    public void info(String text) {
        info(text, new String[][]{});
    }

    public void info(String text, String[][] replacements) {
        String infoPrefix = "&b";
        raw(infoPrefix + text, replacements);
    }

    public void warning(String message) {
        warning(message, new String[][]{});
    }

    public void warning(String text, String[][] replacements) {
        String warningPrefix = "&6&lWARNING &8| &e";
        raw(warningPrefix + text, replacements);
    }

    public void error(String text) {
        error(text, new String[][]{});
    }

    public void error(String text, String[][] replacements) {
        String errorPrefix = "&4&lERROR &8| &c";
        raw(errorPrefix + text, replacements);
    }

    public void raw(String text) {
        raw(text, new String[][]{});
    }

    public void raw(String text, String[][] replacements) {
        // If the debug mode is disabled.
        if (!debuggable.isDebug()) return;
        // Apply all replacements to the message.
        for (String[] replacement : replacements) text = text.replace(replacement[0], replacement[1]);
        // Prefix
        text = "[" + debuggable.getName() + "] " + text;
        // Send the message to console.
        Messenger.sendRaw(debuggable.getReceptor(), text);
    }

}