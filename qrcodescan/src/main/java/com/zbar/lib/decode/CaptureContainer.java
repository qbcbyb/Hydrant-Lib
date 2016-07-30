package com.zbar.lib.decode;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.zbar.lib.R;
import com.zbar.lib.camera.CameraManager;

import java.io.IOException;

/**
 * Created by qbcby on 2015/12/9.
 */
public class CaptureContainer implements SurfaceHolder.Callback {
    private int x = 0;
    private int y = 0;
    private int cropWidth = 0;
    private int cropHeight = 0;
    private boolean isNeedCapture = false;
    private CaptureHandler handler;

    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.50f;
    private boolean vibrate;
    private View mContainer = null;
    private View mCropLayout = null;

    private CaptureContainerCanFinish container;
    private SurfaceView surfaceView;
    private View mQrLineView;

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getCropWidth() {
        return cropWidth;
    }

    int getCropHeight() {
        return cropHeight;
    }

    boolean isNeedCapture() {
        return isNeedCapture;
    }

    CaptureHandler getHandler() {
        return handler;
    }

    void handleDecode(String result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
//		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

        if (container.onCaptureAndCheckNeedRestart(result)) {
            // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
            getHandler().sendEmptyMessage(R.id.restart_preview);
        }
    }

    protected void initHandler() {
        if (handler == null) {
            handler = new CaptureHandler(this);
        }
    }

    public void init(CaptureContainerCanFinish container,
                     int capture_containter, int capture_crop_layout,
                     int capture_preview, int capture_scan_line) {
        init(container, container.findById(capture_containter), container.findById(capture_crop_layout),
                (SurfaceView) container.findById(capture_preview), container.findById(capture_scan_line));
    }

    public void init(CaptureContainerCanFinish container,
                     View capture_containter, View capture_crop_layout,
                     SurfaceView capture_preview, View capture_scan_line) {
        this.container = container;
        // 初始化 CameraManager
        CameraManager.init(container.getActivity().getApplication());

        hasSurface = false;
        inactivityTimer = new InactivityTimer(container);

        mContainer = capture_containter;
        mCropLayout = capture_crop_layout;
        surfaceView = capture_preview;
        mQrLineView = capture_scan_line;

    }

    private void startScanAnimation() {
        TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
        mAnimation.setDuration(1500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        mQrLineView.startAnimation(mAnimation);
    }

    private void stopScanAnimation() {
        mQrLineView.clearAnimation();
    }

    public void resume() {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        playBeep = true;
        AudioManager audioService = (AudioManager) container.getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        startScanAnimation();
    }

    boolean flag = true;

    public void light() {
        if (flag) {
            flag = false;
            // 开闪光灯
            CameraManager.get().openLight();
        } else {
            flag = true;
            // 关闪光灯
            CameraManager.get().offLight();
        }

    }

    public void pause() {
        if (getHandler() != null) {
            getHandler().quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
        stopScanAnimation();
    }

    public void destory() {
        pause();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        inactivityTimer.shutdown();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

            Point point = CameraManager.get().getCameraResolution();
            int width = point.x;
            int height = point.y;

            float widthDividerHeight = width < height ? ((float) width / height) : ((float) height / width);
            final ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
            layoutParams.height = (int) (surfaceView.getWidth() / widthDividerHeight);
            surfaceView.requestLayout();

            final float widthRate = width / (float) surfaceView.getWidth();
            this.x = (int) (mCropLayout.getLeft() * widthRate);
            this.y = (int) (mCropLayout.getTop() * widthRate);

            this.cropWidth = (int) (mCropLayout.getWidth() * widthRate);
            this.cropHeight = (int) (mCropLayout.getHeight() * widthRate);

            // 设置是否需要截图
            this.isNeedCapture = true;
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        initHandler();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
            holder.setSizeFromLayout();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    private void initBeepSound() {
        if (playBeep) {
            if (mediaPlayer == null) {
                container.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            } else {
                mediaPlayer.reset();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            }

            AssetFileDescriptor file = container.getActivity().getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    public Context getContext() {
        return container.getActivity();
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) container.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    public interface CaptureContainerCanFinish extends FinishListener.OnFinishListener {
        View findById(int id);

        Activity getActivity();

        boolean onCaptureAndCheckNeedRestart(String result);
    }
}
