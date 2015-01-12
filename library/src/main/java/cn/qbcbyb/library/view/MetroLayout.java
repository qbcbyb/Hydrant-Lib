package cn.qbcbyb.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import cn.qbcbyb.library.R;

/**
 * Created by 秋云 on 2014/8/24.
 */
public class MetroLayout extends ViewGroup {
    private static final String TAG = "MetroLayout";

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    public static final int UNDEFINED = Integer.MIN_VALUE;
    private static final int DEFAULT_ORIENTATION = HORIZONTAL;
    int orientation = DEFAULT_ORIENTATION;
    int rowCount = 1;
    int columnCount = 1;
    int dividerSize = 0;

    private Map<View, Point> measuredLocations;

    public MetroLayout(Context context) {
        super(context);
    }

    public MetroLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MetroLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        if (this.orientation != orientation) {
            this.orientation = orientation;
            requestLayout();
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        if (this.rowCount != rowCount) {
            this.rowCount = rowCount;
            requestLayout();
        }
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        if (this.columnCount != columnCount) {
            this.columnCount = columnCount;
            requestLayout();
        }
    }

    public int getDividerSize() {
        return dividerSize;
    }

    public void setDividerSize(int dividerSize) {
        this.dividerSize = dividerSize;
        requestLayout();
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MetroLayout);
        try {
            setOrientation(a.getInt(R.styleable.MetroLayout_orientation, DEFAULT_ORIENTATION));
            setRowCount(a.getInt(R.styleable.MetroLayout_rowCount, 1));
            setColumnCount(a.getInt(R.styleable.MetroLayout_columnCount, 1));
            setDividerSize(a.getDimensionPixelSize(R.styleable.MetroLayout_dividerSize, 0));
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredLocations = new HashMap<View, Point>();
        if (orientation == HORIZONTAL) {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = getMeasuredWidth();

        int contentWidth = measureWidth - getPaddingLeft() - getPaddingRight();

        int columnWidth = (contentWidth - (columnCount - 1) * dividerSize) / columnCount;

        int count = getChildCount();
        int[] rowHeightTotal = new int[columnCount];
        int nowIndex = 0;
        for (int i = 0; i < rowHeightTotal.length; i++) {
            rowHeightTotal[i] = getPaddingTop();
        }
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v == null) {
                continue;
            }
            if (v.getVisibility() == View.GONE) {
                continue;
            }

            LayoutParams lp = getLayoutParams(v);

            int columnSpan = lp.columnSpan;
            int childWidth = columnWidth * columnSpan + dividerSize * (columnSpan - 1);

            final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin,
                    lp.height);
            v.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            int x = Integer.MIN_VALUE;
            int y = Integer.MIN_VALUE;

            int lowColumn = 0;
            int minHeight = Integer.MAX_VALUE;
            for (int j = 0; (j + columnSpan - 1) < columnCount; j++) {
                int heightAll = Integer.MIN_VALUE;
                for (int k = 0; k < columnSpan; k++) {
                    heightAll = Math.max(heightAll, rowHeightTotal[j + k]);
                }
                if (minHeight > heightAll) {
                    minHeight = heightAll;
                    lowColumn = j;
                }
            }
            while (nowIndex % columnCount != lowColumn) {
                nowIndex++;
            }

            for (int j = 0; j < columnSpan; j++) {
                int columnIndex = nowIndex % columnCount;
                int rowIndex = nowIndex / columnCount;

                rowHeightTotal[columnIndex] = minHeight;

                if (rowIndex != 0) {
                    rowHeightTotal[columnIndex] += dividerSize;
                }
                if (j == 0) {
                    x = getPaddingLeft();
                    x += (columnIndex * (columnWidth + dividerSize));
                }
                y = Math.max(y, rowHeightTotal[columnIndex]);
                rowHeightTotal[columnIndex] += (v.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                nowIndex++;
            }
            x += lp.leftMargin;
            y += lp.topMargin;
            measuredLocations.put(v, new Point(x, y));
        }
        for (int i = 0; i < rowHeightTotal.length; i++) {
            rowHeightTotal[i] += getPaddingBottom();
        }
        setMeasuredDimension(measureWidth, getMax(rowHeightTotal));
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int measureHeight = this.getMeasuredHeight();

        int contentHeight = measureHeight - getPaddingTop() - getPaddingBottom();

