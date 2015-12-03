package com.sami_ofer.samioferproject;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by alex on 03/12/15.
 */
public class Utils {
    public static void writePref(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(key, value).apply();
    }

    public static String readPref(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, "");
    }
}
