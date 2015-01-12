package cn.qbcbyb.library.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import cn.qbcbyb.library.adapter.InfinitePagerAdapter;

public class InfiniteViewPager extends ViewPager {

    public InfiniteViewPager(Context context) {
        super(context);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        // offset first element so that we can scroll to the left
        if (this.getAdapter() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter pagerAdapter = (InfinitePagerAdapter) getAdapter();
            setCurrentItem(pagerAdapter.getRealCount() * 1000);
        }
    }

}