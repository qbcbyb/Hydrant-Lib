package com.github.qbcbyb.viewlibrary.recycler.adapter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qbcby on 2016/6/14.
 */
public class SimpleRecyclerAdapter<Bean> extends RecyclerAdapter<Bean> {
    private final SparseArrayCompat<LayoutBuilder<Bean>> layoutBuilderArray;
    private final LayoutBuilder<Bean> defaultBuilder;

    public SimpleRecyclerAdapter(Context context, int layoutId, Class<? extends ViewHolder<Bean>> holderClass) {
        super(context);
        layoutBuilderArray = null;
        defaultBuilder = new LayoutBuilder<>(layoutId, holderClass);
    }

    public SimpleRecyclerAdapter(Context context, SparseArrayCompat<LayoutBuilder<Bean>> layoutBuilderArray) {
        super(context);
        this.layoutBuilderArray = layoutBuilderArray;
        defaultBuilder = layoutBuilderArray.valueAt(0);
    }

    @Override
    protected ViewHolder<Bean> getViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        LayoutBuilder<Bean> builder = layoutBuilderArray == null ? defaultBuilder : layoutBuilderArray.get(viewType, defaultBuilder);
        View view = layoutInflater.inflate(builder.layoutId, parent, false);
        try {
            return builder.holderConstructor.newInstance(view);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
