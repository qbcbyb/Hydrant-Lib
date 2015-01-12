package cn.qbcbyb.lib;

import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.origamilabs.library.views.StaggeredGridView;

import cn.qbcbyb.library.activity.CustomViewActionBarActivity;


public class MainActivity extends CustomViewActionBarActivity {//implements FoldableListLayout.OnFoldRotationListener {

    final int[] colorList = {Color.RED, Color.GREEN, Color.BLUE};

    private StaggeredGridView gridView;
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
        gridView = (StaggeredGridView) findViewById(R.id.gridView);
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
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getItemViewType(int position) {
                return position == 0 ? 0 : 1;
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getCount() {
                return 20;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new View(context);
                }
                StaggeredGridView.LayoutParams params = (StaggeredGridView.LayoutParams) convertView.getLayoutParams();
                if (params == null) {
                    params = new StaggeredGridView.LayoutParams(100);
                    convertView.setLayoutParams(params);
                }
                params.span = position == 0 ? 2 : 1;
                convertView.setBackgroundColor(colorList[position % 3]);
                return convertView;
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
}
