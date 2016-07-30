package com.github.qbcbyb.versioncheck.firim;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.github.qbcbyb.libcommon.ContextUtil;
import com.github.qbcbyb.versioncheck.IProgressControl;
import com.github.qbcbyb.versioncheck.IVersionChecker;
import com.github.qbcbyb.versioncheck.VersionCallback;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by qbcby on 2016/7/29.
 */
public class FirimVersionChecker implements IVersionChecker {

    @Override
    public void getLatestVersion(Context context, String checkUrl, VersionCallback callback) {
        this.getLatestVersion(context, checkUrl, null, callback);
    }

    @Override
    public void getLatestVersion(Context context, String checkUrl, final IProgressControl progressControl, final VersionCallback callback) {
        final int nowVersionCode = ContextUtil.getVersionCode(context);
        final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(context, checkUrl, new BaseJsonHttpResponseHandler<VersionInfo>() {
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

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                if (progressControl != null) {
                    progressControl.progressUpdate(bytesWritten * 100 / totalSize);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, VersionInfo response) {
                String version;
                if (response == null || (version = response.getVersion()) == null) {
                    callback.fail(new NullPointerException("version is null"));
                    return;
                }
                int versionCode = -1;
                try {
                    versionCode = Integer.parseInt(version);
                } catch (NumberFormatException e) {
                    callback.fail(new NullPointerException("version is not int"));
                    return;
                }
                if (versionCode > nowVersionCode) {
                    callback.needUpdate(new FirimVersionDownloader(asyncHttpClient, response));
                } else {
                    callback.noNeedUpdate();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, VersionInfo errorResponse) {
                callback.fail(throwable);
            }

            @Override
            protected VersionInfo parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return JSON.parseObject(rawJsonData, VersionInfo.class);
            }
        });
    }

}
