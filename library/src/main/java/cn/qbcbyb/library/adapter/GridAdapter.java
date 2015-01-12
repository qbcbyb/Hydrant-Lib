package cn.qbcbyb.library.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by 秋云 on 2014/8/18.
 */
public class GridAdapter extends BaseAdapter {

    public interface OnGridItemClickListener {
        public void onGridItemClick(Object data);
    }

    private BaseAdapter mAdapter;
    private Context context;
    private OnGridItemClickListener onItemClickListener;
    private LinearLayout.LayoutParams gridlayoutParamsL;
    private LinearLayout.LayoutParams gridlayoutParamsR;

    public GridAdapter(Context context, BaseAdapter adapter, OnGridItemClickListener onItemClickListener) {
        mAdapter = adapter;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dip10 = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics);
        int dip5 = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, displayMetrics);
        gridlayoutParamsL = new LinearLayout.LayoutParams(0, -2);
        gridlayoutParamsL.weight = 1;
        gridlayoutParamsL.leftMargin = dip10;
        gridlayoutParamsL.rightMargin = dip5;
        gridlayoutParamsR = new LinearLayout.LayoutParams(0, -2);
        gridlayoutParamsR.weight = 1;
        gridlayoutParamsR.leftMargin = dip5;
        gridlayoutParamsR.rightMargin = dip10;
    }

    @Override
    public int getCount() {
        return (int) Math.ceil(mAdapter.getCount() / 2f);
    }

    public int getRealCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int arg0) {
        int position = arg0;
        Object item1 = mAdapter.getItem(position * 2);
        if (position * 2 + 1 < mAdapter.getCount()) {
            return new Object[]{item1, mAdapter.getItem(position * 2 + 1)};
        } else {
            return new Object[]{item1};
        }
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object[] objs = (Object[]) getItem(position);
        if (convertView == null) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setWeightSum(2);
            RelativeLayout relativeLayout1 = new RelativeLayout(context);
            RelativeLayout relativeLayout2 = new RelativeLayout(context);
            linearLayout.addView(relativeLayout1, gridlayoutParamsL);
            linearLayout.addView(relativeLayout2, gridlayoutParamsR);
            relativeLayout1.addView(getChildView(position * 2, null, relativeLayout1));
            if (objs.length == 2) {
                relativeLayout2.addView(getChildView(position * 2 + 1, null, relativeLayout2));
            }
            convertView = linearLayout;
        } else {
            LinearLayout linearLayout = (LinearLayout) convertView;
            RelativeLayout relativeLayout1 = (RelativeLayout) linearLayout.getChildAt(0);
            RelativeLayout relativeLayout2 = (RelativeLayout) linearLayout.getChildAt(1);

            getChildView(position * 2,
                    relativeLayout1.getChildCount() > 0 ? relativeLayout1.getChildAt(0) : null, relativeLayout1);

            if (objs.length == 2) {
                if (relativeLayout2.getChildCount() > 0) {
                    getChildView(position * 2 + 1, relativeLayout2.getChildAt(0), relativeLayout2);
                } else {
                    relativeLayout2.addView(getChildView(position * 2 + 1, null, relativeLayout2));
                }
            }
        }
        return convertView;
    }

    private View getChildView(int childPosition, View convertView, ViewGroup parent) {
        View view = mAdapter.getView(childPosition, convertView, parent);
        view.setOnClickListener(new ClickWithObj(mAdapter.getItem(childPosition)));
        return view;
    }


    class ClickWithObj implements View.OnClickListener {
        private Object obj;

        public ClickWithObj(Object obj) {
            this.obj = obj;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onGridItemClick(this.obj);
            }
        }
    }
}
