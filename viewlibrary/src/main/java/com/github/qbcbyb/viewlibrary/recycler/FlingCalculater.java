package com.github.qbcbyb.viewlibrary.recycler;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.ViewConfiguration;

/**
 * Created by qbcby on 2016/6/16.
 */
public class FlingCalculater {
    protected static final float INFLEXION = 0.35f; // Tension lines cross at (INFLEXION, 1)
    protected static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
    protected static double FRICTION = 0.84;

    private double deceleration;

    public void calculateDeceleration(Context context) {
        deceleration = SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.3700787 // inches per meter
                // pixels per inch. 160 is the "default" dpi, i.e. one dip is one pixel on a 160 dpi
                // screen
                * context.getResources().getDisplayMetrics().density * 160.0f * FRICTION;
    }

    public int getSplineFlingDuration(float velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return (int) (1000.0 * Math.exp(l / decelMinusOne));
    }

    public double getSplineFlingDistance(double velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return ViewConfiguration.getScrollFriction() * deceleration
                * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }

    private double getSplineDeceleration(double velocity) {
        return Math.log(INFLEXION * Math.abs(velocity)
                / (ViewConfiguration.getScrollFriction() * deceleration));
    }
}
