package com.github.qbcbyb.viewlibrarysample;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import cn.qbcbyb.library.activity.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button btnNormalPager;
    private Button btnPager;
    private Button btnDeck;
    private Button btnResize;

    @Override
    protected void doCreate() {
        setFinishConfirm(true);
        setContentView(R.layout.activity_main);

        btnNormalPager = (Button) findViewById(R.id.btnNormalPager);
        btnPager = (Button) findViewById(R.id.btnPager);
        btnDeck = (Button) findViewById(R.id.btnDeck);
        btnResize = (Button) findViewById(R.id.btnResize);

        btnNormalPager.setOnClickListener(this);
        btnPager.setOnClickListener(this);
        btnDeck.setOnClickListener(this);
        btnResize.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnNormalPager) {
            Intent intent = new Intent(this, PagerActivity.class);
            startActivity(intent);
        } else if (v == btnPager) {
            Intent intent = new Intent(this, PagerActivity.class);
            intent.putExtra(PagerActivity.KEY_OPEN_NORMAL, false);
            startActivity(intent);
        } else if (v == btnDeck) {
            startActivity(new Intent(this, DeckActivity.class));
        } else if (v == btnResize) {
            startActivity(new Intent(this, ResizeActivity.class));
        }
    }
}
