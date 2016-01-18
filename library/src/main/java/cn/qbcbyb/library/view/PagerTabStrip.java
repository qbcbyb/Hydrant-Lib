/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.qbcbyb.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.qbcbyb.library.R;


public class PagerTabStrip extends LinearLayout {

    private final LayoutParams LAYOUTPARAMS = new LayoutParams(0, -2, 1);

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private ViewPager pager;

    private int tabCount;

    private int currentItem = 0;

    private int indicatorDrawable;

    private LayoutParams childLayoutParams;

    public void setCurrentItem(int currentItem) {
        if (this.currentItem > -1 && this.currentItem < getChildCount()) {
            View view = getChildAt(this.currentItem);
            view.setSelected(false);
        }
        this.currentItem = currentItem;
        if (this.currentItem > -1 && this.currentItem < getChildCount()) {
            View view = getChildAt(this.currentItem);
            view.setSelected(true);
        }
    }

    public PagerTabStrip(Context context) {
        this(context, null);
    }

    public PagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        setOrientation(LinearLayout.HORIZONTAL);

        // get custom attrs

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerTabStrip);

        indicatorDrawable = a.getResourceId(R.styleable.PagerTabStrip_indicatorDrawable, R.drawable.pager_indicator);

        a.recycle();

    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public LayoutParams getChildLayoutParams() {
        return childLayoutParams;
    }

    public void setChildLayoutParams(LayoutParams childLayoutParams) {
        this.childLayoutParams = childLayoutParams;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {
            addIconTab(i);
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                setCurrentItem(pager.getCurrentItem());
            }
        });

    }


    private void addIconTab(final int position) {

        ImageView tab = new ImageView(getContext());
        tab.setImageResource(indicatorDrawable);
        tab.setScaleType(ImageView.ScaleType.CENTER);

        addTab(position, tab);

    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        addView(tab, position, childLayoutParams == null ? LAYOUTPARAMS : childLayoutParams);
    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = getChildAt(i);

            if (v instanceof ImageView) {
                ((ImageView) v).setImageResource(indicatorDrawable);
            }
        }
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            setCurrentItem(position);
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public int getIndicatorDrawable() {
        return indicatorDrawable;
    }

    public void setIndicatorDrawable(int indicatorDrawable) {
        this.indicatorDrawable = indicatorDrawable;
        updateTabStyles();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentItem = savedState.currentPosition;
        setCurrentItem(currentItem);
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentItem;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
