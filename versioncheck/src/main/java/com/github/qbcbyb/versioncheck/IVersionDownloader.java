package com.github.qbcbyb.versioncheck;

import android.content.Context;

/**
 * Created by qbcby on 2016/7/29.
 */
public interface IVersionDownloader {
    void download(Context context,IProgressControl progressControl,DownloadCallback downloadCallback);
}
