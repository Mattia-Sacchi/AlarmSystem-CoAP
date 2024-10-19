package com.utils;

public class Log {

    private static String OpeningText = "\n\t";
    private static String ClosingText = "\n";
    private static String EndlineText = "\n\t";

    public static void printLine(String bgColor, String color, String text) {
        printLine(ColoredText.buildColoredString(bgColor, color, text));
    }

    private static void printLine(String color, String text) {
        printLine(ColoredText.buildColoredString(color, text));
    }

    private static void printLine(String text) {
        System.out.println(text);
    }

    private static String buildComposedString(String title, String description) {
        return title + EndlineText + description;
    }

    public static void error(String title, String description) {
        error(buildComposedString(title, description));
    }

    public static void success(String title, String description) {
        success(buildComposedString(title, description));
    }

    public static void failure(String title, String description) {
        failure(buildComposedString(title, description));
    }

    public static void debug(String title, String description) {
        debug(buildComposedString(title, description));
    }

    public static boolean operationResult(boolean result, String title, String description) {
        return operationResult(result, buildComposedString(title, description));
    }

    public static void error(String title, String... text) {
        error(title);
        for (String t : text)
            printLine("Red", t);
    }

    public static void error(String text) {
        String outText = OpeningText + "Error: " + text + ClosingText;
        printLine("Red", outText);
    }

    public static void success(String title, String... text) {
        success(title);
        for (String t : text)
            printLine("Green", t);
    }

    public static void success(String text) {
        String outText = OpeningText + "Success: " + text + ClosingText;
        printLine("Green", outText);
    }

    public static void failure(String title, String... text) {
        failure(title);
        for (String t : text)
            printLine("Yellow", t);
    }

    public static void failure(String text) {
        String outText = OpeningText + "Failure: " + text + ClosingText;
        printLine("Yellow", outText);
    }

    public static boolean operationResult(boolean result, String text) {
        if (result)
            success(text);
        else
            failure(text);
        return result;
    }

    public static void debug(String title, String... text) {
        debug(title);
        for (String t : text)
            printLine("Yellow", t);
    }

    public static void debug(String text) {
        String outText = OpeningText + "Debug: " + text + ClosingText;
        printLine("Purple", outText);
    }

}
