package com.github.qbcbyb.viewlibrary.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.List;

/**
 * Created by 秋云 on 2015/8/5.
 */
public abstract class BaseLayoutManager extends RecyclerView.LayoutManager implements ISnappyLayoutManager {
    protected static final String TAG = BaseLayoutManager.class.getSimpleName();
    private static final int INTERVAL = 4;
    public static final int SMOOTH_SCROLL_DURATION_NORMAL = 500;

    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;

    protected FlingCalculater flingCalculater = new FlingCalculater();

    private int nowScrollPixels = 0;

    private boolean isVertical() {
        return canScrollVertically();
    }

    public abstract int getItemSize();

    public int getNowScrollPixels() {
        return nowScrollPixels;
    }

    protected void setNowScrollPixels(int nowScrollPixels) {
        this.nowScrollPixels = nowScrollPixels;
    }

    /**
     * @return the index of the first visible item
     */
    public int getNowPosition() {
        return getNowScrollPixels() / getItemSize();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        Context context = view.getContext();
        flingCalculater.calculateDeceleration(context);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        detachAndScrapAttachedViews(recycler);
        fillPager(recycler);
    }

    protected void fillPager(RecyclerView.Recycler recycler) {
        IndexToAttach indexToAttach = computeLayoutIndex();
        final int viewStart = indexToAttach.getViewStart();
        final int viewEnd = indexToAttach.getViewEnd();

        SparseArray<View> viewCache = new SparseArray<>(getChildCount());
        if (getChildCount() != 0) {

            //Cache all views by their existing position, before updating counts
            for (int i = getChildCount() - 1; i > -1; i--) {
                final View child = getChildAt(i);
                final int position = getPosition(child);
                if ((position - viewEnd) * (position - viewStart) > 0) {
                    detachAndScrapView(child, recycler);
                } else {
                    viewCache.put(position, child);
                    detachView(child);
                }
                resetViewMatrix(child);
            }
        }
        while (indexToAttach.moveToNext()) {
            layoutChildFromViewCache(recycler, indexToAttach, viewCache);
        }
        layoutInLoopCompleted(recycler, indexToAttach, viewCache);

        for (int i = 0; i < viewCache.size(); i++) {
            final View removingView = viewCache.valueAt(i);
            removeAndRecycleView(removingView, recycler);
        }
        final List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
        for (int i = 0; i < scrapList.size(); i++) {
            final View removingView = scrapList.get(i).itemView;
            removeAndRecycleView(removingView, recycler);
        }
    }

    protected final View getChildViewInCache(SparseArray<View> viewCache, int k) {
        View view = viewCache.get(k);
        if (view != null) {
            viewCache.remove(k);
        }
        return view;
    }

    protected void layoutChildFromViewCache(RecyclerView.Recycler recycler, IndexToAttach indexToAttach, SparseArray<View> viewCache) {
        final int k = indexToAttach.getViewIndex();
        View view = getChildViewInCache(viewCache, k);
        layoutChildInToLayout(k, indexToAttach.nowScroll, recycler, view);
    }

    protected void layoutInLoopCompleted(RecyclerView.Recycler recycler, IndexToAttach indexToAttach, SparseArray<View> viewCache) {

    }

    protected abstract IndexToAttach computeLayoutIndex();

    protected void layoutChildInToLayout(int nextPosition, int nowScroll, RecyclerView.Recycler recycler, View view) {
    }

    protected void resetViewMatrix(View child) {
        child.setTranslationX(0);
        child.setTranslationY(0);
        child.setScaleX(1);
        child.setScaleY(1);
        child.setRotation(0);
        child.setRotationX(0);
        child.setRotationY(0);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        smoothScrollToPositionInternal(position, SMOOTH_SCROLL_DURATION_NORMAL, false);
    }

    private void smoothScrollToPositionInternal(int finalPosition, int duration, boolean decelerate) {
        RecyclerView.SmoothScroller scroller = new BaseSmoothScroller(this, finalPosition, duration, decelerate);
        startSmoothScroll(scroller);
    }

    @Override
    public void smoothScrollToNearestPosition(int velocityX, int velocityY) {
        if (this.getChildCount() == 0) {
            return;
        }
        int offset = getNowScrollPixels() % getItemSize();
        int toPosition = getNowScrollPixels() / getItemSize();
        if (canScrollHorizontally()) {
            if (velocityX > 0 && offset * INTERVAL > getItemSize()) {
                toPosition += 1;
            } else if (velocityX < 0 && offset * INTERVAL / (INTERVAL - 1) > getItemSize()) {
                toPosition += 1;
            }
        } else {
            if (velocityY > 0 && offset * INTERVAL > getItemSize()) {
                toPosition += 1;
            } else if (velocityY < 0 && offset * INTERVAL / (INTERVAL - 1) > getItemSize()) {
                toPosition += 1;
            }
        }
        smoothScrollToPositionInternal(toPosition, SMOOTH_SCROLL_DURATION_NORMAL, true);
    }

