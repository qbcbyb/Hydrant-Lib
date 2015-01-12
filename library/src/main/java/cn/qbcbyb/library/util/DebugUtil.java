package cn.qbcbyb.library.util;

import android.util.Log;

public abstract class DebugUtil {

    private static boolean Debug = false;

    public static boolean isDebug() {
        return Debug;
    }

    public static void setDebug(boolean debug) {
        DebugUtil.Debug = debug;
    }

    public static void e(String tag, String log) {
        if (Debug)
            Log.e(tag, log);
    }

    public static void e(String tag, String log, Throwable e) {
        if (Debug)
            Log.e(tag, log, e);
    }

    public static void d(String tag, String log) {
        if (Debug)
            Log.d(tag, log);
    }

    public static void d(String tag, String log, Throwable e) {
        if (Debug)
            Log.d(tag, log, e);
    }

    public static void i(String tag, String log) {
        if (Debug)
            Log.i(tag, log);
    }

    public static void i(String tag, String log, Throwable e) {
        if (Debug)
            Log.i(tag, log, e);
    }
}//end of class
