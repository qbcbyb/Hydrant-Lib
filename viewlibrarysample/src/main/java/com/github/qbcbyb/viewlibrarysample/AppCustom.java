package com.github.qbcbyb.viewlibrarysample;

import cn.qbcbyb.library.app.BaseApplication;

/**
 * Created by qbcby on 2016/6/14.
 */
public class AppCustom extends BaseApplication {
    @Override
    public boolean isDebugEable() {
        return BuildConfig.DEBUG;
    }
}
