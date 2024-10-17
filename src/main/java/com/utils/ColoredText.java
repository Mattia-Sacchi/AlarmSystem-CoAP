package com.utils;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ColoredText {

    private static final String TEXT_RESET = "\u001B[0m";

    private static final Map<String, String> colorsMap = Stream.of(
            new AbstractMap.SimpleEntry<>("BLACK", "\u001B[30m"),
            new AbstractMap.SimpleEntry<>("RED", "\u001B[31m"),
            new AbstractMap.SimpleEntry<>("GREEN", "\u001B[32m"),
            new AbstractMap.SimpleEntry<>("YELLOW", "\u001B[33m"),
            new AbstractMap.SimpleEntry<>("BLUE", "\u001B[34m"),
            new AbstractMap.SimpleEntry<>("PURPLE", "\u001B[35m"),
            new AbstractMap.SimpleEntry<>("CYAN", "\u001B[36m"),
            new AbstractMap.SimpleEntry<>("WHITE", "\u001B[37m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BLACK", "\u001B[90m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_RED", "\u001B[91m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_GREEN", "\u001B[92m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_YELLOW", "\u001B[93m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BLUE", "\u001B[94m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_PURPLE", "\u001B[95m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_CYAN", "\u001B[96m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_WHITE", "\u001B[97m"),
            new AbstractMap.SimpleEntry<>("BG_BLACK", "\u001B[40m"),
            new AbstractMap.SimpleEntry<>("BG_RED", "\u001B[41m"),
            new AbstractMap.SimpleEntry<>("BG_GREEN", "\u001B[42m"),
            new AbstractMap.SimpleEntry<>("BG_YELLOW", "\u001B[43m"),
            new AbstractMap.SimpleEntry<>("BG_BLUE", "\u001B[44m"),
            new AbstractMap.SimpleEntry<>("BG_PURPLE", "\u001B[45m"),
            new AbstractMap.SimpleEntry<>("BG_CYAN", "\u001B[46m"),
            new AbstractMap.SimpleEntry<>("BG_WHITE", "\u001B[47m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BG_BLACK", "\u001B[100m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BG_RED", "\u001B[101m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BG_GREEN", "\u001B[102m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BG_YELLOW", "\u001B[103m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BG_BLUE", "\u001B[104m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BG_PURPLE", "\u001B[105m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BG_CYAN", "\u001B[106m"),
            new AbstractMap.SimpleEntry<>("BRIGHT_BG_WHITE", "\u001B[107m"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public static String buildColoredString(String textColor, String str) {
        String toReturn = "";
        textColor = textColor.toUpperCase();
        if (textColor != null && colorsMap.containsKey(textColor))
            toReturn += colorsMap.get(textColor);
        return toReturn + str + TEXT_RESET;
    }

    public static String buildColoredString(String bgColor, String textColor, String str) {
        String toReturn = "";
        bgColor = bgColor.toUpperCase();
        if (bgColor != null && colorsMap.containsKey(bgColor))
            toReturn += colorsMap.get(bgColor);
        return buildColoredString(textColor, toReturn.concat(str));
    }

}
