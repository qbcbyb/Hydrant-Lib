package com.github.qbcbyb.provider;

import android.database.Cursor;

/**
 * Created by qbcby on 2016/5/26.
 */
public interface SQLiteOperateAction<Bean> {
    void readCursorValue(final Bean bean, final Cursor cursor, final int index);

    Object getFieldValue(final Bean bean);
}
