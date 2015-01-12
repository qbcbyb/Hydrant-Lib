package cn.qbcbyb.library.activity;

import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.qbcbyb.library.adapter.ModelAdapter;
import cn.qbcbyb.library.app.AdapterListModelTool;
import cn.qbcbyb.library.app.AdapterViewTool;
import cn.qbcbyb.library.model.BaseModel;


/**
 * Created by 秋云 on 2014/10/11.
 */
public abstract class ListModelActivity<T extends BaseModel> extends BaseActivity implements AdapterViewTool.IAdapterViewContainer<PullToRefreshListView,ModelAdapter<T>,List<T>> {
    protected AdapterListModelTool<T> listCursorTool;

    @Override
    public AdapterListModelTool<T> getTool() {
        return listCursorTool;
    }

    @Override
    protected void doInit() {
        super.doInit();
        listCursorTool=new AdapterListModelTool<T>(this);
        listCursorTool.init();
    }

    @Override
    public View findById(int id) {
        return findViewById(id);
    }
}
