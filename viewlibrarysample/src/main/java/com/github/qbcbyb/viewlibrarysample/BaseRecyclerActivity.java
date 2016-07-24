package com.github.qbcbyb.viewlibrarysample;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.github.qbcbyb.viewlibrary.recycler.SnappyRecyclerView;
import com.github.qbcbyb.viewlibrary.recycler.adapter.SimpleRecyclerAdapter;
import com.github.qbcbyb.viewlibrary.recycler.adapter.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Arrays;

import cn.qbcbyb.library.activity.BaseActivity;

/**
 * Created by qbcby on 2016/6/14.
 */
public abstract class BaseRecyclerActivity extends BaseActivity {

    protected SnappyRecyclerView recyclerView;

    @Override
    protected void doCreate() {
        recyclerView = new SnappyRecyclerView(this);
        setContentView(recyclerView);
        SimpleRecyclerAdapter<String> adapter = new SimpleRecyclerAdapter<>(this, getItemLayoutId(), Holder.class);
        adapter.changeData(Arrays.asList(Constants.getImageUrls()));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(generateLayoutManager());
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_recycler, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuTo20) {
            recyclerView.smoothScrollToPosition(19);
        } else if (item.getItemId() == R.id.menuTo40) {
            recyclerView.smoothScrollToPosition(39);
        } else if (item.getItemId() == R.id.menuTo60) {
            recyclerView.smoothScrollToPosition(59);
        } else if (item.getItemId() == R.id.menuTo80) {
            recyclerView.smoothScrollToPosition(79);
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getItemLayoutId();

    protected abstract RecyclerView.LayoutManager generateLayoutManager();

    private static class Holder extends ViewHolder<String> {
        private final ImageView image;

        public Holder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            addRound(itemView);
        }

        private void addRound(View itemView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final float radius = 5;
                final ViewOutlineProvider provider = new ViewOutlineProvider() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
                    }
                };
                itemView.setOutlineProvider(provider);
                itemView.setClipToOutline(true);
            }
        }

        @Override
        public void bindData(String data, int position) {
            ImageLoader.getInstance().displayImage(data, image);
        }
    }
}
