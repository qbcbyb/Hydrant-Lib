package cn.qbcbyb.lib;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;
import java.util.List;

import cn.qbcbyb.lib.views.DeckViewLayoutAlgorithm;
import cn.qbcbyb.lib.views.DeckViewScroller;
import cn.qbcbyb.library.activity.BaseActivity;
import cn.qbcbyb.library.model.BaseModel;
import cn.qbcbyb.library.view.ImageViewCustom;


public class MainActivity42Activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String DATA = "[{\"id\":632,\"title\":\"三岔口\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_632.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-04 14:19:23\",\"updatetime\":\"2014-12-10 01:37:16\",\"pid\":55880,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":232,\"picHeight\":2400,\"picWidth\":2386,\"period\":\"2011年\",\"uid\":null},{\"id\":29,\"title\":\"丝绸之路\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_29.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:10\",\"updatetime\":\"2014-12-01 07:03:58\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":227,\"picHeight\":1716,\"picWidth\":910,\"period\":\"1996年\",\"uid\":null},{\"id\":30,\"title\":\"丰收图\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_30.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:12\",\"updatetime\":\"2014-12-01 07:03:59\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":144,\"picHeight\":904,\"picWidth\":910,\"period\":\"1996年\",\"uid\":null},{\"id\":648,\"title\":\"西厢记(红娘)\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_648.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"旦\",\"status\":null,\"createtime\":\"2014-12-04 14:25:44\",\"updatetime\":\"2014-12-10 01:36:21\",\"pid\":55880,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":134,\"picHeight\":3174,\"picWidth\":3173,\"period\":\"1999年\",\"uid\":null},{\"id\":651,\"title\":\"锁麟囊\",\"subtitle\":\" \",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_651.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"旦\",\"status\":null,\"createtime\":\"2014-12-04 14:26:39\",\"updatetime\":\"2014-12-13 14:15:02\",\"pid\":55880,\"gid\":9,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":132,\"picHeight\":2280,\"picWidth\":2268,\"period\":\"1986年\",\"uid\":null},{\"id\":65,\"title\":\"马背上的民族\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_65.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:48\",\"updatetime\":\"2014-12-01 07:04:35\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":61,\"picHeight\":635,\"picWidth\":950,\"period\":\"1996年\",\"uid\":null},{\"id\":632,\"title\":\"三岔口\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_632.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-04 14:19:23\",\"updatetime\":\"2014-12-10 01:37:16\",\"pid\":55880,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":232,\"picHeight\":2400,\"picWidth\":2386,\"period\":\"2011年\",\"uid\":null},{\"id\":29,\"title\":\"丝绸之路\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_29.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:10\",\"updatetime\":\"2014-12-01 07:03:58\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":227,\"picHeight\":1716,\"picWidth\":910,\"period\":\"1996年\",\"uid\":null},{\"id\":30,\"title\":\"丰收图\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_30.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:12\",\"updatetime\":\"2014-12-01 07:03:59\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":144,\"picHeight\":904,\"picWidth\":910,\"period\":\"1996年\",\"uid\":null},{\"id\":648,\"title\":\"西厢记(红娘)\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_648.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"旦\",\"status\":null,\"createtime\":\"2014-12-04 14:25:44\",\"updatetime\":\"2014-12-10 01:36:21\",\"pid\":55880,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":134,\"picHeight\":3174,\"picWidth\":3173,\"period\":\"1999年\",\"uid\":null},{\"id\":651,\"title\":\"锁麟囊\",\"subtitle\":\" \",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_651.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"旦\",\"status\":null,\"createtime\":\"2014-12-04 14:26:39\",\"updatetime\":\"2014-12-13 14:15:02\",\"pid\":55880,\"gid\":9,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":132,\"picHeight\":2280,\"picWidth\":2268,\"period\":\"1986年\",\"uid\":null},{\"id\":65,\"title\":\"马背上的民族\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_65.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:48\",\"updatetime\":\"2014-12-01 07:04:35\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":61,\"picHeight\":635,\"picWidth\":950,\"period\":\"1996年\",\"uid\":null}]";

    protected ExpandableRecyclerView mRecyclerView;
    protected SwipeRefreshLayout swipe_container;
    protected CustomAdapter mAdapter;

//    private JazzyRecyclerViewScrollListener jazzyScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity42);
        mRecyclerView = (ExpandableRecyclerView) findViewById(R.id.recyclerView);
        swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipe_container.setOnRefreshListener(this);

//        mRecyclerView.addOnScrollListener(new MyOnScrollListener());
//        mRecyclerView.setItemAnimator(new MyItemAnimator());
//        jazzyScrollListener = new JazzyRecyclerViewScrollListener();
//        mRecyclerView.addOnScrollListener(jazzyScrollListener);
//        jazzyScrollListener.setTransitionEffect(JazzyHelper.CARDS);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int firstVisibleItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
//                int visibleItemCount = recyclerView.getChildCount();
//                int totalItemCount = recyclerView.getAdapter().getItemCount();
//                float x = recyclerView.getHeight() / 104f;
//                for (int i = 0; i < visibleItemCount; i++) {
//                    View view = recyclerView.getChildAt(i);
//                    view.getLayoutParams().height = (int) (Math.pow(3, i) * x);
//                }
//                recyclerView.requestLayout();
//            }
//        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mAdapter = new CustomAdapter(JSON.parseArray(DATA, PaintModel.class));
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity42, menu);
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

    @Override
    public void onRefresh() {
        swipe_container.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe_container.setRefreshing(false);
            }
        }, 2000);
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtBrowseCount;
        private final TextView txtTitle;
        private final TextView txtSubTitle;
        private final ImageViewCustom imgPaint;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ViewHolder", "Element " + getAdapterPosition() + " , " + getLayoutPosition() + " clicked.");
                    if (!mRecyclerView.isInExpandMode()) {
                        mRecyclerView.setExpandInIndex(getAdapterPosition(), 500);
                    }
                }
            });
            txtBrowseCount = (TextView) v.findViewById(R.id.txtBrowseCount);
            txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) v.findViewById(R.id.txtSubTitle);
            imgPaint = (ImageViewCustom) v.findViewById(R.id.imgPaint);

