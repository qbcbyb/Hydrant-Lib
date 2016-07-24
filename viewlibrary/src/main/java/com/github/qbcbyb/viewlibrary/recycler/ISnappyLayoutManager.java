package com.github.qbcbyb.viewlibrary.recycler;

import android.graphics.Point;
import android.support.v7.widget.RecyclerView;

/**
 * Created by qbcby on 2016/6/13.
 */
public interface ISnappyLayoutManager {

    boolean fling(RecyclerView recyclerView, int velocityX, int velocityY);

    void smoothScrollToNearestPosition(int velocityX, int velocityY);
}
