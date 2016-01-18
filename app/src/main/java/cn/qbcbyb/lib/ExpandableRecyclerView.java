package cn.qbcbyb.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by 秋云 on 2015/6/24.
 */
public class ExpandableRecyclerView extends RecyclerView {
    public interface OnExpandListener {
        void onExpand(ExpandableRecyclerView view, float offset, int top, int bottom);
    }

    private MyAnimation animation;
    private boolean inExpandMode = false;
    private float initDownY = 0;
    private GestureDetector gestureDetector;
    private OnExpandListener onExpandListener;

    public ExpandableRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public ExpandableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (inExpandMode) {
                    clearExpand();
                }
                return true;
            }
        });
    }

    public OnExpandListener getOnExpandListener() {
        return onExpandListener;
    }

    public void setOnExpandListener(OnExpandListener onExpandListener) {
        this.onExpandListener = onExpandListener;
    }

    public void clearExpand() {
        if (animation != null) {
            animation.setInReverse(true);
            startAnimation(animation);
        } else {
            setInExpandMode(false);
            invalidate();
        }
    }

    public boolean isInExpandMode() {
        return inExpandMode;
    }

    public void setInExpandMode(boolean inExpandMode) {
        this.inExpandMode = inExpandMode;
    }

    public void setExpandInIndex(int position, int duration) {
        setInExpandMode(true);
        animation = new MyAnimation(position);
        animation.setDuration(duration);
        startAnimation(animation);
    }

    private void notifyExpand(float offset, int sourceTop, int sourceBottom, int topOffset, int bottomOffset) {
        if (onExpandListener != null) {
            onExpandListener.onExpand(ExpandableRecyclerView.this, offset, sourceTop + topOffset, sourceBottom + bottomOffset);
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return inExpandMode || super.canScrollVertically(direction);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (inExpandMode) {
            return true;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (inExpandMode) {
            gestureDetector.onTouchEvent(e);
            return true;
        } else {
            return super.onTouchEvent(e);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (animation != null && inExpandMode) {
            float offsetY = animation.getTopOffset();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    canvas.save();
                    canvas.translate(child.getLeft(), child.getTop() + offsetY);
                    child.draw(canvas);
                    canvas.restore();
                    if (i == animation.getChildIndex()) {
                        offsetY = animation.getBottomOffset();
                    }
                }
            }
            canvas.drawARGB((int) (0x99 * animation.getInterpolatedTime()), 0, 0, 0);
        } else {
//            for (int i = getChildCount(); i >=0 ; i--) {
//                View child = getChildAt(i);
//                if (child.getVisibility() == VISIBLE) {
//                    canvas.save();
//                    canvas.translate(child.getLeft(), (float) Math.pow(3, i) * getHeight() / 104);
//                    child.draw(canvas);
//                    canvas.restore();
//                }
//            }
            super.dispatchDraw(canvas);
        }
    }

    private class MyAnimation extends Animation implements Animation.AnimationListener {
        private int childIndex = -1;
        private boolean inReverse = false;
        private float interpolatedTime;
        private int topStartPosition = 0;
        private int bottomStartPosition = getMeasuredHeight();

        public MyAnimation(int position) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE &&
                        getChildAdapterPosition(child) == position) {
                    topStartPosition = child.getTop() - 50;
                    bottomStartPosition = getMeasuredHeight() - child.getBottom() - 50;
                    childIndex = i;
                    break;
                }
            }
            setAnimationListener(this);
        }

        public void setInReverse(boolean inReverse) {
            this.inReverse = inReverse;
        }

        public int getChildIndex() {
            return childIndex;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            this.interpolatedTime = inReverse ? (1 - interpolatedTime) : interpolatedTime;
            invalidate();
            View view = getChildAt(childIndex);
            notifyExpand(this.interpolatedTime, view.getTop(), view.getBottom(), (int) getTopOffset(), (int) getBottomOffset());
        }

        public float getInterpolatedTime() {
            return interpolatedTime;
        }

        public float getTopOffset() {
            return -topStartPosition * interpolatedTime;
        }

        public float getBottomOffset() {
            return bottomStartPosition * interpolatedTime;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (inReverse) {
                setInExpandMode(false);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
