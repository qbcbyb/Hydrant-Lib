package cn.qbcbyb.lib;

import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;
import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.Arrays;

import cn.qbcbyb.library.activity.BaseActivity;


public class MainActivity3 extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, DynamicGridView.OnDragListener {

    DynamicGridView gridView;
    ImageView image;

    @Override
    protected void doCreate() {
        setContentView(R.layout.activity_main_activity3);
        image= (ImageView) findViewById(R.id.image);
        ImageLoader.getInstance().displayImage("http://jjhwebstatic.qiniudn.com/painter_head_55880.jpg",image);
        gridView = (DynamicGridView) findViewById(R.id.gridView);
        View v = new View(context);
        v.setLayoutParams(new ViewGroup.LayoutParams(-1, 400));
        v.setBackgroundColor(Color.RED);
        gridView.addHeaderView(v, null, false);
        v = new View(context);
        v.setLayoutParams(new ViewGroup.LayoutParams(-1, 400));
        v.setBackgroundColor(Color.RED);
        gridView.addFooterView(v, null, false);

        BaseDynamicGridAdapter adapter = new CheeseDynamicAdapter(this,
                new ArrayList<String>(Arrays.asList(Cheeses.sCheeseStrings)),
                3);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        gridView.setOnDragListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity3, menu);
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
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        gridView.startEditMode(position);
        return false;
    }

    @Override
    public void onDragStarted(int position) {

    }

    @Override
    public void onDragPositionsChanged(int oldPosition, int newPosition) {
    }
}
