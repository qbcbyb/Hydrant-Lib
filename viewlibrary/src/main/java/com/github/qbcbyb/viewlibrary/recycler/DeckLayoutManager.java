package com.github.qbcbyb.viewlibrary.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by 秋云 on 2015/7/28.
 */
public class DeckLayoutManager extends BaseLayoutManager {
    private static final String TAG = DeckLayoutManager.class.getSimpleName();

    public interface DeckChildView {
        void onDeckScrollChanged(int index, float scrollRate, int height);
    }

    private static final int CACHE_VIEW_COUNT = 8;
    private static final float START_SCROLL_INDEX = 1.2f;//the value is ( first item's start position / item's height )
    private int offsetTop = 0;
    public static final float DEFAULT_SCALE = 0.8f;

    private AccelerateInterpolator interpolatorDefault = new AccelerateInterpolator(2f);

    public DeckLayoutManager() {
        this(0);
    }

    public DeckLayoutManager(int offsetTop) {
        this.offsetTop = offsetTop;
    }

    @Override
    public int getItemSize() {
        return getHeight() / CACHE_VIEW_COUNT;
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

        final int widthSpec = getChildMeasureSpec(getWidth(),
                getPaddingLeft() + getPaddingRight() +
                        lp.leftMargin + lp.rightMargin + widthUsed, lp.width,
                canScrollHorizontally());
        final int heightSpec = getChildMeasureSpec(getHeight(),
                getPaddingTop() + getPaddingBottom() +
                        lp.topMargin + lp.bottomMargin + heightUsed, lp.height,
                canScrollVertically());
        child.measure(widthSpec, heightSpec);
    }

    @Override
    protected IndexToAttach computeLayoutIndex() {
        final int nowScroll = (int) (getNowScrollPixels() - (CACHE_VIEW_COUNT - START_SCROLL_INDEX) * getItemSize());
        return new IndexToAttach(-1, CACHE_VIEW_COUNT, nowScroll) {
            @Override
            public int getViewIndexByIndex(int index) {
                return (nowScroll / getItemSize() + index + (nowScroll % getItemSize() > 0 ? 1 : 0));
            }
        };
    }

    @Override
    protected void layoutChildInToLayout(int nextPosition, int nowScroll, RecyclerView.Recycler recycler, View view) {
        if (nextPosition <= -1 || nextPosition > getItemCount() - 1) {
            return;
        }
        if (nextPosition < 0) {
            nextPosition = 0;
        }

        final int measuredHeight;
        if (view == null) {
            view = recycler.getViewForPosition(nextPosition);
            addView(view);

            measureChildWithMargins(view, 0, offsetTop);
            measuredHeight = view.getMeasuredHeight();
            final LayoutParams lp = (LayoutParams) view.getLayoutParams();
            float left = getPaddingLeft() + lp.leftMargin;
            float top = offsetTop + getPaddingTop() + lp.topMargin;
            float right = left + view.getMeasuredWidth();
            float bottom = top + measuredHeight;
            view.layout((int) left, (int) top, (int) right, (int) bottom);
        } else {
            attachView(view);
            measuredHeight = view.getMeasuredHeight();
        }

        float distance = nextPosition - ((float) nowScroll / getItemSize());
        float offsetAbsNext = getOffsetAbs(distance + 1);
        float offsetAbs = getOffsetAbs(distance);

        float interpolation = interpolatorDefault.getInterpolation(offsetAbs);
        final float interpolationNext = interpolatorDefault.getInterpolation(offsetAbsNext);
        float scale = DEFAULT_SCALE + interpolation * (1f - DEFAULT_SCALE);
        double marginMax = getHeight() - offsetTop;
        final float translationY = (float) (marginMax * interpolation);
        final float nextTranslationY = (float) (marginMax * interpolationNext);
//        view.setTranslationY(translationY);
        view.setPivotX(getWidth() / 2);
        view.setPivotY(0f);
        view.setScaleX(scale);
        view.setScaleY(scale);
        final int offsetVertical = offsetTop + getPaddingTop() + ((LayoutParams) view.getLayoutParams()).topMargin;
        final int top = (int) translationY + offsetVertical;
        final float scaledHeight = nextTranslationY - translationY;
        view.setTop(top);
        final int realHeight = Math.round(scaledHeight / scale);
        view.setBottom(top + (realHeight < measuredHeight ? realHeight : measuredHeight));
        if (view instanceof DeckChildView) {
            ((DeckChildView) view).onDeckScrollChanged(nextPosition, (1f - interpolationNext), Math.round(scaledHeight));
        }
    }

    private float getOffsetAbs(float distance) {
        if (distance < 0) {
            distance = 0;
        }
        float offsetAbs = distance / CACHE_VIEW_COUNT;
        if (offsetAbs > 1) {
            offsetAbs = 1;
        }
        return offsetAbs;
    }

    @Override
    public int computeVerticalScrollExtent(RecyclerView.State state) {
        return CACHE_VIEW_COUNT * 100;
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        return getNowScrollPixels() * 100 / getItemSize();
    }

    @Override
    public int computeVerticalScrollRange(RecyclerView.State state) {
        return (getItemCount() + CACHE_VIEW_COUNT - (int) START_SCROLL_INDEX) * 100;
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
