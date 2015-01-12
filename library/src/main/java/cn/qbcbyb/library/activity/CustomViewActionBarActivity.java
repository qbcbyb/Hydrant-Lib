package cn.qbcbyb.library.activity;

import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qbcbyb.library.R;
import cn.qbcbyb.library.view.ImageViewCustom;

public abstract class CustomViewActionBarActivity extends BaseActivity {

    private ActionBar actionBar = null;
    private TextView titleView = null;
    private ImageView leftImageView = null;
    private ImageView rightImageView = null;
    private TextView leftTextView;
    private TextView rightTextView;
    private RelativeLayout customActionBar = null;
    private int actionBarBtnPadding = 0;//DP

    public int getActionBarBtnPadding() {
        if (actionBarBtnPadding == 0) {
            actionBarBtnPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        }
        return actionBarBtnPadding;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == leftImageView) {
                onLeftClick(v);
            } else if (v == rightImageView) {
                onRightClick(v);
            } else if (v == leftTextView) {
                onLeftClick(v);
            } else if (v == rightTextView) {
                onRightClick(v);
            } else if (v == titleView) {
                onTitleClick(v);
            }
        }
    };

    protected void onLeftClick(View v) {
        onBackPressed();
    }

    protected void onRightClick(View v) {
    }

    protected void onTitleClick(View v) {
    }

    protected void setCustomActionBar(RelativeLayout customActionBar) {
        if (this.customActionBar != customActionBar) {
            this.customActionBar = customActionBar;
            titleView = null;
            leftImageView = null;
            rightImageView = null;
            leftTextView = null;
            rightTextView = null;
        }
    }

    protected ActionBar getCustomActionBar() {
        return actionBar;
    }

    protected Integer getActionBarBtnColor() {
        return null;
    }

    protected Integer getActionBarTitleColor() {
        return null;
    }

    @Override
    protected void afterCreate() {
        super.afterCreate();
        initActionBar();
        if (customActionBar != null) {
            initActionBarView();
        }
    }

    protected void initActionBar() {
        if (actionBar == null) {
            actionBar = getSupportActionBar();
        }
        if (actionBar != null) {
            try {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                RelativeLayout customViewLayout = new RelativeLayout(this);
                ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT);
                actionBar.setCustomView(customViewLayout, lp);
                setCustomActionBar(customViewLayout);
            } catch (NullPointerException e) {
                actionBar = null;
            }
        }
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getLeftTextView() {
        if (leftTextView == null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            leftTextView = generateTextViewInActionBar(layoutParams);
            leftTextView.setPadding(getActionBarBtnPadding(), 0, 0, 0);
            if (getActionBarBtnColor() != null) {
                leftTextView.setTextColor(getActionBarBtnColor());
            }
            leftTextView.setOnClickListener(onClickListener);
        }
        return leftTextView;
    }

    public TextView getRightTextView() {
        if (rightTextView == null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rightTextView = generateTextViewInActionBar(layoutParams);
            rightTextView.setPadding(0, 0, getActionBarBtnPadding(), 0);
            if (getActionBarBtnColor() != null) {
                rightTextView.setTextColor(getActionBarBtnColor());
            }
            rightTextView.setOnClickListener(onClickListener);
        }
        return rightTextView;
    }

    public ImageView getLeftImageView() {
        if (leftImageView == null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            leftImageView = generateImageViewInActionBar(layoutParams);
            leftImageView.setPadding(getActionBarBtnPadding(), getActionBarBtnPadding(), 0, getActionBarBtnPadding());
            leftImageView.setOnClickListener(onClickListener);
        }
        return leftImageView;
    }

    public ImageView getRightImageView() {
        if (rightImageView == null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rightImageView = generateImageViewInActionBar(layoutParams);
            rightImageView.setPadding(0, getActionBarBtnPadding(), getActionBarBtnPadding(), getActionBarBtnPadding());
            rightImageView.setOnClickListener(onClickListener);
        }
        return rightImageView;
    }

    protected void initActionBarView() {
        initActionBarTitle();
    }

    private void initActionBarTitle() {
        if (titleView == null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            layoutParams.setMargins(100, 0, 100, 0);
            titleView = generateTextViewInActionBar(layoutParams);
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.abc_action_bar_title_text_size));
            titleView.setText(getTitle());
            if (getActionBarTitleColor() != null) {
                titleView.setTextColor(getActionBarTitleColor());
            }
            titleView.setOnClickListener(onClickListener);
        }
    }

    private TextView generateTextViewInActionBar(RelativeLayout.LayoutParams layoutParams) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setLines(1);
        addCustomViewToActionBar(textView, layoutParams);
        return textView;
    }

    private ImageView generateImageViewInActionBar(RelativeLayout.LayoutParams layoutParams) {
        ImageView imageView = new ImageViewCustom(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addCustomViewToActionBar(imageView, layoutParams);
        return imageView;
    }

    protected void addCustomViewToActionBar(View view) {
        if (customActionBar != null) {
            customActionBar.addView(view);
        }
    }

    protected void addCustomViewToActionBar(View view, ViewGroup.LayoutParams layoutParams) {
        if (customActionBar != null) {
            customActionBar.addView(view, layoutParams);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (this.titleView != null) {
            this.titleView.setText(title);
        }
        super.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

}
