package cn.qbcbyb.library.adapter;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qbcbyb.library.model.BaseModel;
import cn.qbcbyb.library.model.BaseParentModel;

public class ModelGroupAdapter<T extends BaseParentModel<T1>,T1 extends BaseModel>
        extends BaseExpandableListAdapter {
    private Index selectedIndex;
    private OnSelectedChange onSelectedChange;

    private List<T> mGroupData;
    private int mExpandedGroupLayout;
    private int mCollapsedGroupLayout;
    private String[] mGroupFrom;
    private int[] mGroupTo;
    private ModelAdapter.ViewBinder mViewBinder;

    private int mChildLayout;
    private int mLastChildLayout;
    private String[] mChildFrom;
    private int[] mChildTo;

    public Index getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(Index selectedIndex) {
        if (this.getOnSelectedChange() != null) {
            this.getOnSelectedChange().onChange(this.selectedIndex, selectedIndex);
        }
        this.selectedIndex = selectedIndex;
    }

    public OnSelectedChange getOnSelectedChange() {
        return onSelectedChange;
    }

    public void setOnSelectedChange(OnSelectedChange onSelectedChange) {
        this.onSelectedChange = onSelectedChange;
    }

    private LayoutInflater mInflater;

    /**
     * Constructor
     * 
     * @param context
     *            The context where the {@link android.widget.ExpandableListView} associated
     *            with this {@link ModelGroupAdapter} is running
     * @param groupData
     *            A List of Maps. Each entry in the List corresponds to one
     *            group in the list. The Maps contain the data for each group,
     *            and should include all the entries specified in "groupFrom"
     * @param groupFrom
     *            A list of keys that will be fetched from the Map associated
     *            with each group.
     * @param groupTo
     *            The group views that should display column in the "groupFrom"
     *            parameter. These should all be TextViews. The first N views in
     *            this list are given the values of the first N columns in the
     *            groupFrom parameter.
     * @param groupLayout
     *            resource identifier of a view layout that defines the views
     *            for a group. The layout file should include at least those
     *            named views defined in "groupTo"
     * @param childFrom
     *            A list of keys that will be fetched from the Map associated
     *            with each child.
     * @param childTo
     *            The child views that should display column in the "childFrom"
     *            parameter. These should all be TextViews. The first N views in
     *            this list are given the values of the first N columns in the
     *            childFrom parameter.
     * @param childLayout
     *            resource identifier of a view layout that defines the views
     *            for a child. The layout file should include at least those
     *            named views defined in "childTo"
     */
    public ModelGroupAdapter(Context context, List<T> groupData, int groupLayout, String[] groupFrom,
                             int[] groupTo, int childLayout, String[] childFrom, int[] childTo) {
        this(context, groupData, groupLayout, groupLayout, groupFrom, groupTo, childLayout, childLayout, childFrom,
            childTo);
    }

    /**
     * Constructor
     * 
     * @param context
     *            The context where the {@link android.widget.ExpandableListView} associated
     *            with this {@link ModelGroupAdapter} is running
     * @param groupData
     *            A List of Maps. Each entry in the List corresponds to one
     *            group in the list. The Maps contain the data for each group,
     *            and should include all the entries specified in "groupFrom"
     * @param groupFrom
     *            A list of keys that will be fetched from the Map associated
     *            with each group.
     * @param groupTo
     *            The group views that should display column in the "groupFrom"
     *            parameter. These should all be TextViews. The first N views in
     *            this list are given the values of the first N columns in the
     *            groupFrom parameter.
     * @param expandedGroupLayout
     *            resource identifier of a view layout that defines the views
     *            for an expanded group. The layout file should include at least
     *            those named views defined in "groupTo"
     * @param collapsedGroupLayout
     *            resource identifier of a view layout that defines the views
     *            for a collapsed group. The layout file should include at least
     *            those named views defined in "groupTo"
     * @param childFrom
     *            A list of keys that will be fetched from the Map associated
     *            with each child.
     * @param childTo
     *            The child views that should display column in the "childFrom"
     *            parameter. These should all be TextViews. The first N views in
     *            this list are given the values of the first N columns in the
     *            childFrom parameter.
     * @param childLayout
     *            resource identifier of a view layout that defines the views
     *            for a child. The layout file should include at least those
     *            named views defined in "childTo"
     */
    public ModelGroupAdapter(Context context, List<T> groupData, int expandedGroupLayout,
                             int collapsedGroupLayout, String[] groupFrom, int[] groupTo, int childLayout, String[] childFrom,
                             int[] childTo) {
        this(context, groupData, expandedGroupLayout, collapsedGroupLayout, groupFrom, groupTo, childLayout,
            childLayout, childFrom, childTo);
    }

    /**
     * Constructor
     * 
     * @param context
     *            The context where the {@link android.widget.ExpandableListView} associated
     *            with this {@link ModelGroupAdapter} is running
     * @param groupData
     *            A List of Maps. Each entry in the List corresponds to one
     *            group in the list. The Maps contain the data for each group,
     *            and should include all the entries specified in "groupFrom"
     * @param groupFrom
     *            A list of keys that will be fetched from the Map associated
     *            with each group.
     * @param groupTo
     *            The group views that should display column in the "groupFrom"
     *            parameter. These should all be TextViews. The first N views in
     *            this list are given the values of the first N columns in the
     *            groupFrom parameter.
     * @param expandedGroupLayout
     *            resource identifier of a view layout that defines the views
     *            for an expanded group. The layout file should include at least
     *            those named views defined in "groupTo"
     * @param collapsedGroupLayout
     *            resource identifier of a view layout that defines the views
     *            for a collapsed group. The layout file should include at least
     *            those named views defined in "groupTo"
     * @param childFrom
     *            A list of keys that will be fetched from the Map associated
     *            with each child.
     * @param childTo
     *            The child views that should display column in the "childFrom"
     *            parameter. These should all be TextViews. The first N views in
     *            this list are given the values of the first N columns in the
     *            childFrom parameter.
     * @param childLayout
     *            resource identifier of a view layout that defines the views
     *            for a child (unless it is the last child within a group, in
     *            which case the lastChildLayout is used). The layout file
     *            should include at least those named views defined in "childTo"
     * @param lastChildLayout
     *            resource identifier of a view layout that defines the views
     *            for the last child within each group. The layout file should
     *            include at least those named views defined in "childTo"
     */
    public ModelGroupAdapter(Context context, List<T> groupData, int expandedGroupLayout,
                             int collapsedGroupLayout, String[] groupFrom, int[] groupTo, int childLayout, int lastChildLayout,
                             String[] childFrom, int[] childTo) {
        mGroupData = groupData;
        mExpandedGroupLayout = expandedGroupLayout;
        mCollapsedGroupLayout = collapsedGroupLayout;
        mGroupFrom = groupFrom;
        mGroupTo = groupTo;

        mChildLayout = childLayout;
        mLastChildLayout = lastChildLayout;
        mChildFrom = childFrom;
        mChildTo = childTo;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mGroupData.get(groupPosition).getChildren().get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public List<T> getData() {
        return mGroupData;
    }

    public void addData(List<T> data) {
        if (mGroupData != null) {
            mGroupData.addAll(data);
        }
    }

    public void addData(T data) {
        if (mGroupData != null) {
            mGroupData.add(data);
        }
    }

    public void changeData(List<T> data) {
        if (mGroupData != null) {
            mGroupData = null;
            mGroupData = data;
        } else {
            mGroupData = data;
        }
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = newChildView(isLastChild, parent);
        } else {
            v = convertView;
        }
        bindView(v, mGroupData.get(groupPosition).getChildren().get(childPosition), mChildFrom, mChildTo);
        return v;
    }

    /**
     * Instantiates a new View for a child.
     * 
     * @param isLastChild
     *            Whether the child is the last child within its group.
     * @param parent
     *            The eventual parent of this new View.
     * @return A new child View
     */
    public View newChildView(boolean isLastChild, ViewGroup parent) {
        return mInflater.inflate((isLastChild) ? mLastChildLayout : mChildLayout, parent, false);
    }

    private void bindView(View view, BaseModel dataObj, String[] from, int[] to) {
        int len = to.length;
        final Class<? extends BaseModel> clazz = dataObj.getClass();
        final ModelAdapter.ViewBinder binder = mViewBinder;

        for (int i = 0; i < len; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                final Object data = dataObj.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }

                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, dataObj, data, text);
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
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a "
                                + " view that can be bounds by this ModelAdapter");
                    }
                }
            }
        }
    }

    public int getChildrenCount(int groupPosition) {
        return mGroupData.get(groupPosition).getChildren().size();
    }

    public Object getGroup(int groupPosition) {
        return mGroupData.get(groupPosition);
    }

    public int getGroupCount() {
        return mGroupData.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = newGroupView(isExpanded, parent);
        } else {
            v = convertView;
        }
        bindView(v, mGroupData.get(groupPosition), mGroupFrom, mGroupTo);
        return v;
    }

    /**
     * Instantiates a new View for a group.
     * 
     * @param isExpanded
     *            Whether the group is currently expanded.
     * @param parent
     *            The eventual parent of this new View.
     * @return A new group View
     */
    public View newGroupView(boolean isExpanded, ViewGroup parent) {
        return mInflater.inflate((isExpanded) ? mExpandedGroupLayout : mCollapsedGroupLayout, parent, false);
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    /**
     * Returns the {@link ModelAdapter.ViewBinder} used to bind data to views.
     * 
     * @return a ViewBinder or null if the binder does not exist
     * 
     * @see #setViewBinder(ModelAdapter.ViewBinder)
     */
    public ModelAdapter.ViewBinder getViewBinder() {
        return mViewBinder;
    }

    /**
     * Sets the binder used to bind data to views.
     * 
     * @param viewBinder
     *            the binder used to bind data to views, can be null to remove
     *            the existing binder
     * 
     * @see #getViewBinder()
     */
    public void setViewBinder(ModelAdapter.ViewBinder viewBinder) {
        mViewBinder = viewBinder;
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if there
     * is no existing ViewBinder or if the existing ViewBinder cannot handle
     * binding to an ImageView.
     * 
     * This method is called instead of {@link #setViewImage(android.widget.ImageView, String)}
     * if the supplied data is an int or Integer.
     * 
     * @param v
     *            ImageView to receive an image
     * @param value
     *            the value retrieved from the data set
     * 
     * @see #setViewImage(android.widget.ImageView, String)
     */
    public void setViewImage(ImageView v, int value) {
        v.setImageResource(value);
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if there
     * is no existing ViewBinder or if the existing ViewBinder cannot handle
     * binding to an ImageView.
     * 
     * By default, the value will be treated as an image resource. If the value
     * cannot be used as an image resource, the value is used as an image Uri.
     * 
     * This method is called instead of {@link #setViewImage(android.widget.ImageView, int)} if
     * the supplied data is not an int or Integer.
     * 
     * @param v
     *            ImageView to receive an image
     * @param value
     *            the value retrieved from the data set
     * 
     * @see #setViewImage(android.widget.ImageView, int)
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
     * @param v
     *            TextView to receive text
     * @param text
     *            the text to be set for the TextView
     */
    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    public static interface OnSelectedChange {
        void onChange(Index oldIndex, Index newIndex);
    }

    public static class Index {
        private int groupIndex;
        private int childIndex;

        public Index(int groupIndex, int childIndex) {
            super();
            this.groupIndex = groupIndex;
            this.childIndex = childIndex;
        }

        public int getGroupIndex() {
            return groupIndex;
        }

        public void setGroupIndex(int groupIndex) {
            this.groupIndex = groupIndex;
        }

        public int getChildIndex() {
            return childIndex;
        }

        public void setChildIndex(int childIndex) {
            this.childIndex = childIndex;
        }
    }

}