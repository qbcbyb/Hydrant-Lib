package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * <p/>
 * 时间: 2014年5月9日 下午12:24:34
 * <p/>
 * 版本: V_1.0.0
 * <p/>
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {

    public static final String TAG = "QRDecodeThread";
    CaptureContainer captureContainer;
    private Handler handler;
    private final CountDownLatch handlerInitLatch;

    DecodeThread(CaptureContainer captureContainer) {
        super(TAG);
        this.captureContainer = captureContainer;
        handlerInitLatch = new CountDownLatch(1);
    }

    Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            // continue?
        }
        return handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new DecodeHandler(captureContainer);
        handlerInitLatch.countDown();
        Looper.loop();
    }

    public void destory() {
        if (handler != null) {
            handler.getLooper().quit();
        }
    }

}
