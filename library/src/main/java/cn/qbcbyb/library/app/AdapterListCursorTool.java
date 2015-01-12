package cn.qbcbyb.library.app;

import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by 秋云 on 2014/12/10.
 */
public class AdapterListCursorTool extends AdapterViewTool<PullToRefreshListView, CursorAdapter, Cursor> {

    public AdapterListCursorTool(IAdapterViewContainer<PullToRefreshListView, CursorAdapter, Cursor> listContainer) {
        super(listContainer);
    }

    @Override
    public void finishLoadData(Cursor result) {
        getListView().onRefreshComplete();
        if (result == null || result.getCount() == 0) {
            getListView().setMode(getListContainer().getNoMoreRefreshMode());
            if (getPageIndex() == 1) {
                getAdapter().changeCursor(result);
            }
            return;
        }
        getListView().setMode(getListContainer().getInitRefreshMode());
        getAdapter().changeCursor(result);
    }
}
