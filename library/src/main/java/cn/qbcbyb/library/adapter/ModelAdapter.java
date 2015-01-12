package cn.qbcbyb.library.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;

import cn.qbcbyb.library.model.BaseModel;

public class ModelAdapter<T extends BaseModel> extends BaseAdapter {

    private int[] mTo;
    private String[] mFrom;
    private ViewBinder mViewBinder;

    private List<T> mData;

    private int mResource;
    private int mDropDownResource;
    private LayoutInflater mInflater;
    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter
     *                 is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row
     *                 in the list. The Maps contain the data for each row, and
     *                 should include all the entries specified in "from"
     * @param resource Resource identifier of a view layout that defines the views
     *                 for this list item. The layout file should include at least
     *                 those named views defined in "to"
     * @param from     A list of column names that will be added to the Map
     *                 associated with each item.
     * @param to       The views that should display column in the "from" parameter.
     *                 These should all be TextViews. The first N views in this list
     *                 are given the values of the first N columns in the from
     *                 parameter.
     */
    public ModelAdapter(Context context, List<T> data, int resource, String[] from, int[] to) {
        mData = data;
        mResource = mDropDownResource = resource;
        mFrom = from;
        mTo = to;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<T> getData() {
        return mData;
    }

    public void addData(List<T> data) {
        if (mData != null) {
            mData.addAll(data);
        }
    }

    public void addData(T data) {
        if (mData != null) {
            mData.add(data);
        }
    }

    public void changeData(List<T> data) {
        if (mData != null) {
            mData = null;
            mData = data;
        } else {
            mData = data;
        }

    }

    /**
     * @see android.widget.Adapter#getCount()
     */
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }

        bindView(position, v, parent);
        return v;
    }

    /**
     * <p>
     * Sets the layout resource to create the drop down views.
     * </p>
     *
     * @param resource the layout resource defining the drop down views
     * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
     */
    public void setDropDownViewResource(int resource) {
        this.mDropDownResource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    private void bindView(int position, View view, ViewGroup parent) {
        final T dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }
        final ViewBinder binder = mViewBinder;
        final String[] from = mFrom;
        final int[] to = mTo;
        final int count = to.length;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                String fromFieldName = from[i];
                final Object data = dataSet.get(fromFieldName);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }

                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, dataSet, data, text);
                }

                if (!bound) {
                    if (v instanceof Checkable) {
                        if (data instanceof Boolean) {
                            ((Checkable) v).setChecked((Boolean) data);
                        } else if (v instanceof TextView) {
                            // Note: keep the instanceof TextView check at the
                            // bottom of these
                            // ifs since a lot of views are TextViews (e.g.
                            // CheckBoxes).
                            setViewText((TextView) v, text);
                        } else {
                            throw new IllegalStateException(v.getClass().getName()
                                    + " should be bound to a Boolean, not a "
                                    + (data == null ? "<unknown type>" : data.getClass()));
                        }
                    } else if (v instanceof TextView) {
                        // Note: keep the instanceof TextView check at the
                        // bottom of these
                        // ifs since a lot of views are TextViews (e.g.
                        // CheckBoxes).
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        if (data instanceof Integer) {
                            setViewImage((ImageView) v, (Integer) data);
                        } else {
                            setViewImage((ImageView) v, text);
                        }
                    } else if (v instanceof WebView) {
                        /*
                         * v.setFocusable(false); WebView wv = ((WebView) v);
                         * WebSettings webSettings = wv.getSettings();
                         * webSettings
                         * .setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                         * webSettings.setTextSize(TextSize.NORMAL);
                         * wv.loadDataWithBaseURL(null, data.toString(),
                         * "text/html", "utf-8", null); WebViewClickListener wvc
                         * = new WebViewClickListener( activity, wv, parent,
                         * position); wvc.setData(data.toString());
                         * wv.setOnTouchListener(wvc);
                         */
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a "
                                + " view that can be bounds by this ModelAdapter");
                    }
                }
            }
        }
    }

    /**
     * Returns the {@link ViewBinder} used to bind data to views.
     *
     * @return a ViewBinder or null if the binder does not exist
     */
    public ViewBinder getViewBinder() {
        return mViewBinder;
    }

    /**
     * Sets the binder used to bind data to views.
     *
     * @param viewBinder the binder used to bind data to views, can be null to remove
     *                   the existing binder
     * @see #getViewBinder()
     */
    public void setViewBinder(ViewBinder viewBinder) {
        mViewBinder = viewBinder;
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if there
     * is no existing ViewBinder or if the existing ViewBinder cannot handle
     * binding to an ImageView.
     * <p/>
     * This method is called instead of {@link #setViewImage(ImageView, String)}
     * if the supplied data is an int or Integer.
     *
     * @param v     ImageView to receive an image
     * @param value the value retrieved from the data set
     * @see #setViewImage(ImageView, String)
     */
    public void setViewImage(ImageView v, int value) {
        v.setImageResource(value);
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if there
     * is no existing ViewBinder or if the existing ViewBinder cannot handle
     * binding to an ImageView.
     * <p/>
     * By default, the value will be treated as an image resource. If the value
     * cannot be used as an image resource, the value is used as an image Uri.
     * <p/>
     * This method is called instead of {@link #setViewImage(ImageView, int)} if
     * the supplied data is not an int or Integer.
     *
     * @param v     ImageView to receive an image
     * @param value the value retrieved from the data set
     * @see #setViewImage(ImageView, int)
     */
    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    /**
     * Called by bindView() to set the text for a TextView but only if there is
     * no existing ViewBinder or if the existing ViewBinder cannot handle
     * binding to a TextView.
     *
     * @param v    TextView to receive text
     * @param text the text to be set for the TextView
     */
    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    /**
     * This class can be used by external clients of SimpleAdapter to bind
     * values to views.
     * <p/>
     * You should use this class to bind values to views that are not directly
     * supported by SimpleAdapter or to change the way binding occurs for views
     * supported by SimpleAdapter.
     *
     * @see SimpleAdapter#setViewImage(ImageView, int)
     * @see SimpleAdapter#setViewImage(ImageView, String)
     * @see SimpleAdapter#setViewText(TextView, String)
     */
    public static interface ViewBinder {
        /**
         * Binds the specified data to the specified view.
         * <p/>
         * When binding is handled by this ViewBinder, this method must return
         * true. If this method returns false, SimpleAdapter will attempts to
         * handle the binding on its own.
         *
         * @param view               the view to bind the data to
         * @param data               the data to bind to the view
         * @param textRepresentation a safe String representation of the supplied data: it is
         *                           either the result of data.toString() or an empty String
         *                           but it is never null
         * @return true if the data was bound to the view, false otherwise
         */
        boolean setViewValue(View view, Object dataSet, Object data, String textRepresentation);
    }

    public static interface OnSelectedChange {
        void onChange(BaseAdapter adapter, int oldIndex, int newIndex);
    }

}