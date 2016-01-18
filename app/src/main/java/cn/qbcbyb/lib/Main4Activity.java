package cn.qbcbyb.lib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.zbar.lib.decode.CaptureContainer;
import com.zbar.lib.encode.EncodeHandler;

import cn.qbcbyb.library.util.Msg;

public class Main4Activity extends AppCompatActivity implements CaptureContainer.CaptureContainerCanFinish {

    private CaptureContainer container = new CaptureContainer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        try {
            final Bitmap test = EncodeHandler.createQRCode("Test", 500);
            final ImageView view = (ImageView) findViewById(R.id.top_mask);
            view.setImageBitmap(test);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        container.init(this, R.id.capture_containter, R.id.capture_crop_layout, R.id.capture_preview, R.id.capture_scan_line);
    }

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
        Msg.showInfo(this, result);
        return false;
    }

    @Override
    public void onFinish() {
        finish();
    }
}
