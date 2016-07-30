package com.github.qbcbyb.provider.test;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.qbcbyb.provider.TableColumn;

/**
 * Created by qbcby on 2016/7/30.
 */
public enum ConversationTableColumn implements TableColumn<Conversation> {
    _id("INTEGER PRIMARY KEY AUTOINCREMENT") {
        @Override
        public void readCursorValue(Conversation bean, Cursor cursor, int index) {
            bean.set_id(cursor.getLong(index));
        }
    },
    title("TEXT") {
        @Override
        public void readCursorValue(Conversation bean, Cursor cursor, int index) {
            bean.setTitle(cursor.getString(index));
        }

        @Override
        public Object getFieldValue(Conversation bean) {
            return bean.getTitle();
        }
    }, unReadCount("INTEGER") {
        @Override
        public void readCursorValue(Conversation bean, Cursor cursor, int index) {
            bean.setUnReadCount(cursor.getLong(index));
        }

        @Override
        public Object getFieldValue(Conversation bean) {
            return bean.getUnReadCount();
        }
    };

    private DefaultImpls<Conversation> impls;

    ConversationTableColumn(String fieldType) {
        this.impls = new DefaultImpls<>(this, fieldType);
    }

    @Override
    public String getFieldName() {
        return name();
    }

    @Override
    public String getFieldType() {
        return impls.getFieldType();
    }

    @Override
    public int getFieldIndex(Cursor cursor) {
        return impls.getFieldIndex(cursor);
    }

    @Override
    public void fromCursor(Conversation bean, Cursor cursor) {
        impls.fromCursor(bean, cursor);
    }

    @Override
    public void toContentValues(Conversation bean, ContentValues
            contentValues) {
        impls.toContentValues(bean, contentValues);
    }

    @Override
    public DefaultImpls<Conversation> getDefaultImpls() {
        return impls;
    }

    @Override
    public void readCursorValue(Conversation bean, Cursor cursor, int index) {
    }

    @Override
    public Object getFieldValue(Conversation bean) {
        return null;
    }

    private static String[] columnNameArr;

    public static String[] columnNames() {
        if (columnNameArr == null) {
            synchronized (ConversationTableColumn.class) {
                if (columnNameArr == null) {
                    final ConversationTableColumn[] values = values();
                    final int length = values.length;
                    columnNameArr = new String[length];
                    for (int i = 0; i < length; i++) {
                        columnNameArr[i] = values[i].getFieldName();
                    }
                }
            }
        }
        return columnNameArr;
    }
}
