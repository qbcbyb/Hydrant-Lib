package cn.qbcbyb.lib;

import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshStaggeredGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.origamilabs.library.views.StaggeredGridView;

import java.util.Date;

import cn.qbcbyb.library.activity.BaseActivity;
import cn.qbcbyb.library.activity.CustomViewActionBarActivity;
import cn.qbcbyb.library.adapter.ModelAdapter;
import cn.qbcbyb.library.model.BaseModel;
import cn.qbcbyb.library.util.StringUtils;
import cn.qbcbyb.library.view.ImageViewCustom;


public class MainActivity extends CustomViewActionBarActivity implements PullToRefreshBase.OnRefreshListener2<StaggeredGridView> {//implements FoldableListLayout.OnFoldRotationListener {

    final int[] colorList = {Color.RED, Color.GREEN, Color.BLUE};

    final String DATA = "[{\"id\":632,\"title\":\"三岔口\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_632.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-04 14:19:23\",\"updatetime\":\"2014-12-10 01:37:16\",\"pid\":55880,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":232,\"picHeight\":2400,\"picWidth\":2386,\"period\":\"2011年\",\"uid\":null},{\"id\":29,\"title\":\"丝绸之路\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_29.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:10\",\"updatetime\":\"2014-12-01 07:03:58\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":227,\"picHeight\":1716,\"picWidth\":910,\"period\":\"1996年\",\"uid\":null},{\"id\":30,\"title\":\"丰收图\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_30.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:12\",\"updatetime\":\"2014-12-01 07:03:59\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":144,\"picHeight\":904,\"picWidth\":910,\"period\":\"1996年\",\"uid\":null},{\"id\":648,\"title\":\"西厢记(红娘)\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_648.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"旦\",\"status\":null,\"createtime\":\"2014-12-04 14:25:44\",\"updatetime\":\"2014-12-10 01:36:21\",\"pid\":55880,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":134,\"picHeight\":3174,\"picWidth\":3173,\"period\":\"1999年\",\"uid\":null},{\"id\":651,\"title\":\"锁麟囊\",\"subtitle\":\" \",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_651.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"旦\",\"status\":null,\"createtime\":\"2014-12-04 14:26:39\",\"updatetime\":\"2014-12-13 14:15:02\",\"pid\":55880,\"gid\":9,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":132,\"picHeight\":2280,\"picWidth\":2268,\"period\":\"1986年\",\"uid\":null},{\"id\":65,\"title\":\"马背上的民族\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_65.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:48\",\"updatetime\":\"2014-12-01 07:04:35\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":61,\"picHeight\":635,\"picWidth\":950,\"period\":\"1996年\",\"uid\":null},{\"id\":632,\"title\":\"三岔口\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_632.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-04 14:19:23\",\"updatetime\":\"2014-12-10 01:37:16\",\"pid\":55880,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":232,\"picHeight\":2400,\"picWidth\":2386,\"period\":\"2011年\",\"uid\":null},{\"id\":29,\"title\":\"丝绸之路\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_29.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:10\",\"updatetime\":\"2014-12-01 07:03:58\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":227,\"picHeight\":1716,\"picWidth\":910,\"period\":\"1996年\",\"uid\":null},{\"id\":30,\"title\":\"丰收图\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_30.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:12\",\"updatetime\":\"2014-12-01 07:03:59\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":144,\"picHeight\":904,\"picWidth\":910,\"period\":\"1996年\",\"uid\":null},{\"id\":648,\"title\":\"西厢记(红娘)\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_648.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"旦\",\"status\":null,\"createtime\":\"2014-12-04 14:25:44\",\"updatetime\":\"2014-12-10 01:36:21\",\"pid\":55880,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":134,\"picHeight\":3174,\"picWidth\":3173,\"period\":\"1999年\",\"uid\":null},{\"id\":651,\"title\":\"锁麟囊\",\"subtitle\":\" \",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_55880_651.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":\"宣纸\",\"descri\":\"中国戏剧人物画是以中国舞台戏剧为创作题材，因其戏中有画，画又生戏，故诸多画家倾力描绘，尽显才华。成为中国戏剧的“再创造艺术”，成为中国美术花坛上一朵奇葩。\",\"series\":\"旦\",\"status\":null,\"createtime\":\"2014-12-04 14:26:39\",\"updatetime\":\"2014-12-13 14:15:02\",\"pid\":55880,\"gid\":9,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":132,\"picHeight\":2280,\"picWidth\":2268,\"period\":\"1986年\",\"uid\":null},{\"id\":65,\"title\":\"马背上的民族\",\"subtitle\":\"\",\"url\":\"http://jjhwebstatic.qiniudn.com/paint_54089_65.jpg-t320w\",\"size\":\"96cmx96cm\",\"material\":null,\"descri\":\"反映北方少数民族生活风情及重大历史,用传统工笔重彩手法创作出的佳构。平凡中见伟岸、平和中显沧桑，对历史风貌与时代精神的独特把握。\",\"series\":\"\",\"status\":null,\"createtime\":\"2014-12-01 06:01:48\",\"updatetime\":\"2014-12-01 07:04:35\",\"pid\":54089,\"gid\":null,\"tid\":1,\"page\":null,\"pageSize\":null,\"browse_count\":61,\"picHeight\":635,\"picWidth\":950,\"period\":\"1996年\",\"uid\":null}]";
    private ModelAdapter<PaintModel> adapter;

    private PullToRefreshStaggeredGridView gridView;
//    FoldableListLayout foldable_list;

//    List<LinearLayoutCustom> linearLayoutCustoms;


    public int getActionBarBtnPadding() {
        return 23;
    }

    public int getActionBarBtnVerticalPadding() {
        return 8;
    }

