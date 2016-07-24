package com.github.qbcbyb.viewlibrary;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class ClearableEditText extends EditText {

    public static final int VISIBLE_LEVEL = 800;
    public static final int INVISIBLE_LEVEL = 0;
    private final LevelListDrawable clearDrawable;
    private final RectF clearHotspotRect;
    private boolean isClearVisible;

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        clearDrawable = (LevelListDrawable) ContextCompat.getDrawable(context, R.drawable.ic_edit_clear_level);
        clearDrawable.setLevel(VISIBLE_LEVEL);
        clearDrawable.setBounds(0, 0, clearDrawable.getCurrent().getIntrinsicWidth(), clearDrawable.getCurrent().getIntrinsicHeight());
        clearDrawable.setLevel(INVISIBLE_LEVEL);
        clearHotspotRect = new RectF();

        checkClearVisibility();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClearVisible && event.getActionMasked() == MotionEvent.ACTION_UP) {
            final int compoundPaddingTop = getCompoundPaddingTop();
            final int compoundPaddingRight = getCompoundPaddingRight();
            final int compoundPaddingBottom = getCompoundPaddingBottom();
            clearHotspotRect.set(getWidth() - compoundPaddingRight, compoundPaddingTop, getWidth(), getHeight() - compoundPaddingBottom);
            if (clearHotspotRect.contains(event.getX(), event.getY())) {
                setText(null);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setError(null);
        checkClearVisibility();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        checkClearVisibility();
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (right != null) {
            throw new IllegalStateException("Right drawable has be used to clearDrawable!");
        }
        super.setCompoundDrawables(left, top, clearDrawable, bottom);
    }

    private void checkClearVisibility() {
        this.isClearVisible = getText().length() != 0 && isFocused();
        if (clearDrawable != null) {
            if (isClearVisible) {
                clearDrawable.setLevel(VISIBLE_LEVEL);
                final Drawable[] dr = getCompoundDrawables();
                if (dr[2] != clearDrawable) {
                    super.setCompoundDrawables(dr[0], dr[1], clearDrawable, dr[3]);
                }
            } else {
                clearDrawable.setLevel(INVISIBLE_LEVEL);
            }
            invalidateDrawable(clearDrawable);
        }
    }
}
