package cn.qbcbyb.library.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cn.qbcbyb.library.BuildConfig;
import cn.qbcbyb.library.R;
import cn.qbcbyb.library.util.DebugUtil;
import cn.qbcbyb.library.util.SharedPreferencesManager;
import cn.qbcbyb.library.util.StringUtils;

public class BaseApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static BaseApplication instance;
    private static SimpleDateFormat sdf;
    private String logFileName = "error.log";
    private String imageFoldName = "image";
    private String deviceId;

    public static DisplayImageOptions.Builder getDefaultDisplayImageOptionsBuilder() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(BaseApplication.getInstance(BaseApplication.class).getDefaultLoadingRes())
                .showImageForEmptyUri(BaseApplication.getInstance(BaseApplication.class).getDefaultLoadingRes())
                .showImageOnFail(BaseApplication.getInstance(BaseApplication.class).getDefaultLoadingRes())
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565);
    }

    public static <T extends BaseApplication> T getInstance(Class<T> tClass) {
        return tClass.cast(instance);
    }

    public static String getTime() {
        if (sdf == null) {
            sdf = new SimpleDateFormat("MM-dd HH:mm");
        }
        return sdf.format(new Date());
    }

    protected void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public void setImageFoldName(String imageFoldName) {
        this.imageFoldName = imageFoldName;
    }

    public int getDefaultLoadingRes() {
        return R.drawable.default_loading;
    }

    public String getDeviceId() {
        if (StringUtils.isEmpty(deviceId)) {
            TelephonyManager tm = (TelephonyManager) this
                    .getSystemService(TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
            if (StringUtils.isEmpty(deviceId)) {
                deviceId = SharedPreferencesManager.getValue("_DEVICE_ID", UUID.randomUUID().toString());
            }
        }
        return deviceId;
    }

    public boolean isDebugEable() {
        return BuildConfig.DEBUG;
    }

    /**
     * 可在此初始化APP_FOLD
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);
        instance = this;
        DebugUtil.setDebug(isDebugEable());
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(getApplicationContext());
        if (DebugUtil.isDebug()) {
            builder.writeDebugLogs();
        }
        setImageLoaderConfiguration(builder);
        ImageLoaderConfiguration configuration = builder.build();
        ImageLoader.getInstance().init(configuration);
    }

    protected void setImageLoaderConfiguration(ImageLoaderConfiguration.Builder builder) {
        DisplayImageOptions displayImageOptions = getDefaultDisplayImageOptionsBuilder().build();
        builder.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 8)
                .diskCache(new UnlimitedDiskCache(getAppImagePath()))
//                .diskCacheSize(100 * 1024 * 1024)
//                .diskCacheFileCount(300)
                .defaultDisplayImageOptions(displayImageOptions);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        logError(ex);
        exitApplication();
    }

    /**
     * 应用出错退出会触发该方法
     */
    protected void exitApplication() {
    }

    public File getAppPath() {
        synchronized (this) {
            File cachePath;
            if (checkExternalAvaible()) {
                cachePath = this.getExternalCacheDir();
            } else {
                cachePath = this.getCacheDir();
            }
            if (!cachePath.exists()) {
                cachePath.mkdirs();
            }
            return cachePath;
        }
    }

    public boolean checkExternalAvaible() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && checkExternalAvaibleGingerbread());
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public boolean checkExternalAvaibleGingerbread() {
        return !Environment.isExternalStorageRemovable();
    }

    public File getAppImagePath() {
        File imageFold = getAppPathFile(imageFoldName);
        if (!imageFold.exists()) {
            imageFold.mkdirs();
        }
        return imageFold;
    }

    public File getAppPathFile(String fileName) {
        return new File(getAppPath(), fileName);
    }

    public void logError(Throwable ex) {
        ex.printStackTrace();
        StringBuffer buffer = new StringBuffer();
        buffer.append("\r\n\r\n");
        buffer.append(new Date().toString());
        buffer.append("\r\n");
        buffer.append(ex.toString());
        buffer.append("\r\n");
        for (StackTraceElement traceElement : ex.getStackTrace()) {
            buffer.append(traceElement.toString());
            buffer.append("\r\n");
        }
        log(buffer.toString());
    }

    public File log(String buffer) {
        File errLog = this.getAppPathFile(logFileName);
        try {
            if (errLog.exists() || errLog.createNewFile()) {
                OutputStream os = new FileOutputStream(errLog, true);
                os.write(buffer.getBytes("UTF-8"));
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return errLog;
    }

    /**
     * 检查网络是否可用
     *
     * @return 网络是否可用
     */
    public boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
//        return (info != null && info.isAvailable());
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public boolean hadAppInstalled(String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void startInstalledApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    public int getVersionCode() {
        int versionCode = 1;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public String getVersionName() {
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
