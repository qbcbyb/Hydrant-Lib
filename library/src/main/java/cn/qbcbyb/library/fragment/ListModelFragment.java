package cn.qbcbyb.library.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.qbcbyb.library.adapter.ModelAdapter;
import cn.qbcbyb.library.app.AdapterListModelTool;
import cn.qbcbyb.library.app.AdapterViewTool;
import cn.qbcbyb.library.model.BaseModel;


/**
 * Created by 秋云 on 2014/7/25.
 */
public abstract class ListModelFragment<T extends BaseModel> extends  BaseFragment implements AdapterViewTool.IAdapterViewContainer<PullToRefreshListView, ModelAdapter<T>,List<T>> {
    protected AdapterListModelTool<T> adapterListModelTool;

    @Override
    public AdapterListModelTool<T> getTool() {
        return adapterListModelTool;
    }

    @Override
    public void onMainViewCreated(View view) {
        super.onMainViewCreated(view);
        adapterListModelTool=new AdapterListModelTool<T>(this);
        adapterListModelTool.init();
    }

    @Override
    public View findById(int id) {
        return getCachedView().findViewById(id);
    }
}
