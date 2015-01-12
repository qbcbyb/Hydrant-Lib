package cn.qbcbyb.library.adapter;

import android.content.Context;
import android.view.ViewGroup;

import cn.qbcbyb.library.util.DebugUtil;

/**
 * A PagerAdapter that wraps around another PagerAdapter to handle paging
 * wrap-around.
 */
public abstract class InfinitePagerAdapter extends BasePagerAdapter {

    private static final String TAG = "InfinitePagerAdapter";

    public InfinitePagerAdapter(Context context, int count) {
        super(context, count);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return super.getCount();
    }

    public int getVirtualPosition(int position) {
        int index = position % getRealCount();
        return index;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int virtualPosition = getVirtualPosition(position);
        DebugUtil.d(TAG, "instantiateItem: real position: " + position);
        DebugUtil.d(TAG, "instantiateItem: virtual position: " + virtualPosition);
        return super.instantiateItem(container, virtualPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int virtualPosition = getVirtualPosition(position);
        DebugUtil.d(TAG, "destroyItem: real position: " + position);
        DebugUtil.d(TAG, "destroyItem: virtual position: " + virtualPosition);

        super.destroyItem(container, position, object);
    }

    /*
     * End delegation
     */

}