package cn.qbcbyb.library.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class ModelPagerAdapter<T> extends PagerAdapter {

    protected Context context;

    protected List<T> mDataList;
    protected SparseArray<View> mViewList;

    public ModelPagerAdapter(Context context, List<T> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
        mViewList = new SparseArray<View>();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);// 删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 这个方法用来实例化页卡
        View view = mViewList.get(position);
        if (mViewList.get(position) == null) {
            view = createNewView(mDataList.get(position), position);
            mViewList.put(position, view);
        }
        container.addView(view, 0);// 添加页卡
        return view;
    }

    public List<T> getData() {
        return mDataList;
    }

    public void addData(List<T> data) {
        if (mDataList != null) {
            mDataList.addAll(data);
        }
    }

    public void addData(T data) {
        if (mDataList != null) {
            mDataList.add(data);
        }
    }

    public void changeData(List<T> data) {
        if (mDataList != null) {
            mDataList = null;
            mDataList = data;
        } else {
            mDataList = data;
        }

    }

    @Override
    public int getItemPosition(Object object) {
        if (mViewList != null) {
            View view = (View) object;
            return mViewList.keyAt(mViewList.indexOfValue(view));
        }
        return -1;
    }

    @Override
    public int getCount() {
        return this.mDataList == null ? 0 : this.mDataList.size();
    }

    public View getItem(int position) {
        if (mViewList != null && mViewList.indexOfKey(position) > -1) {
            return mViewList.get(position);
        }
        return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    protected abstract View createNewView(T data, int position);

}
