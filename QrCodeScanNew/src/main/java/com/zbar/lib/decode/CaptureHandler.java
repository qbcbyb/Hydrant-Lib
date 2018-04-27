package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Message;

import com.zbar.lib.CaptureActivity;
import com.zbar.lib.R;
import com.zbar.lib.camera.CameraManager;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * <p/>
 * 时间: 2014年5月9日 下午12:23:32
 * <p/>
 * 版本: V_1.0.0
 * <p/>
 * 描述: 扫描消息转发
 */
public final class CaptureHandler extends Handler {

    DecodeThread decodeThread = null;
    CaptureContainer captureContainer = null;
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureHandler(CaptureContainer captureContainer) {
        this.captureContainer = captureContainer;
        decodeThread = new DecodeThread(captureContainer);
        decodeThread.start();
        state = State.SUCCESS;
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {

        if (message.what == R.id.auto_focus) {
            if (state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            }
        } else if (message.what == R.id.restart_preview) {
            restartPreviewAndDecode();
        } else if (message.what == R.id.decode_succeeded) {
            state = State.SUCCESS;
            captureContainer.handleDecode((String) message.obj);// 解析成功，回调
        } else if (message.what == R.id.decode_failed) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
                    R.id.decode);
        }

    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
        removeMessages(R.id.decode);
        removeMessages(R.id.auto_focus);
        decodeThread.destory();
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
                    R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
    }
}
