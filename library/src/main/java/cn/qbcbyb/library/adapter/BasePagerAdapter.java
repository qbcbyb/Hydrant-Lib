package cn.qbcbyb.library.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public abstract class BasePagerAdapter extends PagerAdapter {

    protected Context context;

    protected SparseArray<View> mViewList;
    private int count;

    public BasePagerAdapter(Context context, int count) {
        this.context = context;
        this.count = count;
        mViewList = new SparseArray<View>();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));// 删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 这个方法用来实例化页卡
        View view = mViewList.get(position);
        if (view == null) {
            view = createNewView(position);
            mViewList.put(position, view);
        }
        if (container.indexOfChild(view) >= 0) {
            destroyItem(container, position, view);
        }
        container.addView(view, 0);// 添加页卡
        return view;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    protected abstract View createNewView(int position);

}
