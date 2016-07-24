package cn.qbcbyb.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

public class SharedPreferencesManager {

    private static SharedPreferences preference;

    private static Object preferenceLock = new Object();
    private static Context context;

    /**
     * @param context
     */
    public static void initContext(Context context) {
        SharedPreferencesManager.context = context;
    }

    private static SharedPreferences getPreference() {
        if (preference == null) {
            synchronized (preferenceLock) {
                if (preference == null) {
                    if (context == null) {
                        throw new NullPointerException("context must not be null,call initContext first!");
                    }
                    preference = context.getApplicationContext()
                            .getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);
                }
            }
        }
        return preference;
    }

    public static final String CONFIG_FILE = "CONFIG_FILE";

    public static boolean contains(String key) {
        return getPreference().contains(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(String key, T defaultValue) {
        T result = null;
        Map<String, ?> configMap = getPreference().getAll();
        if (configMap.containsKey(key)) {
            Object obj = configMap.get(key);
            if (defaultValue != null && obj.getClass() != defaultValue.getClass()) {
                Editor editor = getPreference().edit();
                editor.remove(key);
                editor.commit();
            } else {
                result = (T) obj;
            }
        }
        if (result == null && defaultValue != null) {
            Editor editor = getPreference().edit();
            if (defaultValue instanceof String)
                editor.putString(key, (String) defaultValue);
            else if (defaultValue instanceof Integer)
                editor.putInt(key, (Integer) defaultValue);
            else if (defaultValue instanceof Boolean)
                editor.putBoolean(key, (Boolean) defaultValue);
            else if (defaultValue instanceof Float)
                editor.putFloat(key, (Float) defaultValue);
            else if (defaultValue instanceof Long)
                editor.putLong(key, (Long) defaultValue);
            editor.commit();
            result = defaultValue;
        }
        return result;
    }

    public static <T> void saveValue(String key, T value) {
        Editor editor = getPreference().edit();

        if (value instanceof String)
            editor.putString(key, (String) value);
        else if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else if (value instanceof Boolean)
            editor.putBoolean(key, (Boolean) value);
        else if (value instanceof Float)
            editor.putFloat(key, (Float) value);
        else if (value instanceof Long)
            editor.putLong(key, (Long) value);

        editor.commit();
    }

}
