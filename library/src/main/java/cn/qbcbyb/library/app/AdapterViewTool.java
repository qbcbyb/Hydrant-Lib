package cn.qbcbyb.library.app;

import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public abstract class AdapterViewTool<V extends PullToRefreshBase, A extends Adapter, R> implements PullToRefreshBase.OnRefreshListener2 {

    private boolean emptyViewSetSuccess;
    private V listView;
    private TextView txtEmptyView;
    private A adapter;
    private int pageIndex = 1;
    private IAdapterViewContainer<V, A, R> listContainer;

    public AdapterViewTool(IAdapterViewContainer<V, A, R> listContainer) {
        this.listContainer = listContainer;
    }

    public IAdapterViewContainer getListContainer() {
        return listContainer;
    }

    public boolean isEmptyViewSetSuccess() {
        return emptyViewSetSuccess;
    }

    public V getListView() {
        return listView;
    }

    public TextView getTxtEmptyView() {
        return txtEmptyView;
    }

    public A getAdapter() {
        return adapter;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void prepareNextPageIndex() {
        if (canLoadMoreNow()) {
            pageIndex++;
        }
    }

    public void clearPageIndex() {
        this.pageIndex = 1;
    }

    /**
     *
     */
    public void init() {
        listView = (V) listContainer.findById(android.R.id.list);
        txtEmptyView = (TextView) listContainer.findById(android.R.id.empty);
        if (listView != null) {
            if (txtEmptyView != null) {
                emptyViewSetSuccess = false;
                try {
                    Method setEmptyView = listView.getClass().getMethod("setEmptyView", View.class);
                    setEmptyView.invoke(listView, txtEmptyView);
                    emptyViewSetSuccess = true;
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            listView.setOnRefreshListener(this);

            listView.setMode(listContainer.getInitRefreshMode());

            adapter = listContainer.generateAdapter(listView);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        clearPageIndex();
        listContainer.startLoadData(getPageIndex());
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        listContainer.startLoadData(getPageIndex());
    }

    public boolean canLoadMoreNow() {
        return listView.getMode() == PullToRefreshBase.Mode.BOTH || listView.getMode() == PullToRefreshBase.Mode.PULL_FROM_END;
    }

    public boolean canRefreshNow() {
        return listView.getMode() == PullToRefreshBase.Mode.BOTH || listView.getMode() == PullToRefreshBase.Mode.PULL_FROM_START;
    }

    public abstract void finishLoadData(R result);

    /**
     * Created by 秋云 on 2014/12/17.
     */
    public interface IAdapterViewContainer<V1 extends PullToRefreshBase, A1 extends Adapter, R1> {

        PullToRefreshBase.Mode getInitRefreshMode();

        PullToRefreshBase.Mode getNoMoreRefreshMode();

        void startLoadData(int pageIndex);

        /**
         * when you generate complete,please call "listView.setAdapter()" at last
         *
         * @param listView
         * @return
         */
        A1 generateAdapter(V1 listView);

        View findById(int id);

        AdapterViewTool<V1, A1, R1> getTool();

    }
}
