package com.github.qbcbyb.viewlibrarysample;

import android.support.v7.widget.RecyclerView;

import com.github.qbcbyb.viewlibrary.recycler.DeckLayoutManager;

/**
 * Created by qbcby on 2016/6/14.
 */
public class DeckActivity extends BaseRecyclerActivity {
    @Override
    protected int getItemLayoutId() {
        return R.layout.list_notpager_item;
    }

    @Override
    protected RecyclerView.LayoutManager generateLayoutManager() {
        return new DeckLayoutManager((int) (50 * getResources().getDisplayMetrics().density + .5f));
    }
}
