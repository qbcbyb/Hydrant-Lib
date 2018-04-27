package com.zbar.lib.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * <p/>
 * 时间: 2014年5月9日 下午12:22:25
 * <p/>
 * 版本: V_1.0.0
 * <p/>
 * 描述: 相机管理
 */
public final class CameraManager {
    static final int SDK_INT;
    private static CameraManager cameraManager;

    static {
        int sdkInt;
        try {
            sdkInt = android.os.Build.VERSION.SDK_INT;
        } catch (NumberFormatException nfe) {
            sdkInt = 10000;
        }
        SDK_INT = sdkInt;
    }

    private final CameraConfigurationManager configManager;
    private final boolean useOneShotPreviewCallback;
    private final PreviewCallback previewCallback;
    private final AutoFocusCallback autoFocusCallback;
    private Camera camera;
    private boolean initialized;
    private boolean previewing;
    private Parameters parameter;

    private CameraManager(Context context) {
        this.configManager = new CameraConfigurationManager(context);

        useOneShotPreviewCallback = SDK_INT > 3;
        previewCallback = new PreviewCallback(configManager, useOneShotPreviewCallback);
        autoFocusCallback = new AutoFocusCallback();
    }

    public static void init(Context context) {
        if (cameraManager == null) {
            cameraManager = new CameraManager(context);
        }
    }

    public static CameraManager get() {
        return cameraManager;
    }

    public void openDriver(SurfaceHolder holder) throws IOException {
        if (camera == null) {
            camera = Camera.open();
            if (camera == null) {
                throw new IOException();
            }
            camera.setPreviewDisplay(holder);

            if (!initialized) {
                initialized = true;
                configManager.initFromCameraParameters(camera);
            }
            configManager.setDesiredCameraParameters(camera);
            FlashlightManager.enableFlashlight();
        }
    }

    public Point getCameraResolution() {
        return configManager.getCameraResolution();
    }

    public void closeDriver() {
        if (camera != null) {
            FlashlightManager.disableFlashlight();
            camera.release();
            camera = null;
        }
    }

    public void startPreview() {
        if (camera != null && !previewing) {
            camera.startPreview();
            previewing = true;
        }
    }

    public void stopPreview() {
        if (camera != null && previewing) {
            if (!useOneShotPreviewCallback) {
                camera.setPreviewCallback(null);
            }
            camera.stopPreview();
            previewCallback.setHandler(null, 0);
            autoFocusCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    public void requestPreviewFrame(Handler handler, int message) {
        if (camera != null && previewing) {
            previewCallback.setHandler(handler, message);
            if (useOneShotPreviewCallback) {
                camera.setOneShotPreviewCallback(previewCallback);
            } else {
                camera.setPreviewCallback(previewCallback);
            }
        }
    }

    public void requestAutoFocus(Handler handler, int message) {
        if (camera != null && previewing) {
            autoFocusCallback.setHandler(handler, message);
            camera.autoFocus(autoFocusCallback);
        }
    }

    public void takePhoto(Camera.ShutterCallback shutter, Camera.PictureCallback raw,
                          Camera.PictureCallback jpeg) {
        if (camera != null && previewing) {
            camera.takePicture(shutter, raw, jpeg);
        }
    }

    public void toggleLight() {
        if (camera != null) {
            parameter = camera.getParameters();
            final String targetFlashMode;
            if (!Parameters.FLASH_MODE_OFF.equals(parameter.getFlashMode())) {
                targetFlashMode = Parameters.FLASH_MODE_OFF;
            } else {
                targetFlashMode = Parameters.FLASH_MODE_TORCH;
            }
            parameter.setFlashMode(targetFlashMode);
            camera.setParameters(parameter);
        }
    }

    public void openLight() {
        if (camera != null) {
            parameter = camera.getParameters();
            parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
        }
    }

    public void offLight() {
        if (camera != null) {
            parameter = camera.getParameters();
            parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameter);
        }
    }
}
