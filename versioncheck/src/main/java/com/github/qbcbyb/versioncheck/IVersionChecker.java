package com.github.qbcbyb.versioncheck;

import android.content.Context;

/**
 * Created by qbcby on 2016/7/29.
 */
public interface IVersionChecker {
    void getLatestVersion(Context context, String checkUrl, VersionCallback callback);

    void getLatestVersion(Context context, String checkUrl, IProgressControl progressControl, VersionCallback callback);
}
