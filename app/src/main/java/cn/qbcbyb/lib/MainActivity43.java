package cn.qbcbyb.lib;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import cn.qbcbyb.lib.views.DeckChildView;
import cn.qbcbyb.lib.views.DeckView;
import cn.qbcbyb.library.activity.BaseActivity;
import cn.qbcbyb.library.model.BaseModel;
import cn.qbcbyb.library.view.ImageViewCustom;


public class MainActivity43 extends ActionBarActivity {
    private DeckView deckview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_43);
        deckview = (DeckView) findViewById(R.id.deckview);
        final List<PaintModel> models = JSON.parseArray(MainActivity42Activity.DATA, PaintModel.class);

        deckview.initialize(new DeckView.Callback() {
            @Override
            public List getData() {
                return models;
            }

            @Override
            public void loadViewData(WeakReference<DeckChildView> dcv, int item) {
                DeckChildView v = dcv.get();
                TextView txtBrowseCount = (TextView) v.findViewById(R.id.txtBrowseCount);
                TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
                TextView txtSubTitle = (TextView) v.findViewById(R.id.txtSubTitle);
                ImageViewCustom imgPaint = (ImageViewCustom) v.findViewById(R.id.imgPaint);

                PaintModel paintModel = models.get(item);
                txtBrowseCount.setText(paintModel.getBrowse_count() != null ? "浏览 " + paintModel.getBrowse_count() : "暂无浏览");
                txtTitle.setText(paintModel.getTitle());
                txtSubTitle.setText(paintModel.getSubtitle());
                ImageLoader.getInstance().displayImage(paintModel.getUrl(), imgPaint, BaseActivity.DISPLAY_IMAGE_OPTIONS_OF_LIST);
            }

            @Override
            public void unloadViewData(int item) {

            }

            @Override
            public void onViewDismissed(int item) {

            }

            @Override
            public void onItemClick(int item) {

            }

            @Override
            public void onNoViewsToDeck() {

            }

            @Override
            public DeckChildView onCreateNewDeckChildView(Context context, DeckView deckView) {
                DeckChildView view = new DeckChildView(context);
                LayoutInflater.from(context).inflate(R.layout.layout_paint_listitem, view, true);
                return view;
            }
        });
        deckview.post(new Runnable() {
            @Override
            public void run() {
                deckview.scrollToChild(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_43, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PaintModel extends BaseModel {

        private static final long serialVersionUID = 124479061512111904L;

        private String title;
        private String subtitle;
        private String url;
        private String size;
        private String material;
        private String descri;
        private String series;
        private Integer status;
        private Date createtime;
        private Date updatetime;
        private Integer pid;
        private Integer gid;
        private Integer tid;
        private Integer page;
        private Integer pageSize;
        private Integer browse_count;
        private Integer picHeight;
        private Integer picWidth;
        private String period;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public String getDescri() {
            return descri;
        }

        public void setDescri(String descri) {
            this.descri = descri;
        }

        public String getSeries() {
            return series;
        }

        public void setSeries(String series) {
            this.series = series;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Date getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Date createtime) {
            this.createtime = createtime;
        }

        public Date getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(Date updatetime) {
            this.updatetime = updatetime;
        }

        public Integer getPid() {
            return pid;
        }

        public void setPid(Integer pid) {
            this.pid = pid;
        }

        public Integer getGid() {
            return gid;
        }

        public void setGid(Integer gid) {
            this.gid = gid;
        }

        public Integer getTid() {
            return tid;
        }

        public void setTid(Integer tid) {
            this.tid = tid;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getBrowse_count() {
            return browse_count;
        }

        public void setBrowse_count(Integer browse_count) {
            this.browse_count = browse_count;
        }

        public Integer getPicHeight() {
            return picHeight;
        }

        public void setPicHeight(Integer picHeight) {
            this.picHeight = picHeight;
        }

        public Integer getPicWidth() {
            return picWidth;
        }

        public void setPicWidth(Integer picWidth) {
            this.picWidth = picWidth;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }
    }
}
