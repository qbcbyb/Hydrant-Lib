package com.github.qbcbyb.viewlibrary.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.github.qbcbyb.viewlibrary.recycler.pager.FoldableItemLayout;

import java.util.Arrays;

/**
 * Created by 秋云 on 2015/7/28.
 */
public class PagerLayoutManager extends BaseLayoutManager {
    private static final String TAG = PagerLayoutManager.class.getSimpleName();
    private static final int CACHE_VIEW_COUNT = 5;
    public static final float DEFAULT_SCALE = 0.9f;

    private int HORIZONTAL_MARGIN;
    private int MAX_OFFSET;//自中心向两侧的最大偏移量
    private int VERTICAL_MARGIN;

    private DecelerateInterpolator decelerateDefault = new DecelerateInterpolator(2);
    private float density;

    @Override
    public int getItemSize() {
        return getWidth() / 4;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        view.setClipChildren(false);
        density = view.getContext().getResources().getDisplayMetrics().density;
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

        final int widthSpec = getChildMeasureSpec(getWidth(),
                getPaddingLeft() + getPaddingRight() +
                        lp.leftMargin + lp.rightMargin + widthUsed, -1,
                false);
        final int heightSpec = getChildMeasureSpec(getHeight(),
                getPaddingTop() + getPaddingBottom() +
                        lp.topMargin + lp.bottomMargin + heightUsed, -1,
                false);
        child.measure(widthSpec, heightSpec);
    }

    @Override
    protected IndexToAttach computeLayoutIndex() {
        HORIZONTAL_MARGIN = getWidth() / 15;
        VERTICAL_MARGIN = (int) (50 * density);// HORIZONTAL_MARGIN;
        MAX_OFFSET = getWidth() / 6;

        int start = CACHE_VIEW_COUNT + 1;
        int end = 1;
        return new MyIndexToAttach(start, end, getNowScrollPixels());
    }

    @Override
    protected void layoutChildFromViewCache(RecyclerView.Recycler recycler, IndexToAttach indexToAttach, SparseArray<View> viewCache) {
        MyIndexToAttach ind = (MyIndexToAttach) indexToAttach;
        final double nowPosition = (double) ind.nowScroll / getItemSize();
        int nextPosition = ind.getIndex();
        int front = (int) Math.ceil(nowPosition - nextPosition);
        int back = (int) Math.floor(nowPosition + nextPosition);

        double frontDistance = front - nowPosition;
        double backDistance = back - nowPosition;
        if (Math.abs(frontDistance) < Math.abs(backDistance)) {
            layoutChild(ind, back, backDistance, recycler, getChildViewInCache(viewCache, back));
            layoutChild(ind, front, frontDistance, recycler, getChildViewInCache(viewCache, front));
        } else {
            layoutChild(ind, front, frontDistance, recycler, getChildViewInCache(viewCache, front));
            layoutChild(ind, back, backDistance, recycler, getChildViewInCache(viewCache, back));
        }
    }

    @Override
    protected void layoutInLoopCompleted(RecyclerView.Recycler recycler, IndexToAttach indexToAttach, SparseArray<View> viewCache) {
        MyIndexToAttach ind = (MyIndexToAttach) indexToAttach;
        int[] toLayoutIndexs = ind.getToLayoutIndexs();
        if (toLayoutIndexs.length > 0) {
            int leaveIndex = toLayoutIndexs[0];
            layoutChild(ind, leaveIndex, 0, recycler, getChildViewInCache(viewCache, leaveIndex));
        }
    }