    @Override
    public ImageView getLeftImageView() {
        ImageView leftImageView = super.getLeftImageView();
        leftImageView.setPadding(getActionBarBtnPadding(), getActionBarBtnVerticalPadding(), getActionBarBtnPadding(), getActionBarBtnVerticalPadding());
        return leftImageView;
    }

    @Override
    protected void initActionBarView() {
        super.initActionBarView();
        ImageView leftImageView = getLeftImageView();
        leftImageView.setImageResource(R.drawable.title_back);
        leftImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    protected void doCreate() {
        setContentView(R.layout.activity_main);
        gridView = (PullToRefreshStaggeredGridView) findViewById(R.id.gridView);
        gridView.setMode(PullToRefreshBase.Mode.BOTH);
        gridView.setOnRefreshListener(this);

//        foldable_list = (FoldableListLayout) findViewById(R.id.foldable_list);
//        foldable_list.setOnFoldRotationListener(this);

//        linearLayoutCustoms = new ArrayList<LinearLayoutCustom>() {
//            {
//                for (int i = 0; i < 3; i++) {
//                    LinearLayoutCustom linearLayout = new LinearLayoutCustom(context);
//                    Button button = new Button(context);
//                    button.setText("Button" + i);
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Msg.showInfo(context, ((Button) v).getText().toString() + " clicked");
//                        }
//                    });
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1);
//                    params.setMargins(100, 100, 100, 100);
//                    linearLayout.addView(button, params);
//                    add(linearLayout);
//                }
//            }
//        };
//
//        foldable_list.setAdapter(new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return colorList.length;
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return colorList[position];
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
////                    final DynamicGridView gridView = new DynamicGridView(context);
////                    gridView.setNumColumns(4);
////                    gridView.setAdapter(new CheeseDynamicAdapter(context,
////                            new ArrayList<String>(Arrays.asList(Cheeses.sCheeseStrings)),
////                            4));
////                    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
////                        @Override
////                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////                            gridView.startEditMode(position);
////                            return true;
////                        }
////                    });
////                    convertView=gridView;
//                    convertView = linearLayoutCustoms.get(position);
//                }
//                convertView.setBackgroundColor(colorList[position]);
//                return convertView;
//            }
//        });
////        foldable_list.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                foldable_list.scrollToPosition(1);
////            }
////        }, 3000);
    }

    @Override
    protected void doInit() {
        super.doInit();
        adapter = new ModelAdapter<PaintModel>(context, null, R.layout.layout_paint_listitem,
                new String[]{
                        "browse_count",
                        "title",
                        "subtitle",
                        "url"
                },
                new int[]{
                        R.id.txtBrowseCount,
                        R.id.txtTitle,
                        R.id.txtSubTitle,
                        R.id.imgPaint}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return view;
            }
        };
        adapter.setViewBinder(new ModelAdapter.ViewBinder() {

            public boolean setViewValue(View view, Object o, Object o2, final String s) {
                if (view instanceof ImageView) {
                    PaintModel paint = ((PaintModel) o);
                    ImageViewCustom imageViewCustom = (ImageViewCustom) view;
                    float widthDividedHeight = paint.getPicWidth() / (float) paint.getPicHeight();
                    if (widthDividedHeight == 0 || paint.getPicHeight() == 0) {
                        widthDividedHeight = 1;
                    }
                    imageViewCustom.setWidthDividedHeight(widthDividedHeight);
                    imageViewCustom.requestLayout();
                    ImageLoader.getInstance().displayImage(s, (ImageView) view, BaseActivity.DISPLAY_IMAGE_OPTIONS_OF_LIST);
                    return true;
                } else if (view.getId() == R.id.txtBrowseCount) {
                    ((TextView) view).setText(StringUtils.isNotEmpty(s) ? "浏览 " + s : "暂无浏览");
                    return true;
                } else if (view.getId() == R.id.txtTitle || view.getId() == R.id.txtSubTitle) {
                    final TextView textView = (TextView) view;
//                    textView.post(new Runnable() {
//                        @Override
//                        public void run() {
                    textView.setText(s);
//                        }
//                    });
                    return true;
                }
                return false;
            }
        });
        gridView.getRefreshableView().setAdapter(adapter);
        gridView.post(new Runnable() {
            @Override
            public void run() {
                onPullDownToRefresh(gridView);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onPullDownToRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
        refreshView.onRefreshComplete();
        adapter.changeData(JSON.parseArray(DATA, PaintModel.class));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
        refreshView.onRefreshComplete();
        adapter.addData(JSON.parseArray(DATA, PaintModel.class));
        adapter.notifyDataSetChanged();
    }

//    @Override
//    public void onFoldRotation(float rotation, boolean isFromUser) {
//        int nowIndex = (int) (rotation / 180);
//        if (rotation % 180 == 0) {
//            for (int i = 0; i < linearLayoutCustoms.size(); i++) {
//                LinearLayoutCustom linearLayoutCustom = linearLayoutCustoms.get(i);
//                linearLayoutCustom.setVisibility(rotation / 180 == i ? View.VISIBLE : View.INVISIBLE);
////                linearLayoutCustom.setChecked(rotation / 180 == i);
//            }
//        } else {
//            for (int i = 0; i < 2; i++) {
//                int toShowIndex = i + nowIndex;
//                if (toShowIndex > -1 && toShowIndex < linearLayoutCustoms.size()) {
//                    LinearLayoutCustom linearLayoutCustom = linearLayoutCustoms.get(toShowIndex);
//                    if (linearLayoutCustom.getVisibility() != View.VISIBLE) {
//                        linearLayoutCustom.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        }
//    }


    public static class PaintModel extends BaseModel {

        private static final long serialVersionUID = 124479061512111904L;

        private Integer id;
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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

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
