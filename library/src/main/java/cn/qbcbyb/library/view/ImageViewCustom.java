package cn.qbcbyb.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import cn.qbcbyb.library.R;

public class ImageViewCustom extends ImageView {

    public ImageViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ImageViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public ImageViewCustom(Context context) {
        super(context);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewCustom);
        try {
            widthDividedHeight = a.getFloat(R.styleable.ImageViewCustom_widthDividedHeight, 0);
        } finally {
            a.recycle();
        }
    }

    private float widthDividedHeight = 0f;

    public float getWidthDividedHeight() {
        return widthDividedHeight;
    }

    public void setWidthDividedHeight(float widthDividedHeight) {
        this.widthDividedHeight = widthDividedHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getLayoutParams() != null) {
            if (widthDividedHeight == 0) {
                if (this.getDrawable() != null) {
                    try {
                        widthDividedHeight = (float) this.getDrawable().getIntrinsicWidth() / this.getDrawable().getIntrinsicHeight();
                    } catch (NullPointerException e) {
                        widthDividedHeight = 0;
                    }
                }
            }
            if (widthDividedHeight != 0) {
//                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
                    int w = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();//getMeasuredWidth()
                    int h = (int) (w / widthDividedHeight) + getPaddingTop() + getPaddingBottom();
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
                } else if (getLayoutParams().width == LayoutParams.WRAP_CONTENT) {
                    int h = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();//getMeasuredHeight()
                    int w = (int) (h * widthDividedHeight) + getPaddingLeft() + getPaddingRight();
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
