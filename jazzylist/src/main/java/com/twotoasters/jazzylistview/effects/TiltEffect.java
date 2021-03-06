/*
 * Copyright (C) 2015 Two Toasters
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twotoasters.jazzylistview.effects;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;

import com.twotoasters.jazzylistview.JazzyEffect;
import com.twotoasters.jazzylistview.JazzyHelper;

public class TiltEffect implements JazzyEffect {

    private static final float INITIAL_SCALE_FACTOR = 0.7f;

    @Override
    public void initView(View item, int position, int scrollDirection) {
        ViewCompat.setPivotX(item, item.getWidth() / 2);
        ViewCompat.setPivotY(item, item.getHeight() / 2);
        ViewCompat.setScaleX(item, INITIAL_SCALE_FACTOR);
        ViewCompat.setScaleY(item, INITIAL_SCALE_FACTOR);
        ViewCompat.setTranslationY(item, item.getHeight() / 2 * scrollDirection);
        ViewCompat.setAlpha(item, JazzyHelper.OPAQUE / 2);
    }

    @Override
    public void setupAnimation(View item, int position, int scrollDirection, ViewPropertyAnimatorCompat animator) {
        animator
                .translationYBy(-item.getHeight() / 2 * scrollDirection)
                .scaleX(1)
                .scaleY(1)
                .alpha(JazzyHelper.OPAQUE);
    }
}
