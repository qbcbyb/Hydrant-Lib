package com.zbar.lib.decode;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.zbar.lib.R;
import com.zbar.lib.ZbarManager;
import com.zbar.lib.bitmap.PlanarYUVLuminanceSource;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * <p/>
 * 时间: 2014年5月9日 下午12:24:13
 * <p/>
 * 版本: V_1.0.0
 * <p/>
 * 描述: 接受消息后解码
 */
final class DecodeHandler extends Handler {

    CaptureContainer captureContainer = null;

    DecodeHandler(CaptureContainer captureContainer) {
        this.captureContainer = captureContainer;
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    private void decode(byte[] data, int width, int height) {
        final byte[] rotatedData;
        WindowManager windowManager = (WindowManager) captureContainer.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        final int rotation = display.getRotation();
        if (rotation == Surface.ROTATION_0) {
            rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++)
                    rotatedData[x + y * width] = data[(width - x - 1) * height + y];
            }
        } else if (rotation == Surface.ROTATION_270) {
            rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++)
                    rotatedData[x + y * width] = data[(width - x - 1) + (height - y - 1) * width];
            }
        } else if (rotation == Surface.ROTATION_180) {
            rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++)
                    rotatedData[x + y * width] = data[x * height + height - y - 1];
            }
        } else {// 如果是横屏
            rotatedData = data;
        }
        ZbarManager manager = new ZbarManager();
        String result = manager.decode(rotatedData, width, height, true, captureContainer.getX(), captureContainer.getY(), captureContainer.getCropWidth(),
                captureContainer.getCropHeight());

        if (result != null) {
            if (captureContainer.isNeedCapture()) {
                // 生成bitmap
                PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(rotatedData, width, height, captureContainer.getX(), captureContainer.getY(),
                        captureContainer.getCropWidth(), captureContainer.getCropHeight(), false);
                int[] pixels = source.renderThumbnail();
                int w = source.getThumbnailWidth();
                int h = source.getThumbnailHeight();
                Bitmap bitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888);
                try {
                    String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Qrcode/";
                    File root = new File(rootPath);
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File f = new File(rootPath + "Qrcode.jpg");
                    if (f.exists()) {
                        f.delete();
                    }
                    f.createNewFile();

                    FileOutputStream out = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (null != captureContainer.getHandler()) {
                Message msg = new Message();
                msg.obj = result;
                msg.what = R.id.decode_succeeded;
                captureContainer.getHandler().sendMessage(msg);
            }
        } else {
            if (null != captureContainer.getHandler()) {
                captureContainer.getHandler().sendEmptyMessage(R.id.decode_failed);
            }
        }
    }

}
