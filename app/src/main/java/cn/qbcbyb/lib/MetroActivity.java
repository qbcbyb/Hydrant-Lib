package cn.qbcbyb.lib;

import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.origamilabs.library.views.StaggeredGridView;

import java.util.List;

import cn.qbcbyb.library.activity.CustomViewActionBarActivity;
import cn.qbcbyb.library.adapter.ModelAdapter;
import cn.qbcbyb.library.model.BaseModel;
import cn.qbcbyb.library.util.ResourcesManager;
import cn.qbcbyb.library.view.MetroItemView;

/**
 * Created by 秋云 on 2015/2/3.
 */
public class MetroActivity extends CustomViewActionBarActivity {

    private StaggeredGridView staggeredGridView;
    private ModelAdapter<JumpData> jumpDataModelAdapter;

    @Override
    protected void doCreate() {
        staggeredGridView = new StaggeredGridView(this);
        staggeredGridView.setColumnCount(4);
        staggeredGridView.setItemMargin(4);
        setContentView(staggeredGridView);
        initGridView();
    }

    private void initGridView() {
        List<JumpData> jumpDataList = JSON.parseArray("[{\"display\":\"生\",\"queryType\":\"Series\",\"catalogType\":\"Painter\",\"span\":\"2\",\"drawable\":\"main_1\"},{\"display\":\"旦\",\"queryType\":\"Series\",\"catalogType\":\"Painter\",\"span\":\"2\",\"drawable\":\"main_2\"},{\"display\":\"净\",\"queryType\":\"Series\",\"catalogType\":\"Painter\",\"span\":\"2\",\"drawable\":\"main_3\"},{\"display\":\"丑\",\"queryType\":\"Series\",\"catalogType\":\"Painter\",\"span\":\"2\",\"drawable\":\"main_4\"},{\"display\":\"作品精选\",\"queryType\":\"Series\",\"catalogType\":\"Painter\",\"span\":\"4\",\"drawable\":\"main_5\"},{\"display\":\"杨贵妃剧目\",\"queryType\":\"Series\",\"catalogType\":\"Painter\",\"span\":\"2\",\"drawable\":\"main_6\"},{\"display\":\"四郎探母剧目\",\"queryType\":\"Series\",\"catalogType\":\"Painter\",\"span\":\"2\",\"drawable\":\"main_7\"},{\"display\":\"西厢记剧目\",\"queryType\":\"Series\",\"catalogType\":\"Painter\",\"span\":\"2\",\"drawable\":\"main_8\"},{\"display\":\"将相和剧目\",\"queryType\":\"Series\",\"catalogType\":\"Painter\",\"span\":\"2\",\"drawable\":\"main_9\"}]", JumpData.class);
        jumpDataModelAdapter = new ModelAdapter<JumpData>(context, jumpDataList, R.layout.layout_personal_main_item,
                new String[]{"display"},
                new int[]{R.id.itemMetro}) {
//            @Override
//            public int getItemViewType(int position) {
//                JumpData item = (JumpData) getItem(position);
//                return item.getCatalogType() == null ? 0 : 1;
//            }

//            @Override
//            public int getViewTypeCount() {
//                return 2;
//            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = null;
//                if (getItemViewType(position) == 1) {
                view = super.getView(position, convertView, parent);
//                } else {
//                    if (convertView == null) {
//                        view = LayoutInflater.from(PersonalMainFragment.this.getActivity()).inflate(R.layout.layout_personal_main_title, parent, false);
//
//                        InitModel initConfig = SyApplication.getInstance().getInitConfig();
//                        if (initConfig != null && initConfig.getPainter() != null) {
//                            final ImageView imgUserImg = (ImageView) view.findViewById(R.id.imgUserImg);
//                            TextView txtUserName = (TextView) view.findViewById(R.id.txtUserName);
//                            TextView txtUserDesc = (TextView) view.findViewById(R.id.txtUserDesc);
//
//                            final PainterModel painter = initConfig.getPainter();
//                            txtUserName.setText(painter.getName());
//                            txtUserDesc.setText(painter.getDescri());
//                            ImageManager.setImage(imgUserImg, painter.getHead(), SyApplication.getDefaultDiaplayImageOptionsBuilder()
//                                    .showImageOnLoading(R.drawable.main_user_icon)
//                                    .showImageForEmptyUri(R.drawable.main_user_icon)
//                                    .showImageOnFail(R.drawable.main_user_icon)
//                                    .build());
//                        }
//                        MetroLayout.LayoutParams layoutParams = ((MetroLayout.LayoutParams) view.getLayoutParams());
//                        layoutParams.setColumnSpan(4);
//                    } else {
//                        view = convertView;
//                    }
//                }
                return view;
            }
        };
        jumpDataModelAdapter.setViewBinder(new ModelAdapter.ViewBinder() {
            public boolean setViewValue(View view, Object o, Object o2, String s) {
                MetroItemView itemView = ((MetroItemView) view);
                JumpData data = ((JumpData) o);
                itemView.setImageResource(ResourcesManager.getDrawable(data.getDrawable(), getPackageName()));
                itemView.setText(data.getDisplay());
                StaggeredGridView.LayoutParams layoutParams = ((StaggeredGridView.LayoutParams) view.getLayoutParams());
                layoutParams.span = data.getSpan();
                return true;
            }
        });
        staggeredGridView.setAdapter(jumpDataModelAdapter);
    }

    public enum QueryType {
        AllWork, Recommend, Series
    }

    public enum CatalogType {
        Topic(1, "话题分类", true),
        Paint(2, "作品分类", false),
        Painter(3, "画家", false),
        Gallery(4, "画廊", false),
        PaintDetail(5, "作品详情", false),
        TopicDetail(6, "话题详情", true);

        private int intValue;
        private String catalogName;
        private boolean isHuati;

        CatalogType(int intValue, String catalogName, boolean isHuati) {
            this.intValue = intValue;
            this.catalogName = catalogName;
            this.isHuati = isHuati;
        }

        public int getIntValue() {
            return intValue;
        }

        public String getCatalogName() {
            return catalogName;
        }

        public boolean isHuati() {
            return isHuati;
        }

        public static CatalogType fromIntValue(int intValue) {
            for (CatalogType catalogType : CatalogType.values()) {
                if (catalogType.intValue == intValue) {
                    return catalogType;
                }
            }
            return null;
        }
    }

    public static class JumpData extends BaseModel {

        private static final long serialVersionUID = 8822425716487237820L;
        private String display;
        private String title;
        private String search;
        private String url;
        private QueryType queryType;
        private String tag;
        private int span = 1;
        private String drawable;
        private CatalogType catalogType;

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getTitle() {
            return title == null ? getDisplay() : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSearch() {
            return search == null ? getTitle() : search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public QueryType getQueryType() {
            return queryType;
        }

        public void setQueryType(String queryType) {
            this.queryType = QueryType.valueOf(queryType);
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getSpan() {
            return span;
        }

        public void setSpan(int span) {
            this.span = span;
        }

        public String getDrawable() {
            return drawable;
        }

        public void setDrawable(String drawable) {
            this.drawable = drawable;
        }

        public CatalogType getCatalogType() {
            return catalogType;
        }

        public void setCatalogType(String catalogType) {
            this.catalogType = CatalogType.valueOf(catalogType);
        }
    }
}
