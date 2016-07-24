package com.github.qbcbyb.viewlibrary.recycler.adapter;

import android.view.View;

import java.lang.reflect.Constructor;

/**
 * Created by qbcby on 2016/6/14.
 */
public class LayoutBuilder<Bean> {
    public final int layoutId;
    public final Constructor<? extends ViewHolder<Bean>> holderConstructor;

    public LayoutBuilder(int layoutId, Class<? extends ViewHolder<Bean>> holderClass) {
        this.layoutId = layoutId;
        try {
            this.holderConstructor = holderClass.getConstructor(View.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("holderConstructor must has only one View parameter!");
        }
    }
}
