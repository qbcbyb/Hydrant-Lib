package com.github.qbcbyb.viewlibrary.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 秋云 on 2015/7/28.
 */
public class ResizeLayoutManager extends BaseLayoutManager {
    private static final String TAG = ResizeLayoutManager.class.getSimpleName();
    private int offsetBottom;

    public interface ResizeChildView {
        void onResizeScrollChanged(int index, float scrollRate, int height);
    }

    private int childHeightMax = 240;
    private int childHeightMin = 120;

    public ResizeLayoutManager(int childHeightMin, int childHeightMax) {
        this.childHeightMin = childHeightMin;
        this.childHeightMax = childHeightMax;
    }

    @Override
    public int getItemSize() {
        return childHeightMax;
    }

    public void measureChildCustom(View child, int heightDimension) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

        final int widthSpec = getChildMeasureSpec(getWidth(),
                getPaddingLeft() + getPaddingRight(), -1,
                canScrollHorizontally());
        final int heightSpec = getChildMeasureSpec(getHeight(),
                getPaddingTop() + getPaddingBottom(), heightDimension,
                canScrollVertically());
        child.measure(widthSpec, heightSpec);
    }

    @Override
    protected IndexToAttach computeLayoutIndex() {
        offsetBottom = 0;
        int start = getNowPosition();
        int end = getItemCount() - 1;
        return new IndexToAttach(start, end, getNowScrollPixels()) {
            @Override
            public int getViewIndexByIndex(int index) {
                return index;
            }

            @Override
            protected boolean checkIndexOK() {
                if (offsetBottom > getHeight() + 100) {
                    return false;
                }
                return super.checkIndexOK();
            }
        };
    }

    @Override
    protected void layoutChildInToLayout(int nextPosition, int nowScroll, RecyclerView.Recycler recycler, View view) {
        int nextScroll = nextPosition * getItemSize() - nowScroll;
        if (nextScroll < 0) {
            offsetBottom = nextScroll;
        }

        if (nextPosition < 0 || nextPosition > getItemCount() - 1) {
            return;
        }
        if (nextScroll < 0) {
            nextScroll = 0;
        }

        if (view == null) {
            view = recycler.getViewForPosition(nextPosition);
            addView(view);
            measureChildCustom(view, childHeightMax);
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int right = left + view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();
            view.layout(left, top, right, bottom);
        } else {
            attachView(view);
        }

        final int height = (nextScroll < getItemSize() ? Math.round(childHeightMax - (childHeightMax - childHeightMin) * nextScroll / getItemSize()) : childHeightMin);
        final int nextTop = offsetBottom + height;

        view.setTop(offsetBottom);
        view.setBottom(nextTop);

        if (view instanceof ResizeChildView) {
            ((ResizeChildView) view).onResizeScrollChanged(nextPosition, nextScroll < getItemSize() ? ((float) nextScroll / getItemSize()) : 1, height);
        }

        offsetBottom = nextTop;
    }

    @Override
    public int computeVerticalScrollExtent(RecyclerView.State state) {
        return 100;
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        return getNowScrollPixels() * 100 / getItemSize();
    }

    @Override
    public int computeVerticalScrollRange(RecyclerView.State state) {
        return getItemCount() * 100;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || getItemCount() == 0) {
            return 0;
        }
        int finalScroll = getNowScrollPixels() + dy;

        float toPosition = (float) finalScroll / getItemSize();
        if (toPosition < 0) {
            finalScroll = 0;
            dy = 0;
        } else if (toPosition > getItemCount() - 1) {
            finalScroll = (getItemCount() - 1) * getItemSize();
            dy = 0;
        }
        setNowScrollPixels(finalScroll);
        fillPager(recycler);
        return dy;
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
                ViewGroup.LayoutParams.WRAP_CONTENT);
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

        public LayoutParams(LayoutParams source) {
            super(source);
        }
    }

}
