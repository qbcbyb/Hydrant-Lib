package com.github.qbcbyb.versioncheck;

import java.io.File;

/**
 * Created by qbcby on 2016/7/29.
 */
public interface DownloadCallback {
    void downloaded(File file);

    void fail(IVersionDownloader downloader, Throwable throwable, boolean canRetry);
}
