package cn.qbcbyb.library.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import cn.qbcbyb.library.activity.BaseActivity;

/**
 * Created by 秋云 on 2014/7/25.
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();

    public static final DisplayImageOptions DISPLAY_IMAGE_OPTIONS_OF_SOURCE = BaseActivity.DISPLAY_IMAGE_OPTIONS_OF_SOURCE;
    public static final DisplayImageOptions DISPLAY_IMAGE_OPTIONS_OF_LIST = BaseActivity.DISPLAY_IMAGE_OPTIONS_OF_LIST;
    public static final int REQUEST_CODE = BaseActivity.REQUEST_CODE;
    public static final int REQUEST_CODE2 = BaseActivity.REQUEST_CODE2;
    public static final int REQUEST_CODE3 = BaseActivity.REQUEST_CODE3;
    public static final int REQUEST_CODE4 = BaseActivity.REQUEST_CODE4;

    private View cachedView;

    public View getCachedView() {
        return cachedView;
    }

    public void setCachedView(View cachedView) {
        this.cachedView = cachedView;
    }

    public boolean isViewCachedEnable() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!isViewCachedEnable() || getCachedView() == null) {
            View view = onCreateMainView(inflater, container, savedInstanceState);
            setCachedView(view);
            onMainViewCreated(view);
            onMainViewInited();
        } else if (getCachedView().getParent() != null) {
            ((ViewGroup) getCachedView().getParent()).removeView(getCachedView());
        }
        return getCachedView();
    }

    View onCreateMainView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateContentView(inflater, container, savedInstanceState);
    }

    public void onMainViewCreated(View view) {
    }

    public void onMainViewInited() {
    }

    public abstract View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

}
