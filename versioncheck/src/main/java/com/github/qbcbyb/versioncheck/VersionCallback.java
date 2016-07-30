package com.github.qbcbyb.versioncheck;

/**
 * Created by qbcby on 2016/7/29.
 */
public interface VersionCallback {

    void needUpdate(IVersionDownloader downloader);

    void noNeedUpdate();

    void fail(Throwable throwable);

}
