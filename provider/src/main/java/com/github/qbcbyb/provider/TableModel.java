package com.github.qbcbyb.provider;

import android.content.ContentValues;
import android.database.Cursor;

public interface TableModel<T, R extends TableColumn<T>> {
    R[] defaultTableColumnArr();

    void fromCursor(Cursor cursor);

    ContentValues toContentValues();

    ContentValues toContentValues(R[] tableColumns);
}
