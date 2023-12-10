package com.soyaldo.simplemessaging.utils;

import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Arrays;

public class MineDown {

    public static String legacyTranslate(String text) {
        StringBuilder finalText = new StringBuilder();
        Arrays.stream(translate(text)).forEach(baseComponent -> finalText.append(baseComponent.toLegacyText()));
        return finalText.toString();
    }

    public static BaseComponent[] translate(String text) {
        return de.themoep.minedown.MineDown.parse(text);
    }

}