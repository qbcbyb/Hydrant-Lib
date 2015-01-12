package uk.co.senab.photoview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import uk.co.senab.photoview.log.LogManager;

/**
 * Created by 秋云 on 2014/8/28.
 */
public class PhotoViewPager extends ViewPager {
    private boolean isLocked;

    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            LogManager.getLogger().d("PhotoViewAttacher", "onInterceptTouchEvent");
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            LogManager.getLogger().d("PhotoViewAttacher", "onInterceptTouchEvent_IllegalArgumentException");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogManager.getLogger().d("PhotoViewAttacher", "onTouchEvent");
        if (!isLocked) {
            return super.onTouchEvent(event);
        }
        return false;
    }

}
