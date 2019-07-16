package com.jishimed;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.support.annotation.NonNull;
import android.util.Log;
import java.util.Locale;

public class JSUtils {
    private static final Locale defLocale = Locale.getDefault();

    public JSUtils() {
    }

    public static String format(String format, Object... args) {
        return String.format(defLocale, format, args);
    }

    public static void v(String tag, String format, Object... args) {
    }

    public static void e(String tag, String format, Object... args) {
        Log.e(tag, String.format(defLocale, format, args));
    }

    public static void w(String tag, String format, Object... args) {
        Log.w(tag, String.format(defLocale, format, args));
    }

    public static void i(String tag, String format, Object... args) {
        Log.i(tag, String.format(defLocale, format, args));
    }

    public static void d(String tag, String format, Object... args) {
    }

    public static long parseLong(@NonNull String s) {
        if (s.startsWith("0x")) {
            return Long.parseLong(s.substring(2), 16);
        } else {
            return s.startsWith("#") ? Long.parseLong(s.substring(1), 16) : Long.parseLong(s);
        }
    }

    public static int parseInteger(@NonNull String s) {
        long val = parseLong(s);
        if (val >= -2147483648L && val <= 4294967295L) {
            return (int)val;
        } else {
            throw new NumberFormatException();
        }
    }

    public static float parseFloat(@NonNull String s) {
        return Float.parseFloat(s);
    }

    public static double parseDouble(@NonNull String s) {
        return Double.parseDouble(s);
    }

    public static boolean parseBoolean(@NonNull String s) {
        String var1 = s.toLowerCase();
        byte var2 = -1;
        switch(var1.hashCode()) {
            case 3569038:
                if (var1.equals("true")) {
                    var2 = 0;
                }
                break;
            case 97196323:
                if (var1.equals("false")) {
                    var2 = 1;
                }
        }

        switch(var2) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                throw new NumberFormatException();
        }
    }
}
