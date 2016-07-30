package com.github.qbcbyb.provider;

import android.content.ContentValues;
import android.database.Cursor;

public interface TableModel<T, R extends TableColumn<T>> {
    R[] defaultTableColumnArr();

    void fromCursor(Cursor cursor);

    ContentValues toContentValues();

    ContentValues toContentValues(R[] tableColumns);

    class DefaultImpls<T extends TableModel<T, R>, R extends TableColumn<T>> {
        private final T tableModel;
        private Long _id;

        public DefaultImpls(T tableModel) {
            this.tableModel = tableModel;
        }

        public Long get_id() {
            return _id;
        }

        public void set_id(Long _id) {
            this._id = _id;
        }

        public void fromCursor(Cursor cursor) {
            for (R column : tableModel.defaultTableColumnArr()) {
                column.fromCursor(tableModel, cursor);
            }
        }

        public ContentValues toContentValues() {
            return toContentValues(tableModel.defaultTableColumnArr());
        }

        public ContentValues toContentValues(R[] tableColumns) {
            ContentValues values = new ContentValues();
            for (TableColumn<T> column : tableColumns) {
                column.toContentValues(tableModel, values);
            }
            return values;
        }
    }
}
