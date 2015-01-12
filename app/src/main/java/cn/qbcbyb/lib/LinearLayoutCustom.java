package cn.qbcbyb.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by 秋云 on 2014/11/3.
 */
public class LinearLayoutCustom extends LinearLayout implements Checkable {

    private boolean checked = true;

    public LinearLayoutCustom(Context context) {
        super(context);
    }

    public LinearLayoutCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return checked && super.dispatchTouchEvent(ev);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
