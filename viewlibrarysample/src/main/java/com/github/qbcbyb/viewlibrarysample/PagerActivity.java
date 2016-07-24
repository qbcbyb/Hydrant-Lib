package com.github.qbcbyb.viewlibrarysample;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.github.qbcbyb.viewlibrary.recycler.PagerLayoutManager;

/**
 * Created by qbcby on 2016/6/14.
 */
public class PagerActivity extends BaseRecyclerActivity {
    public static final String KEY_OPEN_NORMAL = "OPEN_NORMAL";
    private boolean isNormal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isNormal = getIntent().getBooleanExtra(KEY_OPEN_NORMAL, true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getItemLayoutId() {
        return isNormal ? R.layout.list_normal_pager_item : R.layout.list_pager_item;
    }

    @Override
    protected RecyclerView.LayoutManager generateLayoutManager() {
        return new PagerLayoutManager();
    }
}
