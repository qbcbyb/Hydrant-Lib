package cn.qbcbyb.library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qbcbyb.library.R;


/**
 * Created by 秋云 on 2014/8/25.
 */
public class MetroItemView extends RelativeLayout {

    public enum ViewLayoutMode {
        Normal, Overlay
    }

    private ImageViewCustom imageView;
    private TextView titleView;
    private int imageResource = 0;
    private Integer workId = 0;
    private ViewLayoutMode viewLayoutMode;

    public MetroItemView(Context context) {
        super(context);
        initViews(context, null);
    }

    public MetroItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        this.imageView = new ImageViewCustom(context);
        this.titleView = new TextView(context);
        this.imageView.setId(android.R.id.icon);
        int titleHeightDefault = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, getResources().getDisplayMetrics());
        LayoutParams textLayout = new LayoutParams(-1, titleHeightDefault);
        this.addView(this.imageView, new LayoutParams(-1, -2));
        textLayout.addRule(ALIGN_PARENT_BOTTOM);
        this.addView(this.titleView, textLayout);
        this.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.titleView.setGravity(Gravity.CENTER);
        this.titleView.setLines(1);
        this.titleView.setVisibility(GONE);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MetroItemView);
            try {
                viewLayoutMode = ViewLayoutMode.values()[a.getInt(R.styleable.MetroItemView_viewLayoutMode, 0)];
                textLayout.addRule(BELOW, viewLayoutMode == ViewLayoutMode.Normal ? android.R.id.icon : -1);
                int titleHeight = a.getDimensionPixelSize(R.styleable.MetroItemView_titleHeight, Integer.MIN_VALUE);
                if (titleHeight != Integer.MIN_VALUE) {
                    setTitleHeight(titleHeight);
                }
                setWorkId(a.getInt(R.styleable.MetroItemView_workId, 0));
                int drawableId = a.getResourceId(R.styleable.MetroItemView_android_src, 0);
                if (drawableId != 0) {
                    setImageResource(drawableId);
                }
                Drawable drawable = a.getDrawable(R.styleable.MetroItemView_textBackground);
                if (drawable != null) {
                    setTextBackground(drawable);
                }
                setText(a.getText(R.styleable.MetroItemView_android_text));
                setTextColor(a.getColor(R.styleable.MetroItemView_android_textColor, Color.BLACK));
                setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.MetroItemView_android_textSize, 14));
            } finally {
                a.recycle();
            }
        }
    }

    public void setViewLayoutMode(ViewLayoutMode viewLayoutMode) {
        this.viewLayoutMode = viewLayoutMode;
        if (this.titleView != null) {
            LayoutParams textLayout = (LayoutParams) this.titleView.getLayoutParams();
            textLayout.addRule(BELOW, viewLayoutMode == ViewLayoutMode.Normal ? android.R.id.icon : -1);
        }
    }

    public void setTextBackground(Drawable textBackground) {
        if (this.titleView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.titleView.setBackground(textBackground);
            } else {
                this.titleView.setBackgroundDrawable(textBackground);
            }
        }
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public void setImageResource(int resId) {
        imageView.setImageResource(resId);
        this.imageResource = resId;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleView.getLayoutParams().height=titleHeight;
        if(!this.titleView.isLayoutRequested()){
            this.titleView.requestLayout();
        }
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageURI(Uri uri) {
        imageView.setImageURI(uri);
    }

    public void setImageDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    public void setImageBitmap(Bitmap bm) {
        imageView.setImageBitmap(bm);
    }

    public Drawable getDrawable() {
        return imageView.getDrawable();
    }

    public void setText(int resid) {
        titleView.setText(resid);
    }

    public void setText(CharSequence text) {
        titleView.setText(text);
        if (text != null) {
            this.titleView.setVisibility(VISIBLE);
        } else {
            this.titleView.setVisibility(GONE);
        }
    }

    @ViewDebug.CapturedViewProperty
    public CharSequence getText() {
        return titleView.getText();
    }

    public void setTextColor(ColorStateList colors) {
        titleView.setTextColor(colors);
    }

    public void setTextColor(int color) {
        titleView.setTextColor(color);
    }

    public void setTextSize(float size) {
        titleView.setTextSize(size);
    }

    public void setTextSize(int unit, float size) {
        titleView.setTextSize(unit, size);
    }
}
