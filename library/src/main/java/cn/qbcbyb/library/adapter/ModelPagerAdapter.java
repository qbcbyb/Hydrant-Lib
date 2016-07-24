package cn.qbcbyb.library.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ModelPagerAdapter<T> extends PagerAdapter {

    protected Context context;

    protected List<T> mDataList;
    protected Map<T, WeakReference<View>> mViewList;

    public ModelPagerAdapter(Context context, List<T> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
        mViewList = new HashMap<>();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        final T t = ((T) object);
        final View view;
        if (mViewList.containsKey(t) && (view = mViewList.get(t).get()) != null) {
            container.removeView(view);// 删除页卡
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final T t = mDataList.get(position);
        final View view;
        if (!mViewList.containsKey(t) || mViewList.get(t).get() == null) {
            view = createNewView(t, position);
            mViewList.put(t, new WeakReference<>(view));
        } else {
            view = mViewList.get(t).get();
        }
        container.addView(view, 0);// 添加页卡
        return t;
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
        final T t = (T) object;
        return mDataList != null && mDataList.contains(t) ? mDataList.indexOf(t) : POSITION_NONE;
    }

    @Override
    public int getCount() {
        return this.mDataList == null ? 0 : this.mDataList.size();
    }

    public T getItem(int position) {
        if (mDataList != null && mDataList.size() > position) {
            return mDataList.get(position);
        }
        return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == mViewList.get((T) obj).get();
    }

    protected abstract View createNewView(T data, int position);

}
