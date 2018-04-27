package com.github.qbcbyb.viewlibrary.recycler.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by qbcby on 2016/6/14.
 */
public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter {
    private final SparseArrayCompat<LayoutBuilder<Cursor, ? extends ViewHolder<Cursor>>> layoutBuilderArray;
    private final LayoutBuilder<Cursor, ? extends ViewHolder<Cursor>> defaultBuilder;

    public SimpleCursorRecyclerAdapter(Context context, LayoutBuilder<Cursor, ? extends ViewHolder<Cursor>> layoutBuilder) {
        super(context);
        layoutBuilderArray = null;
        defaultBuilder = layoutBuilder;
    }

    public SimpleCursorRecyclerAdapter(Context context, SparseArrayCompat<LayoutBuilder<Cursor, ? extends ViewHolder<Cursor>>> layoutBuilderArray) {
        super(context);
        this.layoutBuilderArray = layoutBuilderArray;
        defaultBuilder = layoutBuilderArray.valueAt(0);
    }

    @Override
    protected ViewHolder<Cursor> getViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        LayoutBuilder<Cursor, ? extends ViewHolder<Cursor>> builder = layoutBuilderArray == null ? defaultBuilder : layoutBuilderArray.get(viewType, defaultBuilder);
        return builder.generateViewHolder(layoutInflater, parent, false);
    }
}
