package com.github.qbcbyb.viewlibrary.recycler.adapter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by qbcby on 2016/6/14.
 */
public class SimpleRecyclerAdapter<Bean> extends RecyclerAdapter<Bean> {
    private final SparseArrayCompat<LayoutBuilder<Bean, ? extends ViewHolder<Bean>>> layoutBuilderArray;
    private final LayoutBuilder<Bean, ? extends ViewHolder<Bean>> defaultBuilder;

    public SimpleRecyclerAdapter(Context context, LayoutBuilder<Bean, ? extends ViewHolder<Bean>> layoutBuilder) {
        super(context);
        layoutBuilderArray = null;
        defaultBuilder = layoutBuilder;
    }

    public SimpleRecyclerAdapter(Context context, SparseArrayCompat<LayoutBuilder<Bean, ? extends ViewHolder<Bean>>> layoutBuilderArray) {
        super(context);
        this.layoutBuilderArray = layoutBuilderArray;
        defaultBuilder = layoutBuilderArray.valueAt(0);
    }

    @Override
    protected ViewHolder<Bean> getViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        LayoutBuilder<Bean, ? extends ViewHolder<Bean>> builder = layoutBuilderArray == null ? defaultBuilder : layoutBuilderArray.get(viewType, defaultBuilder);
        return builder.generateViewHolder(layoutInflater, parent, false);
    }
}
