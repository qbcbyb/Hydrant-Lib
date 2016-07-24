package com.github.qbcbyb.provider;

import android.content.ContentValues;
import android.database.Cursor;

public interface TableColumn<Bean> extends SQLiteOperateAction<Bean> {

    String getFieldName();

    String getFieldType();

    int getFieldIndex(Cursor cursor);

    void fromCursor(Bean bean, Cursor cursor);

    void toContentValues(Bean bean, ContentValues contentValues);

    DefaultImpls<Bean> getDefaultImpls();

    final class DefaultImpls<Bean> {

        private TableColumn<Bean> tableColumn;
        private String fieldType;

        public DefaultImpls(TableColumn<Bean> tableColumn, String fieldType) {
            this.tableColumn = tableColumn;
            this.fieldType = fieldType;
        }

        public String getFieldType() {
            return fieldType;
        }

        public int getFieldIndex(Cursor cursor) {
            return cursor.getColumnIndex(tableColumn.getFieldName());
        }

        public void fromCursor(Bean bean, Cursor cursor) {
            int i = tableColumn.getFieldIndex(cursor);
            if (cursor.isNull(i)) return;
            this.tableColumn.readCursorValue(bean, cursor, i);
        }

        public void toContentValues(Bean bean, ContentValues contentValues) {
            final Object value;
            if ((value = this.tableColumn.getFieldValue(bean)) == null)
                return;
            final String fieldName = tableColumn.getFieldName();

            if (value instanceof Integer) contentValues.put(fieldName, (Integer) value);
            else if (value instanceof Long) contentValues.put(fieldName, (Long) value);
            else if (value instanceof String) contentValues.put(fieldName, (String) value);
            else if (value instanceof Boolean) contentValues.put(fieldName, (Boolean) value);
            else if (value instanceof Double) contentValues.put(fieldName, (Double) value);
            else if (value instanceof Float) contentValues.put(fieldName, (Float) value);
            else if (value instanceof Short) contentValues.put(fieldName, (Short) value);
            else if (value instanceof Byte) contentValues.put(fieldName, (Byte) value);
            else if (value instanceof byte[]) contentValues.put(fieldName, (byte[]) value);
        }
    }
}