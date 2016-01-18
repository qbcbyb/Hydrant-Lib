package cn.qbcbyb.library.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qbcbyb.library.R;

public abstract class CustomViewActionBarActivity extends BaseActivity {

    private TextView titleView = null;
    private ImageView leftImageView = null;
    private ImageView rightImageView = null;
    private TextView leftTextView;
    private TextView rightTextView;
    private RelativeLayout customActionBar = null;

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
    private LinearLayout linearLayoutContentView;

    protected void onLeftClick(View v) {
        onBackPressed();
    }

    protected void onRightClick(View v) {
    }

    protected void onTitleClick(View v) {
    }

    protected void setCustomActionBar(RelativeLayout customActionBar) {
        if (customActionBar != null && this.customActionBar != customActionBar) {
            this.customActionBar = customActionBar;
            titleView = (TextView) customActionBar.findViewById(R.id.toolbar_titleTextView);
            leftImageView = (ImageView) customActionBar.findViewById(R.id.toolbar_leftImageView);
            rightImageView = (ImageView) customActionBar.findViewById(R.id.toolbar_rightImageView);
            leftTextView = (TextView) customActionBar.findViewById(R.id.toolbar_leftTextView);
            rightTextView = (TextView) customActionBar.findViewById(R.id.toolbar_rightTextView);
            if (titleView != null) {
                titleView.setText(getTitle());
                titleView.setOnClickListener(onClickListener);
            }
            if (leftImageView != null) {
                leftImageView.setOnClickListener(onClickListener);
            }
            if (rightImageView != null) {
                rightImageView.setOnClickListener(onClickListener);
            }
            if (leftTextView != null) {
                leftTextView.setOnClickListener(onClickListener);
            }
            if (rightTextView != null) {
                rightTextView.setOnClickListener(onClickListener);
            }
        }
    }

    private LinearLayout getContentViewWithToolbar() {
        if (linearLayoutContentView == null) {
            linearLayoutContentView = new LinearLayout(this);
            linearLayoutContentView.setOrientation(LinearLayout.VERTICAL);
            View.inflate(this, R.layout.default_toolbar_ios, linearLayoutContentView);
        }
        return linearLayoutContentView;
    }

    @Override
    public void setContentView(int layoutResID) {
        LinearLayout contentViewWithToolbar = getContentViewWithToolbar();
        View.inflate(this, layoutResID, contentViewWithToolbar);
        super.setContentView(contentViewWithToolbar);
    }

    @Override
    public void setContentView(View view) {
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        }
        LinearLayout contentViewWithToolbar = getContentViewWithToolbar();
        contentViewWithToolbar.addView(view);
        super.setContentView(contentViewWithToolbar);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        }
        LinearLayout contentViewWithToolbar = getContentViewWithToolbar();
        contentViewWithToolbar.addView(view);
        super.setContentView(contentViewWithToolbar, params);
    }

    @Override
    protected void afterCreate() {
        super.afterCreate();
        initActionBarView();
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getLeftTextView() {
        return leftTextView;
    }

    public TextView getRightTextView() {
        return rightTextView;
    }

    public ImageView getLeftImageView() {
        return leftImageView;
    }

    public ImageView getRightImageView() {
        return rightImageView;
    }

    protected void initActionBarView() {
        setCustomActionBar((RelativeLayout) findViewById(R.id.toolbar));
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
