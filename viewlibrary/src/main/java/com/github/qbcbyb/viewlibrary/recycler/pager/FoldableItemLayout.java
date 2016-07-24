package com.github.qbcbyb.viewlibrary.recycler.pager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Provides basic functionality for fold animation: splitting view into 2 parts,
 * synchronous rotation of both parts and so on.
 */
public class FoldableItemLayout extends FrameLayout {
    private static final String TAG = FoldableItemLayout.class.getSimpleName();

    private static final int CAMERA_DISTANCE = 90;
    private static final int ANGLE_INTERVAL = 80;//旋转的总角度
    private static final int ANGLE_ROTATION = 50;//初始角度

    private final BaseLayout mBaseLayout;
    private final PartView mLeftPart, mRightPart;

    private int mWidth, mHeight;
    private Bitmap mCacheBitmap;

    private boolean mIsInTransformation;

    private float mOffsetDistance;

    public FoldableItemLayout(Context context) {
        this(context, null);
    }

    public FoldableItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldableItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            setCameraDistance(CAMERA_DISTANCE * getResources().getDisplayMetrics().densityDpi);
        }

        mBaseLayout = new BaseLayout(this);

        mLeftPart = new PartView(this, Gravity.LEFT);
        mRightPart = new PartView(this, Gravity.RIGHT);

        setClipChildren(false);
        setInTransformation(false);
    }

    @Override
    public void setBackground(Drawable background) {
        if (mBaseLayout != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mBaseLayout.setBackground(background);
            } else {
                mBaseLayout.setBackgroundDrawable(background);
            }
        } else {
            super.setBackground(background);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int pivotX = w / 2;
        int pivotY = h / 2;
        setPivotX(pivotX);
        setPivotY(pivotY);
        mLeftPart.setPivotX(pivotX);
        mLeftPart.setPivotY(pivotY);
        mRightPart.setPivotX(pivotX);
        mRightPart.setPivotY(pivotY);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBaseLayout.moveInflatedChildren(this, 3); // skipping mBaseLayout & mLeftPart & mRightPart views
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (mBaseLayout != null) {
            mBaseLayout.moveInflatedChildren(this, 3); // skipping mBaseLayout & mLeftPart & mRightPart views
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mIsInTransformation) {
            ensureCacheBitmap();
        }
        super.dispatchDraw(canvas);
    }

    private void ensureCacheBitmap() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        // Check if correct cache bitmap is already created
        if (mCacheBitmap != null && mCacheBitmap.getWidth() == mWidth
                && mCacheBitmap.getHeight() == mHeight) return;

        if (mCacheBitmap != null) {
            mCacheBitmap.recycle();
            mCacheBitmap = null;
        }

        if (mWidth != 0 && mHeight != 0) {
            try {
                mCacheBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError outOfMemoryError) {
                mCacheBitmap = null;
            }
        }

        applyCacheBitmap(mCacheBitmap);
    }

    private void applyCacheBitmap(Bitmap bitmap) {
        mBaseLayout.setCacheCanvas(bitmap == null ? null : new Canvas(bitmap));
        mLeftPart.setCacheBitmap(bitmap);
        mRightPart.setCacheBitmap(bitmap);
    }

    public void setFoldRotation(float offsetDistance, double interpolation, double interpolationAnother, double scale, double scaleAnother) {
        mOffsetDistance = offsetDistance;
        setInTransformation(mOffsetDistance > -1 && mOffsetDistance < 1);

        if (mIsInTransformation) {
            setRotationY(0);
            final double partViewWidth = getMeasuredWidth() / 2d;
            final double maxLength = interpolation * scale + interpolationAnother * scaleAnother;
            final double partViewAnotherWidth = partViewWidth;// * scaleAnother;
            final double partViewSelfWidth = partViewWidth;//* scale;
            if (offsetDistance > 0) {
                final float rotationAnother = (offsetDistance - 1) * ANGLE_INTERVAL - ANGLE_ROTATION;
                final double anotherLength = Math.abs(Math.cos(rotationAnother * Math.PI / 180d) * partViewAnotherWidth);
                final float rotation = offsetDistance * ANGLE_INTERVAL + ANGLE_ROTATION;
                final double selfLength = Math.abs(Math.cos(rotation * Math.PI / 180d) * partViewAnotherWidth);
                if (anotherLength > maxLength) {
                    if (offsetDistance > 0.5f) {
                        mLeftPart.setVisibility(INVISIBLE);
                    } else {
                        mLeftPart.setVisibility(VISIBLE);
                        mLeftPart.applyFoldRotation(rotation);
                    }
                } else {
                    mLeftPart.setVisibility(VISIBLE);
                    if (offsetDistance > 0.5f) {
                        final double anotherHalfLength = (maxLength - anotherLength) / 2d + anotherLength;
                        float rotationHalf = (float) (Math.acos((maxLength - anotherHalfLength) / partViewSelfWidth) * 180d / Math.PI);
                        mLeftPart.applyFoldRotation(rotationHalf);
                    } else {
                        final double selfHalfLength = (maxLength - selfLength) / 2d + selfLength;
                        float rotationHalf = (float) (Math.acos(selfHalfLength / partViewSelfWidth) * 180d / Math.PI);
                        mLeftPart.applyFoldRotation(rotationHalf);
                    }
                }
                mRightPart.setVisibility(VISIBLE);
                mRightPart.applyFoldRotation(-ANGLE_ROTATION);
            } else {
                final float rotationAnother = (1 + offsetDistance) * ANGLE_INTERVAL + ANGLE_ROTATION;
                final double anotherLength = Math.abs(Math.cos(rotationAnother * Math.PI / 180d) * partViewAnotherWidth);
                final float rotation = offsetDistance * ANGLE_INTERVAL - ANGLE_ROTATION;
                final double selfLength = Math.abs(Math.cos(rotation * Math.PI / 180d) * partViewAnotherWidth);
                if (anotherLength > maxLength) {
                    if (offsetDistance < -.5f) {
                        mRightPart.setVisibility(INVISIBLE);
                    } else {
                        mRightPart.setVisibility(VISIBLE);
                        mRightPart.applyFoldRotation(rotation);
                    }
                } else {
                    mRightPart.setVisibility(VISIBLE);
                    if (offsetDistance < -.5f) {
                        final double anotherHalfLength = (maxLength - anotherLength) / 2d + anotherLength;
                        float rotationHalf = -(float) (Math.acos((maxLength - anotherHalfLength) / partViewSelfWidth) * 180d / Math.PI);
                        mRightPart.applyFoldRotation(rotationHalf);
                    } else {
                        final double selfHalfLength = (maxLength - selfLength) / 2d + selfLength;
                        float rotationHalf = -(float) (Math.acos(selfHalfLength / partViewSelfWidth) * 180d / Math.PI);
                        mRightPart.applyFoldRotation(rotationHalf);
                    }
                }
                mLeftPart.setVisibility(VISIBLE);
                mLeftPart.applyFoldRotation(ANGLE_ROTATION);
            }
        } else if (mOffsetDistance <= -1) {
            setRotationY(ANGLE_ROTATION);
        } else if (mOffsetDistance >= 1) {
            setRotationY(-ANGLE_ROTATION);
        }

        invalidate();
    }

    public float getFoldRotation() {
        return mOffsetDistance;
    }

    private void setInTransformation(boolean isInTransformation) {
        if (mIsInTransformation == isInTransformation) return;
        mIsInTransformation = isInTransformation;

        mBaseLayout.setDrawToCache(isInTransformation);
        mLeftPart.setVisibility(isInTransformation ? VISIBLE : INVISIBLE);
        mRightPart.setVisibility(isInTransformation ? VISIBLE : INVISIBLE);
    }

    public FrameLayout getBaseLayout() {
        return mBaseLayout;
    }

    public void setLayoutVisibleBounds(Rect visibleBounds) {
        mLeftPart.setVisibleBounds(visibleBounds);
        mRightPart.setVisibleBounds(visibleBounds);
    }

    public void setFoldShading(FoldShading shading) {
        mLeftPart.setFoldShading(shading);
        mRightPart.setFoldShading(shading);
    }


    /**
     * View holder layout that can draw itself into given canvas
     */
    private static class BaseLayout extends FrameLayout {

        private Canvas mCacheCanvas;
        private boolean mIsDrawToCache;

        @SuppressWarnings("deprecation")
        private BaseLayout(FoldableItemLayout layout) {
            super(layout.getContext());

            int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
            LayoutParams params = new LayoutParams(matchParent, matchParent);
            layout.addView(this, params);

            // Moving background
            Drawable background = layout.getBackground();
            if (background != null) {
                this.setBackgroundDrawable(background);
                layout.setBackgroundDrawable(null);
            }

            setWillNotDraw(false);
        }

        private void moveInflatedChildren(FoldableItemLayout layout, int firstSkippedItems) {
            while (layout.getChildCount() > firstSkippedItems) {
                View view = layout.getChildAt(firstSkippedItems);
                LayoutParams params = (LayoutParams) view.getLayoutParams();
                layout.removeViewAt(firstSkippedItems);
                addView(view, params);
            }
        }

        @Override
        public void draw(Canvas canvas) {
            if (mIsDrawToCache) {
                if (mCacheCanvas != null) {
                    mCacheCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    super.draw(mCacheCanvas);
                }
            } else {
                super.draw(canvas);
            }
        }

        private void setCacheCanvas(Canvas cacheCanvas) {
            mCacheCanvas = cacheCanvas;
        }

        private void setDrawToCache(boolean drawToCache) {
            if (mIsDrawToCache == drawToCache) return;
            mIsDrawToCache = drawToCache;
            invalidate();
        }

    }

    /**
     * Splat part view. It will draw top or bottom part of cached bitmap and overlay shadows.
     * Also it contains main logic for all transformations (fold rotation, scale, "rolling distance").
     */
    private static class PartView extends View {

        private final int mGravity;

        private Bitmap mBitmap;
        private final Rect mBitmapBounds = new Rect();

        private float mClippingFactor = 0.5f;

        private final Paint mBitmapPaint;

        private Rect mVisibleBounds;

        private int mInternalVisibility;
        private int mExternalVisibility;

        private float mLocalFoldRotation;
        private FoldShading mShading = new SimpleFoldShading();

        public PartView(FoldableItemLayout parent, int gravity) {
            super(parent.getContext());
            mGravity = gravity;

            final int matchParent = LayoutParams.MATCH_PARENT;
            parent.addView(this, new LayoutParams(matchParent, matchParent));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                setCameraDistance(CAMERA_DISTANCE * getResources().getDisplayMetrics().densityDpi);
            }
            mBitmapPaint = new Paint();
            mBitmapPaint.setDither(true);
            mBitmapPaint.setFilterBitmap(true);

            setWillNotDraw(false);
        }

        private void setCacheBitmap(Bitmap bitmap) {
            mBitmap = bitmap;
            calculateBitmapBounds();
        }

        private void setVisibleBounds(Rect visibleBounds) {
            mVisibleBounds = visibleBounds;
            calculateBitmapBounds();
        }

        private void setFoldShading(FoldShading shading) {
            mShading = shading;
        }

        private void calculateBitmapBounds() {
            if (mBitmap == null) {
                mBitmapBounds.set(0, 0, 0, 0);
            } else {
                int h = mBitmap.getHeight();
                int w = mBitmap.getWidth();

                int left = mGravity == Gravity.LEFT ? 0 : (int) (w * (1 - mClippingFactor) - 0.5f);
                int right = mGravity == Gravity.LEFT ? (int) (w * mClippingFactor + 0.5f) : w;

                mBitmapBounds.set(left, 0, right, h);
                if (mVisibleBounds != null) {
                    if (!mBitmapBounds.intersect(mVisibleBounds)) {
                        mBitmapBounds.set(0, 0, 0, 0); // no intersection
                    }
                }
            }

            invalidate();
        }

        private void applyFoldRotation(float rotation) {

            setRotationY(rotation);

            mInternalVisibility = VISIBLE;// isVisible ? VISIBLE : INVISIBLE;
            applyVisibility();

            mLocalFoldRotation = rotation;

            invalidate(); // needed to draw shadow overlay
        }

        @Override
        public void setVisibility(int visibility) {
            mExternalVisibility = visibility;
            applyVisibility();
        }

        private void applyVisibility() {
            super.setVisibility(mExternalVisibility == VISIBLE ? mInternalVisibility : mExternalVisibility);
        }

        @SuppressLint("MissingSuperCall")
        @Override
        public void draw(Canvas canvas) {
            if (mShading != null)
                mShading.onPreDraw(canvas, mBitmapBounds, mLocalFoldRotation, mGravity);
            if (mBitmap != null)
                canvas.drawBitmap(mBitmap, mBitmapBounds, mBitmapBounds, mBitmapPaint);
            if (mShading != null)
                mShading.onPostDraw(canvas, mBitmapBounds, mLocalFoldRotation, mGravity);
        }

    }

}
