package cn.qbcbyb.lib.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by 秋云 on 2015/7/3.
 */
public class DeckChildView extends FrameLayout implements View.OnClickListener {
    DeckViewConfig mConfig;

    private int mKey = -1;
    float mTaskProgress;
    ObjectAnimator mTaskProgressAnimator;
    boolean mIsFocused;
    boolean mFocusAnimationsEnabled;
    boolean mClipViewInStack;

    ValueAnimator.AnimatorUpdateListener mUpdateDimListener =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setTaskProgress((Float) animation.getAnimatedValue());
                }
            };

    public DeckChildView(Context context) {
        this(context, null);
    }

    public DeckChildView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeckChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mConfig = DeckViewConfig.getInstance();
    }

    public int getAttachedKey() {
        return mKey;
    }

    public void onTaskBound(int key) {
        mKey = key;
    }

    private boolean isBound() {
        return mKey != -1;
    }

    /**
     * Binds this task view to the task
     */
    public void onTaskUnbound() {
        mKey = -1;
    }

    public void startNoUserInteractionAnimation() {

    }

    public void setNoUserInteractionState() {

    }

    /**
     * Enables/disables handling touch on this task view.
     */
    public void setTouchEnabled(boolean enabled) {
        setOnClickListener(enabled ? this : null);
    }

    public void reset() {

    }

    /**
     * Returns whether this view should be clipped, or any views below should clip against this
     * view.
     */
    boolean shouldClipViewInStack() {
        return mClipViewInStack && (getVisibility() == View.VISIBLE);
    }

    /**
     * Sets whether this view should be clipped, or clipped against.
     */
    public void setClipViewInStack(boolean clip) {
        if (clip != mClipViewInStack) {
            mClipViewInStack = clip;
//            if (mCb != null) {
//                mCb.onDeckChildViewClipStateChanged(this);
//            }
        }
    }

    /**
     * Sets the current task progress.
     */
    public void setTaskProgress(float p) {
        mTaskProgress = p;
    }

    /**
     * Returns the current task progress.
     */
    public float getTaskProgress() {
        return mTaskProgress;
    }

    public void setFocusedTask(boolean animateFocusedState) {
        mIsFocused = true;
        // Workaround, we don't always want it focusable in touch mode, but we want the first task
        // to be focused after the enter-recents animation, which can be triggered from either touch
        // or keyboard
        setFocusableInTouchMode(true);
        requestFocus();
        setFocusableInTouchMode(false);
        invalidate();
    }

    /**
     * Unsets the focused task explicitly.
     */
    void unsetFocusedTask() {
        mIsFocused = false;
        invalidate();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!gainFocus) {
            unsetFocusedTask();
        }
    }

    /**
     * Returns whether we have explicitly been focused.
     */
    public boolean isFocusedTask() {
        return mIsFocused || isFocused();
    }

    /**
     * Enables all focus animations.
     */
    void enableFocusAnimations() {
        boolean wasFocusAnimationsEnabled = mFocusAnimationsEnabled;
        mFocusAnimationsEnabled = true;
    }

    void dismissTask() {
        // Animate out the view and call the callback
        final DeckChildView tv = this;
        startDeleteTaskAnimation(new Runnable() {
            @Override
            public void run() {
//                if (mCb != null) {
//                    mCb.onDeckChildViewDismissed(tv);
//                }
            }
        });
    }

    /**
     * Animates the deletion of this task view
     */
    void startDeleteTaskAnimation(final Runnable r) {
        // Disabling clipping with the stack while the view is animating away
        setClipViewInStack(false);

        animate().translationX(mConfig.taskViewRemoveAnimTranslationXPx)
                .alpha(0f)
                .setStartDelay(0)
                .setUpdateListener(null)
                .setInterpolator(mConfig.fastOutSlowInInterpolator)
                .setDuration(mConfig.taskViewRemoveAnimDuration)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // We just throw this into a runnable because starting a view property
                        // animation using layers can cause inconsisten results if we try and
                        // update the layers while the animation is running.  In some cases,
                        // the runnabled passed in may start an animation which also uses layers
                        // so we defer all this by posting this.
                        r.run();

                        // Re-enable clipping with the stack (we will reuse this view)
                        setClipViewInStack(true);
                    }
                })
                .start();
    }

    /**
     * Synchronizes this view's properties with the task's transform
     */
    void updateViewPropertiesToTaskTransform(DeckChildViewTransform toTransform, int duration) {
        updateViewPropertiesToTaskTransform(toTransform, duration, null);
    }

    void updateViewPropertiesToTaskTransform(DeckChildViewTransform toTransform, int duration,
                                             ValueAnimator.AnimatorUpdateListener updateCallback) {
        // Apply the transform
        toTransform.applyToTaskView(this, duration, mConfig.fastOutSlowInInterpolator, false,
                !mConfig.fakeShadows, updateCallback);

        // Update the task progress
        DVUtils.cancelAnimationWithoutCallbacks(mTaskProgressAnimator);
        if (duration <= 0) {
            setTaskProgress(toTransform.p);
        } else {
            mTaskProgressAnimator = ObjectAnimator.ofFloat(this, "taskProgress", toTransform.p);
            mTaskProgressAnimator.setDuration(duration);
            mTaskProgressAnimator.addUpdateListener(mUpdateDimListener);
            mTaskProgressAnimator.start();
        }
    }

    /**
     * Resets this view's properties
     */
    void resetViewProperties() {
//        setDim(0);
        setLayerType(View.LAYER_TYPE_NONE, null);
        DeckChildViewTransform.reset(this);
    }

    /**
     * When we are un/filtering, this method will set up the transform that we are animating to,
     * in order to hide the task.
     */
    void prepareTaskTransformForFilterTaskHidden(DeckChildViewTransform toTransform) {
        // Fade the view out and slide it away
        toTransform.alpha = 0f;
        toTransform.translationY += 200;
        toTransform.translationZ = 0;
    }

    /**
     * When we are un/filtering, this method will setup the transform that we are animating from,
     * in order to show the task.
     */
    void prepareTaskTransformForFilterTaskVisible(DeckChildViewTransform fromTransform) {
        // Fade the view in
        fromTransform.alpha = 0f;
    }

    /**
     * Prepares this task view for the enter-recents animations.  This is called earlier in the
     * first layout because the actual animation into recents may take a long time.
     */
    void prepareEnterRecentsAnimation(boolean isTaskViewLaunchTargetTask,
                                      boolean occludesLaunchTarget, int offscreenY) {
//        int initialDim = getDim();
        if (mConfig.launchedHasConfigurationChanged) {
            // Just load the views as-is
        } else if (mConfig.launchedFromAppWithThumbnail) {
            if (isTaskViewLaunchTargetTask) {
                // Set the dim to 0 so we can animate it in
//                initialDim = 0;
            } else if (occludesLaunchTarget) {
                // Move the task view off screen (below) so we can animate it in
                setTranslationY(offscreenY);
            }

        } else if (mConfig.launchedFromHome) {
            // Move the task view off screen (below) so we can animate it in
            setTranslationY(offscreenY);
//            setTranslationZ(0);
            setScaleX(1f);
            setScaleY(1f);
        }
    }

    /**
     * Animates this task view as it enters recents
     */
    void startEnterRecentsAnimation(final ViewAnimation.TaskViewEnterContext ctx) {
        Log.i(getClass().getSimpleName(), "startEnterRecentsAnimation");
        final DeckChildViewTransform transform = ctx.currentTaskTransform;
        int startDelay = 0;

        if (mConfig.launchedFromHome) {
            Log.i(getClass().getSimpleName(), "mConfig.launchedFromHome false");
            // Animate the tasks up
            int frontIndex = (ctx.currentStackViewCount - ctx.currentStackViewIndex - 1);
            int delay = mConfig.transitionEnterFromHomeDelay +
                    frontIndex * mConfig.taskViewEnterFromHomeStaggerDelay;

            setScaleX(transform.scale);
            setScaleY(transform.scale);
            if (!mConfig.fakeShadows) {
//                animate().translationZ(transform.translationZ);
            }
            animate()
                    .translationY(transform.translationY)
                    .setStartDelay(delay)
                    .setUpdateListener(ctx.updateListener)
                    .setInterpolator(mConfig.quintOutInterpolator)
                    .setDuration(mConfig.taskViewEnterFromHomeDuration +
                            frontIndex * mConfig.taskViewEnterFromHomeStaggerDelay)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            // Decrement the post animation trigger
                            ctx.postAnimationTrigger.decrement();
                        }
                    })
                    .start();
            ctx.postAnimationTrigger.increment();
            startDelay = delay;
        }

        // Enable the focus animations from this point onwards so that they aren't affected by the
        // window transitions
        postDelayed(new Runnable() {
            @Override
            public void run() {
                enableFocusAnimations();
            }
        }, startDelay);
    }

    /**
     * Animates this task view as it leaves recents by pressing home.
     */
    void startExitToHomeAnimation(ViewAnimation.TaskViewExitContext ctx) {
        animate()
                .translationY(ctx.offscreenTranslationY)
                .setStartDelay(0)
                .setUpdateListener(null)
                .setInterpolator(mConfig.fastOutLinearInInterpolator)
                .setDuration(mConfig.taskViewExitToHomeDuration)
                .withEndAction(ctx.postAnimationTrigger.decrementAsRunnable())
                .start();
        ctx.postAnimationTrigger.increment();
    }

    /**
     * Animates this task view as it exits recents
     */
    void startLaunchTaskAnimation(final Runnable postAnimRunnable, boolean isLaunchingTask,
                                  boolean occludesLaunchTarget, boolean lockToTask) {
        if (!isLaunchingTask) {
            // If this is another view in the task grouping and is in front of the launch task,
            // animate it away first
            if (occludesLaunchTarget) {
                animate().alpha(0f)
                        .translationY(getTranslationY() + mConfig.taskViewAffiliateGroupEnterOffsetPx)
                        .setStartDelay(0)
                        .setUpdateListener(null)
                        .setInterpolator(mConfig.fastOutLinearInInterpolator)
                        .setDuration(mConfig.taskViewExitToAppDuration)
                        .start();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
