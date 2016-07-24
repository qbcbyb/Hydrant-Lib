package com.github.qbcbyb.viewlibrary.recycler.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qbcby on 2016/6/14.
 */
public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter {
    private final SparseArrayCompat<LayoutBuilder<Cursor>> layoutBuilderArray;
    private final LayoutBuilder<Cursor> defaultBuilder;

    public SimpleCursorRecyclerAdapter(Context context, int layoutId, Class<? extends ViewHolder<Cursor>> holderClass) {
        super(context);
        layoutBuilderArray = null;
        defaultBuilder = new LayoutBuilder<>(layoutId, holderClass);
    }

    public SimpleCursorRecyclerAdapter(Context context, SparseArrayCompat<LayoutBuilder<Cursor>> layoutBuilderArray) {
        super(context);
        this.layoutBuilderArray = layoutBuilderArray;
        defaultBuilder = layoutBuilderArray.valueAt(0);
    }

    @Override
    protected ViewHolder<Cursor> getViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        LayoutBuilder<Cursor> builder = layoutBuilderArray == null ? defaultBuilder : layoutBuilderArray.get(viewType, defaultBuilder);
        View view = layoutInflater.inflate(builder.layoutId, parent, false);
        try {
            return builder.holderConstructor.newInstance(view);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
