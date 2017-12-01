package com.github.qbcbyb.versioncheck.firim;

import android.content.Context;
import android.os.Environment;

import com.github.qbcbyb.versioncheck.DownloadCallback;
import com.github.qbcbyb.versioncheck.IProgressControl;
import com.github.qbcbyb.versioncheck.IVersionDownloader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;

import java.io.File;
import java.text.MessageFormat;

import cz.msebera.android.httpclient.Header;

/**
 * Created by qbcby on 2016/7/29.
 */
class FirimVersionDownloader implements IVersionDownloader {
    private final AsyncHttpClient asyncHttpClient;
    private final VersionInfo version;

    public FirimVersionDownloader(AsyncHttpClient asyncHttpClient, VersionInfo version) {
        this.asyncHttpClient = asyncHttpClient;
        this.version = version;
    }

    public String getDownloadFileName() {
        return MessageFormat.format("{0}-{1}.apk", version.getName(), version.getVersionShort());
    }

    @Override
    public void download(Context context, final IProgressControl progressControl, final DownloadCallback downloadCallback) {
        final File dir_download = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getDownloadFileName());
        asyncHttpClient.get(version.getInstallUrl(), new RangeFileAsyncHttpResponseHandler(dir_download) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                boolean canRetry = file != null && file.exists() && file.delete();
                downloadCallback.fail(FirimVersionDownloader.this, throwable, canRetry);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                downloadCallback.downloaded(file);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                if (progressControl != null) {
                    progressControl.progressUpdate(bytesWritten * 100 / totalSize);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                if (progressControl != null) {
                    progressControl.progressStart();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (progressControl != null) {
                    progressControl.progressEnd();
                }
            }
        });
    }
}