    private View layoutChild(MyIndexToAttach ind, int nextPosition, double distance, RecyclerView.Recycler recycler, View view) {
        ind.setIndexLayouted(nextPosition);
        if (nextPosition < 0 || nextPosition > getItemCount() - 1) {
            return null;
        }
        if (view == null) {
            view = recycler.getViewForPosition(nextPosition);
            addView(view);
            measureChildWithMargins(view, HORIZONTAL_MARGIN * 2, VERTICAL_MARGIN * 2);
            final LayoutParams lp = (LayoutParams) view.getLayoutParams();
            float left = HORIZONTAL_MARGIN + getPaddingLeft() + lp.leftMargin;
            float top = VERTICAL_MARGIN + getPaddingTop() + lp.topMargin;
            float right = getWidth() - HORIZONTAL_MARGIN - getPaddingRight() - lp.rightMargin;
            float bottom = getHeight() - VERTICAL_MARGIN - getPaddingBottom() - lp.bottomMargin;
            view.layout((int) left, (int) top, (int) right, (int) bottom);
        } else {
            attachView(view);
        }

        final float disAbs = (float) Math.abs(distance);
        float offsetAbs = disAbs / CACHE_VIEW_COUNT;
        if (offsetAbs > 1) {
            offsetAbs = 1;
        }
        float interpolation = decelerateDefault.getInterpolation(offsetAbs);
        float translationX = MAX_OFFSET * interpolation;
        view.setTranslationX(distance > 0 ? translationX : -translationX);
        float scale = 1 - interpolation * (1f - DEFAULT_SCALE);
        view.setScaleX(scale);
        view.setScaleY(scale);
        if (view instanceof FoldableItemLayout) {
            final double interpolationAnother = disAbs < 1 ? decelerateDefault.getInterpolation((1 - disAbs) / CACHE_VIEW_COUNT) : 0;
            ((FoldableItemLayout) view).setFoldRotation((float) distance,
                    interpolation * MAX_OFFSET, interpolationAnother * MAX_OFFSET,
                    scale, 1f - interpolationAnother * (1f - DEFAULT_SCALE));
        }
        return view;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || getItemCount() == 0) {
            return 0;
        }

        int finalScroll = getNowScrollPixels() + dx;
        float toPosition = (float) finalScroll / getItemSize();
        if (toPosition < 0) {
            finalScroll = 0;
            dx = 0;
        } else if (toPosition > getItemCount() - 1) {
            finalScroll = (getItemCount() - 1) * getItemSize();
            dx = 0;
        }
        setNowScrollPixels(finalScroll);
        fillPager(recycler);
        return dx;
    }

    @Override
    public void scrollToPosition(int position) {
        if (position > getItemCount() - 1 || position < 0) {
            Log.e(TAG, "Cannot scroll to " + position + ", item count is " + getItemCount());
            return;
        }
        setNowScrollPixels(position * getItemSize());
        requestLayout();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
        } else {
            return new LayoutParams(lp);
        }
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }
    }

    private class MyIndexToAttach extends IndexToAttach {
        private int[] toLayoutIndexs;

        public MyIndexToAttach(int start, int end, int nowScroll) {
            super(start, end, nowScroll);
            int viewStart = getViewStart();
            int viewEnd = getViewEnd();
            int length = viewEnd - viewStart;
            toLayoutIndexs = new int[length];
            for (int i = 0; i < length; i++) {
                toLayoutIndexs[i] = viewStart + i;
            }
            Arrays.sort(toLayoutIndexs);
            int indexOfZero = Arrays.binarySearch(toLayoutIndexs, 0);
            if (indexOfZero > 0) {
                toLayoutIndexs = Arrays.copyOfRange(toLayoutIndexs, indexOfZero, toLayoutIndexs.length);
            }
        }

        public boolean setIndexLayouted(int layoutIndex) {
            int index = Arrays.binarySearch(toLayoutIndexs, layoutIndex);
            if (index < 0) {
                return false;
            } else {
                int length = toLayoutIndexs.length;
                int[] newLayoutIndexs = new int[length - 1];
                for (int i = 0; i < length - 1; i++) {
                    int oldIndex = i < index ? i : i + 1;
                    newLayoutIndexs[i] = toLayoutIndexs[oldIndex];
                }
                toLayoutIndexs = newLayoutIndexs;
                return true;
            }
        }

        public int[] getToLayoutIndexs() {
            return toLayoutIndexs;
        }

        @Override
        public int getViewIndexByIndex(int index) {
            return 0;
        }

        @Override
        public int getViewEnd() {
            return (int) Math.ceil((double) nowScroll / getItemSize() + start);
        }

        @Override
        public int getViewStart() {
            return (int) Math.ceil((double) nowScroll / getItemSize() - start);
        }

        @Override
        public String toString() {
            return "index:" + getIndex() + ",arr:" + Arrays.toString(toLayoutIndexs);
        }
    }
}