        int rowHeight = (contentHeight - (rowCount - 1) * dividerSize) / rowCount;

        int count = getChildCount();
        int[] columnWidthTotal = new int[rowCount];
        int nowIndex = 0;
        for (int i = 0; i < columnWidthTotal.length; i++) {
            columnWidthTotal[i] = getPaddingLeft();
        }
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v == null) {
                continue;
            }
            if (v.getVisibility() == View.GONE) {
                continue;
            }

            LayoutParams lp = getLayoutParams(v);

            int rowSpan = lp.rowSpan;
            int childHeight = rowHeight * rowSpan + dividerSize * (rowSpan - 1);

            final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin,
                    lp.width);
            v.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            int x = Integer.MIN_VALUE;
            int y = Integer.MIN_VALUE;

            int lowRow = 0;
            int minWidth = Integer.MAX_VALUE;
            for (int j = 0; (j + rowSpan - 1) < columnCount; j++) {
                int widthAll = Integer.MIN_VALUE;
                for (int k = 0; k < rowSpan; k++) {
                    widthAll = Math.max(widthAll, columnWidthTotal[j + k]);
                }
                if (minWidth > widthAll) {
                    minWidth = widthAll;
                    lowRow = j;
                }
            }

            while (nowIndex % rowCount != lowRow) {
                nowIndex++;
            }

            for (int j = 0; j < rowSpan; j++) {
                int rowIndex = nowIndex % rowCount;
                int columnIndex = nowIndex / rowCount;

                columnWidthTotal[rowIndex] = minWidth;

                if (columnIndex != 0) {
                    columnWidthTotal[rowIndex] += dividerSize;
                }
                if (j == 0) {
                    y = getPaddingTop();
                    y += (rowIndex * (rowHeight + dividerSize));
                }
                x = Math.max(x, columnWidthTotal[rowIndex]);
                columnWidthTotal[rowIndex] += (v.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                nowIndex++;
            }
            y += lp.topMargin;
            x += lp.leftMargin;
            measuredLocations.put(v, new Point(x, y));
        }
        for (int i = 0; i < columnWidthTotal.length; i++) {
            columnWidthTotal[i] += getPaddingRight();
        }
        setMeasuredDimension(getMax(columnWidthTotal), measureHeight);
    }

    private int getMax(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        return max;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (measuredLocations != null) {
            int count = getChildCount();
            int childLeft = 0;
            int childTop = 0;
            int childRight = 0;
            int childBottom = 0;
            for (int i = 0; i < count; i++) {
                View c = getChildAt(i);
                if (!measuredLocations.containsKey(c)) {
                    continue;
                }
                Point location = measuredLocations.get(c);
                childLeft = location.x;//+l
                childTop = location.y;//+t
                childRight = childLeft + c.getMeasuredWidth();
                childBottom = childTop + c.getMeasuredHeight();
                c.layout(childLeft, childTop, childRight, childBottom);
            }
        }
        measuredLocations = null;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}
     * and a height of {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
     * when the layout's orientation is {@link #VERTICAL}. When the orientation is
     * {@link #HORIZONTAL}, the width is set to {@link LayoutParams#WRAP_CONTENT}
     * and the height to {@link LayoutParams#WRAP_CONTENT}.
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        if (orientation == HORIZONTAL) {
            return new LayoutParams(LayoutParams.WRAP_CONTENT, 0);
        } else if (orientation == VERTICAL) {
            return new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        }
        return null;
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }


    // Override to allow type-checking of LayoutParams.
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    private LayoutParams getLayoutParams(View v) {
        return (LayoutParams) v.getLayoutParams();
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private int rowSpan = 1;
        private int columnSpan = 1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MetroLayout_Layout);
            try {
                setRowSpan(a.getInt(R.styleable.MetroLayout_Layout_layout_rowSpan, 1));
                setColumnSpan(a.getInt(R.styleable.MetroLayout_Layout_layout_columnSpan, 1));
            } finally {
                a.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public int getRowSpan() {
            return rowSpan;
        }

        public void setRowSpan(int rowSpan) {
            this.rowSpan = rowSpan;
        }

        public int getColumnSpan() {
            return columnSpan;
        }

        public void setColumnSpan(int columnSpan) {
            this.columnSpan = columnSpan;
        }
    }
}
