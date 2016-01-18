package com.twotoasters.jazzylistview.effects;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;

import com.twotoasters.jazzylistview.JazzyEffect;

public class CardsEffect implements JazzyEffect {

    private static final int INITIAL_ROTATION_ANGLE = 90;

    @Override
    public void initView(View item, int position, int scrollDirection) {
        ViewCompat.setPivotX(item, item.getWidth() / 2);
        ViewCompat.setPivotY(item, item.getHeight() / 2);
        ViewCompat.setRotationX(item, INITIAL_ROTATION_ANGLE * scrollDirection);
        ViewCompat.setTranslationY(item, item.getHeight() * scrollDirection);
    }

    @Override
    public void setupAnimation(View item, int position, int scrollDirection, ViewPropertyAnimatorCompat animator) {
        animator.rotationXBy(-INITIAL_ROTATION_ANGLE * scrollDirection)
                .translationYBy(-item.getHeight() * scrollDirection);
    }
}
