package cn.qbcbyb.lib;

import cn.qbcbyb.library.app.BaseApplication;

/**
 * Created by 秋云 on 2014/10/31.
 */
public class AppCustom extends BaseApplication {
    @Override
    public boolean isDebugEable() {
        return BuildConfig.DEBUG;
    }
}
