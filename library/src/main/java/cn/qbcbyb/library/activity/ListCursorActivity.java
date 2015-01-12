package cn.qbcbyb.library.activity;

import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.qbcbyb.library.app.AdapterListCursorTool;
import cn.qbcbyb.library.app.AdapterViewTool;

/**
 * Created by 秋云 on 2014/10/11.
 */
public abstract class ListCursorActivity extends BaseActivity implements AdapterViewTool.IAdapterViewContainer<PullToRefreshListView, CursorAdapter, Cursor> {
    protected AdapterListCursorTool listCursorTool;

    @Override
    public AdapterListCursorTool getTool() {
        return listCursorTool;
    }

    @Override
    protected void doInit() {
        super.doInit();
        listCursorTool = new AdapterListCursorTool(this);
        listCursorTool.init();
    }

    @Override
    public View findById(int id) {
        return findViewById(id);
    }
}
