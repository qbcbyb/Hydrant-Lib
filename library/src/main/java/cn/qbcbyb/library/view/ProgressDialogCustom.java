package cn.qbcbyb.library.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import cn.qbcbyb.library.R;
import cn.qbcbyb.library.util.StringUtils;

/**
 * Created by 秋云 on 2014/7/24.
 */
public class ProgressDialogCustom extends Dialog {
    private String message;

    public ProgressDialogCustom(Context context) {
        super(context, R.style.ProgressDialogTheme);
    }

    public ProgressDialogCustom(Context context, String message) {
        this(context);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        if (StringUtils.isNotEmpty(message)) {
            TextView textView = (TextView) findViewById(R.id.message);
            textView.setText(message);
        }
    }
}