    @Override
    public boolean fling(RecyclerView recyclerView, int velocityX, int velocityY) {
        if (getChildCount() == 0) {
            return false;
        }

        final boolean canScrollHorizontal = canScrollHorizontally();
        final boolean canScrollVertical = canScrollVertically();

        if (!canScrollHorizontal || Math.abs(velocityX) < mMinFlingVelocity) {
            velocityX = 0;
        }
        if (!canScrollVertical || Math.abs(velocityY) < mMinFlingVelocity) {
            velocityY = 0;
        }
        if (velocityX == 0 && velocityY == 0) {
            // If we don't have any velocity, return false
            return false;
        }

        if (!recyclerView.dispatchNestedPreFling(velocityX, velocityY)) {
            final boolean canScroll = canScrollHorizontal || canScrollVertical;
            recyclerView.dispatchNestedFling(velocityX, velocityY, canScroll);

            if (canScroll) {
                velocityX = Math.max(-mMaxFlingVelocity, Math.min(velocityX, mMaxFlingVelocity));
                velocityY = Math.max(-mMaxFlingVelocity, Math.min(velocityY, mMaxFlingVelocity));
                return flingInternal(velocityX, velocityY);
            }
        }
        return false;
    }

    private boolean flingInternal(int velocityX, int velocityY) {
        double offset = calcOffsetForVelocity(velocityX, velocityY);
        double finalPosition = (offset + getNowScrollPixels()) / getItemSize();
        if (finalPosition <= 0 || finalPosition >= getItemCount() - 1) {
            return false;
        }
        if (Math.abs(offset) * 2 < getItemSize()) {
            smoothScrollToNearestPosition(velocityX, velocityY);
        } else {
            int toPosition = (int) Math.round(finalPosition);
            int duration = calcDurForVelocity(velocityX, velocityY);
            smoothScrollToPositionInternal(toPosition, duration, true);
        }
        return true;
    }

    private int calcDurForVelocity(int velocityX, int velocityY) {
        int velocity = isVertical() ? velocityY : velocityX;
        return flingCalculater.getSplineFlingDuration(velocity);
    }

    private double calcOffsetForVelocity(int velocityX, int velocityY) {
        int velocity = isVertical() ? velocityY : velocityX;

        final double dist = flingCalculater.getSplineFlingDistance(velocity);

        return (velocity > 0 ? dist : -dist);
    }

    static class BaseSmoothScroller extends RecyclerView.SmoothScroller {
        private final DecelerateInterpolator mOnFoundInterpolator = new DecelerateInterpolator(2.5f);

        private final Interpolator mOnSeekInterpolator;
        private final BaseLayoutManager baseLayoutManager;
        private final int duration;
        private int initFinalDx;
        private int initFinalDy;

        public BaseSmoothScroller(BaseLayoutManager baseLayoutManager, int targetPosition, int duration, boolean decelerateAllTheTime) {
            this.baseLayoutManager = baseLayoutManager;
            this.duration = duration < 1 ? 1 : duration;
            if (decelerateAllTheTime) {
                mOnSeekInterpolator = mOnFoundInterpolator;
            } else {
                mOnSeekInterpolator = new LinearInterpolator();
            }
            setTargetPosition(targetPosition);
        }

        @Override
        protected void onStart() {
        }

        @Override
        protected void onStop() {
            initFinalDx = initFinalDy = 0;
        }

        @Override
        protected void onSeekTargetStep(int dx, int dy, RecyclerView.State state, Action action) {
            initFinalDx = clampApplyScroll(initFinalDx, dx);
            initFinalDy = clampApplyScroll(initFinalDy, dy);
            if (initFinalDx == 0 && initFinalDy == 0) {
                initFinal();
                action.update(initFinalDx, initFinalDy, duration, mOnSeekInterpolator);
            }
        }

        @Override
        protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
            if ((initFinalDx == 0 && initFinalDy == 0) || action.getInterpolator() != mOnFoundInterpolator) {
                initFinal();
                action.update(initFinalDx, initFinalDy, duration, mOnFoundInterpolator);
            }
        }

        private void initFinal() {
            int finalPixel = getFinal();
            boolean vertical = baseLayoutManager.isVertical();
            if (vertical) {
                initFinalDx = 0;
                initFinalDy = finalPixel;
            } else {
                initFinalDx = finalPixel;
                initFinalDy = 0;
            }
        }

        protected int getFinal() {
            return getTargetPosition() * baseLayoutManager.getItemSize() - baseLayoutManager.getNowScrollPixels();
        }

        private int clampApplyScroll(int tmpDt, int dt) {
            final int before = tmpDt;
            tmpDt -= dt;
            if (before * tmpDt <= 0) { // changed sign, reached 0 or was 0, reset
                return 0;
            }
            return tmpDt;
        }
    }

    protected abstract class IndexToAttach {
        public final int nowScroll;
        public final int start;
        public final int end;
        public final int step;
        private int index;

        public IndexToAttach(int start, int end, int nowScroll) {
            this.nowScroll = nowScroll;
            this.start = start;
            this.end = end;
            this.step = start > end ? -1 : 1;
            index = start - step;
        }

        public boolean moveToFirst() {
            index = start;
            return checkIndexOK();
        }

        public boolean moveToLast() {
            index = end;
            return checkIndexOK();
        }

        public boolean moveToNext() {
            index += step;
            return checkIndexOK();
        }

        protected boolean checkIndexOK() {
            return (index - start) * (index - end) <= 0;
        }

        public int getIndex() {
            return index;
        }

        public abstract int getViewIndexByIndex(int index);

        public int getViewIndex() {
            return getViewIndexByIndex(index);
        }

        public int getViewStart() {
            return getViewIndexByIndex(start);
        }

        public int getViewEnd() {
            return getViewIndexByIndex(end);
        }

    }
}
