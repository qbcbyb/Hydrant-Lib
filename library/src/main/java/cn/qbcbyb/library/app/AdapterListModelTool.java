package cn.qbcbyb.library.app;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.qbcbyb.library.adapter.ModelAdapter;
import cn.qbcbyb.library.model.BaseModel;

/**
 * Created by 秋云 on 2014/12/10.
 */
public class AdapterListModelTool<T extends BaseModel> extends AdapterViewTool<PullToRefreshListView,ModelAdapter<T>,List<T>> {
    public AdapterListModelTool(IAdapterViewContainer<PullToRefreshListView,ModelAdapter<T>,List<T>> listContainer) {
        super(listContainer);
    }

    @Override
    public void finishLoadData(List<T> result) {
        getListView().onRefreshComplete();
        if (result == null || result.size() == 0) {
            getListView().setMode(getListContainer().getNoMoreRefreshMode());
            if (getPageIndex() == 1) {
                getAdapter().changeData(result);
                getAdapter().notifyDataSetChanged();
            }
            return;
        }
        getListView().setMode(getListContainer().getInitRefreshMode());
        if (getPageIndex() == 1) {
            getAdapter().changeData(result);
        } else {
            getAdapter().addData(result);
        }
        getAdapter().notifyDataSetChanged();
        prepareNextPageIndex();
    }
}
