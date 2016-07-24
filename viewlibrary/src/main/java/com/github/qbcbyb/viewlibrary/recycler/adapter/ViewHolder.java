package com.github.qbcbyb.viewlibrary.recycler.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by qbcby on 2015/12/5.
 */
public abstract class ViewHolder<Bean> extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(Bean data, int position);

}
