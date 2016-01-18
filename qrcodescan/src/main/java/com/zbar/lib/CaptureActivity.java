package com.zbar.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.zbar.lib.decode.CaptureContainer;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * <p/>
 * 时间: 2014年5月9日 下午12:25:31
 * <p/>
 * 版本: V_1.0.0
 * <p/>
 * 描述: 扫描界面
 */
public class CaptureActivity extends Activity implements CaptureContainer.CaptureContainerCanFinish {

    public static final String RESULT_DATA_KEY = "result";

    private CaptureContainer container = new CaptureContainer();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qr_scan);

        container.init(this, R.id.capture_containter, R.id.capture_crop_layout, R.id.capture_preview, R.id.capture_scan_line);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        container.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        container.pause();
    }

    @Override
    protected void onDestroy() {
        container.destory();
        super.onDestroy();
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
        if (TextUtils.isEmpty(result)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
//			System.out.println("Result:"+resultString);
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(RESULT_DATA_KEY, result);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        return false;
    }

    @Override
    public void onFinish() {
        finish();
    }
}