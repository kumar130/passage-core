package com.nm.logging.util;

/**
 * Created by kre5335 on 6/6/2017.
 */
public final class SafeStringUtils {


    public static String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return removeNonPrintable(value);
    }

    public static String removeNonPrintable(String value) {
        if (value == null) {
            return null;
        }
        return value.chars().filter(c -> ignore(c)).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    public static String replaceNonPrintable(String value) {
        return replaceNonPrintable(value, ' ');
    }

    private static String replaceNonPrintable(String value, char replacement) {
        if (value == null) {
            return null;
        }
        return value.chars().map(c -> ignore(c) ? c : replacement).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    private static boolean ignore(int c) {
        return (c >= 0x20 && c < 0x7f);
    }
}
