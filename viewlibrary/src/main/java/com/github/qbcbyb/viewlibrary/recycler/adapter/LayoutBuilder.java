package com.github.qbcbyb.viewlibrary.recycler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qbcby on 2016/6/14.
 */
public abstract class LayoutBuilder<Bean, T extends ViewHolder<Bean>> {
    private final int layoutId;

    public LayoutBuilder(int layoutId) {
        this.layoutId = layoutId;
    }

    public T generateViewHolder(LayoutInflater layoutInflater, ViewGroup parent, boolean attachToRoot) {
        return newViewHolder(layoutInflater.inflate(layoutId, parent, attachToRoot));
    }

    public abstract T newViewHolder(View itemView);
}
