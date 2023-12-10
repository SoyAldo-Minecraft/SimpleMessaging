package com.soyaldo.simplemessaging.utils;

import java.util.Arrays;

public class BytesAndStrings {

    public static byte[] translate(String text) {
        String[] tokens = text.replaceAll("[\\[\\]]", "").split(", ");
        byte[] bytes = new byte[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            bytes[i] = Byte.parseByte(tokens[i]);
        }
        return bytes;
    }

    public static String translate(byte[] bytes) {
        return Arrays.toString(bytes);
    }

}