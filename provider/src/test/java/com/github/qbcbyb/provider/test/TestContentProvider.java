package com.github.qbcbyb.provider.test;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.qbcbyb.provider.ContentProvider;
import com.github.qbcbyb.provider.SQLiteTable;

/**
 * Created by qbcby on 2016/7/30.
 */
public class TestContentProvider extends ContentProvider {
    static {
        SQLiteTable.initAuthority("replace_your_Authority_Name");
        Tables.init();
        ContentProvider.DEBUG = BuildConfig.DEBUG;
    }
    @Override
    public SQLiteOpenHelper getSQLiteOpenHelper(Context context) {
        return new TestSQLiteOpenHelper(context);
    }
}
