package com.github.qbcbyb.viewlibrary;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by qbcby on 2016/8/8.
 */
public class ScreenShotTool {
    private final View captureView;

    public ScreenShotTool(View captureView) {
        this.captureView = captureView;
    }

    public Bitmap takeScreenShot() {
        boolean drawingCacheEnabled = captureView.isDrawingCacheEnabled();
        captureView.setDrawingCacheEnabled(true);
        captureView.buildDrawingCache();
        Bitmap drawingCache = captureView.getDrawingCache();
        captureView.setDrawingCacheEnabled(drawingCacheEnabled);
        return drawingCache;
    }

    public Bitmap takeLongScreenShot() {
        final int width, height;
//        if(captureView instanceof RecyclerView){
//            RecyclerView captureView = (RecyclerView) this.captureView;
//            captureView.
//            int horizontalScrollOffset = captureView.computeHorizontalScrollOffset();
//            int horizontalScrollExtent = captureView.computeHorizontalScrollExtent();
//            int horizontalScrollRange = captureView.computeHorizontalScrollRange();
//            captureView.getScrollX()
//            width=captureView.getWidth();
//            height= horizontalScrollOffset
//        }
        boolean drawingCacheEnabled = captureView.isDrawingCacheEnabled();
        captureView.setDrawingCacheEnabled(true);
        captureView.buildDrawingCache();
        Bitmap drawingCache = captureView.getDrawingCache();
        captureView.setDrawingCacheEnabled(drawingCacheEnabled);
        return drawingCache;
    }
}
