package cn.qbcbyb.library.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import cn.qbcbyb.library.R;
import cn.qbcbyb.library.app.BaseApplication;
import cn.qbcbyb.library.fragment.BaseFragment;
import cn.qbcbyb.library.fragment.IController;
import cn.qbcbyb.library.util.Msg;

public abstract class BaseActivity extends ActionBarActivity implements IController {
    protected final String TAG = this.getClass().getSimpleName();

    public static final DisplayImageOptions DISPLAY_IMAGE_OPTIONS_OF_SOURCE = BaseApplication.getDefaultDisplayImageOptionsBuilder()
            .imageScaleType(ImageScaleType.NONE)
            .build();
    public static final DisplayImageOptions DISPLAY_IMAGE_OPTIONS_OF_LIST = BaseApplication.getDefaultDisplayImageOptionsBuilder()
            .resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .build();

    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_CODE2 = 2;
    public static final int REQUEST_CODE3 = 3;
    public static final int REQUEST_CODE4 = 4;
    protected final BaseActivity context = this;

    private boolean finishConfirm = false;
    private long exitTime = Integer.MIN_VALUE;

    protected static List<Activity> activedActivity = new ArrayList<Activity>();

    public boolean isFinishConfirm() {
        return finishConfirm;
    }

    public void setFinishConfirm(boolean finishConfirm) {
        this.finishConfirm = finishConfirm;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (finishConfirm) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!this.getSupportFragmentManager().popBackStackImmediate()) {
                    if ((System.currentTimeMillis() - exitTime) > getResources().getInteger(R.integer.finish_doubleclicktime)) {
                        Msg.showInfo(context, R.string.finish_clickagain);
                        exitTime = System.currentTimeMillis();
                    } else {
                        finish();
                    }
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!activedActivity.contains(this)) {
            activedActivity.add(this);
        }
//        if (getStatusBarTintDrawable() != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            SystemBarTintManager tintManager;
//            tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintDrawable(getStatusBarTintDrawable());
//            tintManager.setStatusBarTintEnabled(true);
//            setStatusBarDarkMode(getStatusBarDarkMode());
//        }
        doCreate();
        afterCreate();
        doInit();
    }

//    protected Drawable getStatusBarTintDrawable() {
//        return null;
//    }
//
//    protected boolean getStatusBarDarkMode() {
//        return false;
//    }
//
//    public void setStatusBarDarkMode(boolean darkmode) {
//        Class<? extends Window> clazz = getWindow().getClass();
//        try {
//            int darkModeFlag = 0;
//            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
//            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
//            darkModeFlag = field.getInt(layoutParams);
//            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
//            extraFlagField.invoke(getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doDestroy();
        if (activedActivity.contains(this)) {
            activedActivity.remove(this);
        }
    }

    protected abstract void doCreate();

    protected void afterCreate() {
        // do nothing
    }

    protected void doInit() {
        // do nothing
    }

    protected void doDestroy() {
        // do nothing
    }

    @Override
    public void onFragmentViewCreated(BaseFragment fragment) {

    }

    public static void finishAllActivity() {
        for (Activity activity : activedActivity) {
            activity.finish();
        }
        activedActivity.clear();
    }

    public static void finishOtherActivity(Class<? extends Activity> exclude) {
        List<Activity> include = new ArrayList<Activity>();
        for (Activity activity : activedActivity) {
            if (!exclude.isInstance(activity)) {
                activity.finish();
            } else {
                include.add(activity);
            }
        }
        activedActivity.clear();
        for (Activity activity : include) {
            activedActivity.add(activity);
        }
    }

    public <T extends BaseApplication> T getBaseApplication(Class<T> tClass) {
        return BaseApplication.getInstance(tClass);
    }

    protected int getVersionCode() {
        return getBaseApplication(BaseApplication.class).getVersionCode();
    }

    protected String getVersionName() {
        return getBaseApplication(BaseApplication.class).getVersionName();
    }

}
