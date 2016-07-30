package com.github.qbcbyb.libcommon;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

public class SharedPreferencesManager {

    public static final String CONFIG_FILE = "CONFIG_FILE";

    private static WeakReference<SharedPreferences> weakReference;

    static SharedPreferences getPreference(Context context) {
        SharedPreferences preferences;
        if (weakReference == null || (preferences = weakReference.get()) == null) {
            preferences = context.getApplicationContext()
                    .getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);
            weakReference = new WeakReference<>(preferences);
        }
        return preferences;
    }

}
