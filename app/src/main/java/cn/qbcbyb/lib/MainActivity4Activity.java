package cn.qbcbyb.lib;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;

import cn.qbcbyb.library.adapter.ModelPagerAdapter;
import cn.qbcbyb.library.view.PagerTabStrip;


public class MainActivity4Activity extends ActionBarActivity {

    ViewPager viewPager;
    PagerTabStrip viewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity4);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerIndicator = (PagerTabStrip) findViewById(R.id.viewPagerIndicator);

        viewPager.setAdapter(new ModelPagerAdapter<Integer>(this, Arrays.asList(R.drawable.main_1,R.drawable.main_2,R.drawable.main_3)) {
            @Override
            protected View createNewView(Integer data, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(data);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
        viewPagerIndicator.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity4, menu);
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
}
