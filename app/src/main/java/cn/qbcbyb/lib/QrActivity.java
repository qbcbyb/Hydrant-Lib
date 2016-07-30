package cn.qbcbyb.lib;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.zbar.lib.decode.CaptureContainer;

import cn.qbcbyb.library.activity.CustomViewActionBarActivity;

/**
 * Created by qbcby on 2016/7/27.
 */
public class QrActivity extends CustomViewActionBarActivity implements CaptureContainer.CaptureContainerCanFinish {
    private CaptureContainer container = new CaptureContainer();

    @Override
    protected void doCreate() {
        setContentView(R.layout.activity_qr_scan);
        container.init(this, R.id.capture_containter, R.id.capture_crop_layout, R.id.capture_preview, R.id.capture_scan_line);
    }

    @Override
    public View findById(int id) {
        return findViewById(id);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onCaptureAndCheckNeedRestart(String result) {
        Log.d(TAG, "onCaptureAndCheckNeedRestart() called with: " + "result = [" + result + "]");
        container.pause();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                container.resume();
            }
        }, 2000);
        return false;
    }

    @Override
    public void onFinish() {
        finish();
    }

    @Override
    protected void onPause() {
        container.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        container.resume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        container.destory();
        super.onDestroy();
    }
}
