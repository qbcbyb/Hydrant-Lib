package com.github.qbcbyb.viewlibrary.recycler.pager;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.Gravity;

public class SimpleFoldShading implements FoldShading {

    private static final int SHADOW_START_COLOR = 0x33000000;
    private static final int SHADOW_END_COLOR = 0x01000000;
    private static final int SHADOW_MAX_ALPHA = 192;

    private final Paint mSolidShadow;
    private final Matrix localM;
    private final LinearGradient shader;

    public SimpleFoldShading() {
        mSolidShadow = new Paint();
        localM = new Matrix();
        shader = new LinearGradient(0, 0, 200, 0, SHADOW_START_COLOR, SHADOW_END_COLOR, Shader.TileMode.CLAMP);
        mSolidShadow.setShader(shader);
    }

    @Override
    public void onPreDraw(Canvas canvas, Rect bounds, float rotation, int gravity) {
        // NO-OP
    }

    @Override
    public void onPostDraw(Canvas canvas, Rect bounds, float rotation, int gravity) {
        float intensity = getShadowIntensity(rotation, gravity);
        if (intensity > 0) {
            localM.setTranslate(bounds.left, bounds.top);
            shader.setLocalMatrix(localM);
            int alpha = (int) (SHADOW_MAX_ALPHA * intensity);
            mSolidShadow.setAlpha(alpha);
            canvas.drawRect(bounds, mSolidShadow);
        }
    }

    private float getShadowIntensity(float rotation, int gravity) {
        float intensity = 0;
        if (gravity == Gravity.RIGHT) {
            if (rotation > -90 && rotation < 0) { // (-90; 0) - rotation is applied
                intensity = -rotation / 90f;
            }
//        } else {
//            if (rotation > 0 && rotation < 90) { // (0; 90) - rotation is applied
//                intensity = rotation / 90f;
//            }
        }
        return intensity;
    }

}
