package com.github.qbcbyb.provider.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseArray;

import com.github.qbcbyb.provider.ContentProviderUtil;
import com.github.qbcbyb.provider.SQLiteTable;

/**
 * Created by qbcby on 2016/7/30.
 */
public class TestSQLiteOpenHelper extends  SQLiteOpenHelper {

    public static final String ALTER_TABLE_S_ADD_COLUMN_S_S = "ALTER TABLE %s ADD COLUMN %s %s;";

    public TestSQLiteOpenHelper(Context context) {
        super(context, "replace_your_DB_NAME", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db != null) {
            try {
                for (SQLiteTable table : SQLiteTable.getTables()) {
                    ContentProviderUtil.createTable(db, table);
                }
            } finally {
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db != null) {
            SparseArray<UpgradeAction> upgrades = new SparseArray<>();
//            upgrades.put(5, upgradeFromV5);//use on upgrade
            for (int i = oldVersion; i < newVersion; i++) {
                upgrades.get(i).onUpgrade(db);
            }
        }
    }

    private interface UpgradeAction {
        void onUpgrade(SQLiteDatabase db);
    }


//    private UpgradeAction upgradeFromV5 = new UpgradeAction() { // use on upgrade
//        @Override
//        public void onUpgrade(SQLiteDatabase db) {
//            ContentProviderUtil.createTable(db, Tables.ShareWalletInfoTable);
//        }
//    };
}