//                    "browse_count",
//                    "title",
//                    "subtitle",
//                    "url"
        }

        public void setData(PaintModel paintModel) {
            txtBrowseCount.setText(paintModel.getBrowse_count() != null ? "浏览 " + paintModel.getBrowse_count() : "暂无浏览");
            txtTitle.setText(paintModel.getTitle());
            txtSubTitle.setText(paintModel.getSubtitle());

//            float widthDividedHeight = paintModel.getPicWidth() / (float) paintModel.getPicHeight();
//            if (widthDividedHeight == 0 || paintModel.getPicHeight() == 0) {
//                widthDividedHeight = 1;
//            }
//            imgPaint.setWidthDividedHeight(widthDividedHeight);
//            imgPaint.requestLayout();
            ImageLoader.getInstance().displayImage(paintModel.getUrl(), imgPaint, BaseActivity.DISPLAY_IMAGE_OPTIONS_OF_LIST);
        }
    }

    public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static final String TAG = "CustomAdapter";

        private List<PaintModel> mDataSet;


        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
         */
        public CustomAdapter(List<PaintModel> dataSet) {
            mDataSet = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_paint_listitem, viewGroup, false);

            v.getLayoutParams().height = viewGroup.getHeight() / 5;
            return new ViewHolder(v);
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            Log.d(TAG, "Element " + position + " set.");

            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            viewHolder.setData(mDataSet.get(position));
        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
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

//    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
//        private int lastPosition = 0;
//
//        public void setLastPosition(int lastPosition) {
//            if (this.lastPosition != lastPosition) {
//                this.lastPosition = lastPosition;
//                DebugUtil.d("setLastPosition", "lastPosition:" + lastPosition + ",childcount:" + mRecyclerView.getChildCount());
////                mAdapter.notifyItemInserted(lastPosition);
//                mAdapter.notifyItemChanged(lastPosition);
//            }
//        }
//
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            DebugUtil.d("onScrolled", "dx:" + dx + ",dy:" + dy);
//            View lastView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
//            int lastItemPosition = recyclerView.getChildAdapterPosition(lastView);
//            setLastPosition(lastItemPosition);
//        }
//    }


//    public class MyItemAnimator extends RecyclerView.ItemAnimator {
//
//        List<RecyclerView.ViewHolder> mAnimationAddViewHolders = new ArrayList<RecyclerView.ViewHolder>();
//        List<RecyclerView.ViewHolder> mAnimationChangeViewHolders = new ArrayList<RecyclerView.ViewHolder>();
//        List<RecyclerView.ViewHolder> mAnimationRemoveViewHolders = new ArrayList<RecyclerView.ViewHolder>();
//
//        //需要执行动画时会系统会调用，用户无需手动调用
//        @Override
//        public void runPendingAnimations() {
//            if (!mAnimationChangeViewHolders.isEmpty()) {
//                AnimatorSet animator;
//                View target;
//                for (final RecyclerView.ViewHolder viewHolder : mAnimationChangeViewHolders) {
//                    target = viewHolder.itemView;
//                    animator = new AnimatorSet();
//
//                    animator.playTogether(
//                            ObjectAnimator.ofFloat(target, "translationY", target.getMeasuredHeight(), 0.0f),
//                            ObjectAnimator.ofFloat(target, "alpha", ViewHelper.getAlpha(target), 1.0f)
//                    );
//
//                    animator.setTarget(target);
//                    animator.setDuration(200);
//                    animator.addListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            mAnimationChangeViewHolders.remove(viewHolder);
//                            if (!isRunning()) {
//                                dispatchAnimationsFinished();
//                            }
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    });
//                    animator.start();
//                }
//            } else if (!mAnimationAddViewHolders.isEmpty()) {
//
//            } else if (!mAnimationRemoveViewHolders.isEmpty()) {
//            }
//        }
//
//        //remove时系统会调用，返回值表示是否需要执行动画
//        @Override
//        public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
//            return mAnimationRemoveViewHolders.add(viewHolder);
//        }
//
//        //viewholder添加时系统会调用
//        @Override
//        public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
//            return mAnimationAddViewHolders.add(viewHolder);
//        }
//
//        @Override
//        public boolean animateMove(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
//            return false;
//        }
//
//        @Override
//        public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
//            return mAnimationChangeViewHolders.add(oldHolder);
//        }
//
//        @Override
//        public void endAnimation(RecyclerView.ViewHolder viewHolder) {
//        }
//
//        @Override
//        public void endAnimations() {
//        }
//
//        @Override
//        public boolean isRunning() {
//            return !(mAnimationAddViewHolders.isEmpty() && mAnimationRemoveViewHolders.isEmpty() && mAnimationChangeViewHolders.isEmpty());
//        }
//
//    }

}
