package com.zbar.lib.decode;

import android.content.DialogInterface;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * <p/>
 * 时间: 2014年5月9日 下午12:24:51
 * <p/>
 * 版本: V_1.0.0
 */
public final class FinishListener
        implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable {

    public interface OnFinishListener {
        void onFinish();
    }

    private final OnFinishListener activityToFinish;

    public FinishListener(OnFinishListener activityToFinish) {
        this.activityToFinish = activityToFinish;
    }

    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }

    public void run() {
        activityToFinish.onFinish();
    }

}
