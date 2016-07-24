package com.github.qbcbyb.viewlibrarysample;

import android.support.v7.widget.RecyclerView;

import com.github.qbcbyb.viewlibrary.recycler.ResizeLayoutManager;

/**
 * Created by qbcby on 2016/6/14.
 */
public class ResizeActivity extends BaseRecyclerActivity {
    @Override
    protected int getItemLayoutId() {
        return R.layout.list_notpager_item;
    }

    @Override
    protected RecyclerView.LayoutManager generateLayoutManager() {
        float density = getResources().getDisplayMetrics().density;
        return new ResizeLayoutManager((int) (120 * density), (int) (240 * density));
    }
}
