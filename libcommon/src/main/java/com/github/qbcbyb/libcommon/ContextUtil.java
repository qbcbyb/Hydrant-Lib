package com.github.qbcbyb.libcommon;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by qbcby on 2016/7/29.
 */
public class ContextUtil {

    public static final String KEY_DEVICE_ID = "_DEVICE_ID";

    private static void assertContextNotNull(Context context) {
        if (context == null) {
            throw new NullPointerException("context must not be null!");
        }
    }

    public static boolean checkExternalAvaible() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && checkExternalAvaibleGingerbread());
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean checkExternalAvaibleGingerbread() {
        return !Environment.isExternalStorageRemovable();
    }

    public static String getDeviceId(Context context) {
        assertContextNotNull(context);
        context = context.getApplicationContext();
        SharedPreferences preference = SharedPreferencesManager.getPreference(context);
        if (preference.contains(KEY_DEVICE_ID)) {
            return preference.getString(KEY_DEVICE_ID, null);
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (StringUtil.isEmpty(deviceId)) {
            deviceId = UUID.randomUUID().toString();
        }
        SharedPreferences.Editor edit = preference.edit();
        edit.putString(KEY_DEVICE_ID, deviceId);
        edit.apply();
        return deviceId;
    }

    /**
     * 检查网络是否可用
     *
     * @return 网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        assertContextNotNull(context);
        context = context.getApplicationContext();
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
//        return (info != null && info.isAvailable());
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            NetworkInfo info[] = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("WrongConstant")
    public static boolean hadAppInstalled(Context context, String packageName) {
        assertContextNotNull(context);
        context = context.getApplicationContext();
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void startInstalledApp(Context context, String packageName) {
        assertContextNotNull(context);
        context = context.getApplicationContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    public static int getVersionCode(Context context) {
        assertContextNotNull(context);
        context = context.getApplicationContext();
        int versionCode = 1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getVersionName(Context context) {
        assertContextNotNull(context);
        context = context.getApplicationContext();
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
