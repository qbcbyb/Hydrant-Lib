package com.github.qbcbyb.viewlibrary.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 秋云 on 2015/8/4.
 */
public abstract class RecyclerAdapter<Bean> extends RecyclerView.Adapter<ViewHolder<Bean>> {

    private List<Bean> mDataSet;
    private final LayoutInflater layoutInflater;

    protected RecyclerAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void changeData(List<Bean> dataSet) {
        this.mDataSet = dataSet;
    }

    public void addData(List<Bean> dataSet) {
        if (this.mDataSet == null) {
            this.mDataSet = dataSet;
        } else {
            this.mDataSet.addAll(dataSet);
        }
    }

    public List<Bean> getAllData() {
        return mDataSet;
    }

    public Bean getItemData(int position) {
        return mDataSet.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    @Override
    public ViewHolder<Bean> onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(layoutInflater, parent, viewType);
    }

    protected abstract ViewHolder<Bean> getViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder<Bean> holder, int position) {
        holder.bindData(getItemData(position), position);
    }

}
