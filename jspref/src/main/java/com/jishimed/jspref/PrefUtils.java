package com.jishimed.jspref;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.jishimed.JSUtils;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class PrefUtils {
    private static final String TAG = "PrefUtils";
    private static SharedPreferences spDefault = null;

    public PrefUtils() {
    }

    public static void init(Context context) {
        Class var1 = PrefUtils.class;
        synchronized (PrefUtils.class) {
            spDefault = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }
    }

    public static SharedPreferences getDefault() {
        return (SharedPreferences) Objects.requireNonNull(spDefault);
    }

    private static <T> T tryParsePreference(SharedPreferences sp, String key, T defValue, Function<String, T> parser) {
        String str = sp.getString(key, (String) null);
        if (!TextUtils.isEmpty(str) && parser != null) {
            str = str.trim();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return parser.apply(str);
                }
                return defValue;
            } catch (NumberFormatException var6) {
                Log.w("PrefUtils", String.format("Failed to parse Preference. key=%s, value=%s", key, str));
                return defValue;
            }
        } else {
            return defValue;
        }
    }

    public static int get(String key, int defValue) {
        try {
            return ((SharedPreferences)Objects.requireNonNull(spDefault)).getInt(key, defValue);
        } catch (ClassCastException var3) {
            return (Integer)tryParsePreference(spDefault, key, defValue, JSUtils::parseInteger);
        }
    }

    public static long get(String key, long defValue) {
        try {
            return ((SharedPreferences)Objects.requireNonNull(spDefault)).getLong(key, defValue);
        } catch (ClassCastException var4) {
            return (Long)tryParsePreference(spDefault, key, defValue, JSUtils::parseLong);
        }
    }

    public static boolean get(String key, boolean defValue) {
        try {
            return ((SharedPreferences)Objects.requireNonNull(spDefault)).getBoolean(key, defValue);
        } catch (ClassCastException var3) {
            return (Boolean)tryParsePreference(spDefault, key, defValue, JSUtils::parseBoolean);
        }
    }

    public static float get(String key, float defValue) {
        try {
            return ((SharedPreferences)Objects.requireNonNull(spDefault)).getFloat(key, defValue);
        } catch (ClassCastException var3) {
            return (Float)tryParsePreference(spDefault, key, defValue, JSUtils::parseFloat);
        }
    }

    public static String get(String key, String defValue) {
        return ((SharedPreferences)Objects.requireNonNull(spDefault)).getString(key, defValue);
    }

    public static Set<String> get(String key, Set<String> defValue) {
        return ((SharedPreferences)Objects.requireNonNull(spDefault)).getStringSet(key, defValue);
    }

    public static int get(SharedPreferences sp, String key, int defValue) {
        try {
            return ((SharedPreferences)Objects.requireNonNull(sp)).getInt(key, defValue);
        } catch (ClassCastException var4) {
            return (Integer)tryParsePreference(sp, key, defValue, JSUtils::parseInteger);
        }
    }

    public static long get(SharedPreferences sp, String key, long defValue) {
        try {
            return ((SharedPreferences)Objects.requireNonNull(sp)).getLong(key, defValue);
        } catch (ClassCastException var5) {
            return (Long)tryParsePreference(sp, key, defValue, JSUtils::parseLong);
        }
    }

    public static boolean get(SharedPreferences sp, String key, boolean defValue) {
        try {
            return ((SharedPreferences)Objects.requireNonNull(sp)).getBoolean(key, defValue);
        } catch (ClassCastException var4) {
            return (Boolean)tryParsePreference(sp, key, defValue, JSUtils::parseBoolean);
        }
    }

    public static float get(SharedPreferences sp, String key, float defValue) {
        try {
            return ((SharedPreferences)Objects.requireNonNull(sp)).getFloat(key, defValue);
        } catch (ClassCastException var4) {
            return (Float)tryParsePreference(sp, key, defValue, JSUtils::parseFloat);
        }
    }

    public static String get(SharedPreferences sp, String key, String defValue) {
        return ((SharedPreferences)Objects.requireNonNull(sp)).getString(key, defValue);
    }

    public static Set<String> get(SharedPreferences sp, String key, Set<String> defValue) {
        return ((SharedPreferences)Objects.requireNonNull(sp)).getStringSet(key, defValue);
    }

    public static void set(String key, boolean value) {
        ((SharedPreferences)Objects.requireNonNull(spDefault)).edit().putBoolean(key, value).apply();
    }

    public static void set(String key, int value) {
        ((SharedPreferences)Objects.requireNonNull(spDefault)).edit().putInt(key, value).apply();
    }

    public static void set(String key, String value) {
        ((SharedPreferences)Objects.requireNonNull(spDefault)).edit().putString(key, value).apply();
    }

    public static void set(String key, long value) {
        ((SharedPreferences)Objects.requireNonNull(spDefault)).edit().putLong(key, value).apply();
    }

    public static void set(String key, float value) {
        ((SharedPreferences)Objects.requireNonNull(spDefault)).edit().putFloat(key, value).apply();
    }
}
