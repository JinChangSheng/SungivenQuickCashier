package cn.pospal.www.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jinchangsheng on 17/7/6.
 *
 */
public class PreferenceUtils {
    public static  String KEY_PROMOTION = "promotion_hide";//显示的套餐

    public static String getPrefString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static String getPrefString(Context context, String key,
                                       final String defaultValue) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    public static void setPrefString(Context context, final String key, final String value) {
        SharedPreferences settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        settings.edit().putString(key, value).commit();
    }

    public static void setPrefString(Context context, final String filename, final String key, final String value) {
        SharedPreferences settings = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        settings.edit().putString(key, value).commit();
    }

    public static void setPrefLong(Context context, final String filename, final String key, final Long value) {
        SharedPreferences settings = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        settings.edit().putLong(key, value).commit();
    }

    public static void setPrefLong(Context context, final String key, final Long value) {
        SharedPreferences settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        settings.edit().putLong(key, value).commit();
    }

    public static boolean getPrefBoolean(Context context, final String key,
                                         final boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    public static boolean hasKey(Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(
                key);
    }

    public static void setPrefBoolean(Context context, final String key,
                                      final boolean value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).commit();
    }

    public static void setPrefInt(Context context, final String key,
                                  final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).commit();
    }

    public static int getPrefInt(Context context, final String key,
                                 final int defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    public static void setPrefFloat(Context context, final String key,
                                    final float value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putFloat(key, value).commit();
    }

    public static float getPrefFloat(Context context, final String key,
                                     final float defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getFloat(key, defaultValue);
    }

    public static void setSettingLong(Context context, final String key,
                                      final long value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putLong(key, value).commit();
    }

    public static long getPrefLong(Context context, final String key,
                                   final long defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    public static long getPrefLong(Context context, final String filename, final String key,
                                   final long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }


    public static void clearPreference(Context context, final SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }

    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, 0);
        sp.edit().remove(key).commit();
    }

    public static void remove(Context context, String filename, String key) {
        SharedPreferences sp = context.getSharedPreferences(filename, 0);
        sp.edit().remove(key).commit();
    }


}