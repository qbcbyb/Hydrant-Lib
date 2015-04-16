package cn.qbcbyb.library.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.qbcbyb.library.app.AdapterListCursorTool;
import cn.qbcbyb.library.app.AdapterViewTool;

/**
 * Created by 秋云 on 2014/7/25.
 */
public abstract class ListCursorFragment extends BaseFragment implements AdapterViewTool.IAdapterViewContainer<PullToRefreshListView, CursorAdapter, Cursor> {
    protected AdapterListCursorTool listCursorTool;

    @Override
    public AdapterListCursorTool getTool() {
        return listCursorTool;
    }

    @Override
    public void onMainViewCreated(View view) {
        super.onMainViewCreated(view);
        listCursorTool=new AdapterListCursorTool(this);
        listCursorTool.init();
    }

    @Override
    public View findById(int id) {
        return getCachedView().findViewById(id);
    }
}
