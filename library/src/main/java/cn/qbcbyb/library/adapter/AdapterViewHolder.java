package cn.qbcbyb.library.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by 秋云 on 2014/10/11.
 */
public class AdapterViewHolder {
    private View view;
    private SparseArray<View> viewSparseArray;

    public AdapterViewHolder(View view) {
        this.view = view;
        this.viewSparseArray = new SparseArray<View>();
    }

    public View findViewById(int id) {
        if (this.viewSparseArray.indexOfKey(id) < 0) {
            this.viewSparseArray.put(id, view.findViewById(id));
        }
        return this.viewSparseArray.get(id);
    }
}
