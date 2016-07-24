package com.github.qbcbyb.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.util.SparseArray;

import java.util.Collection;

public final class SQLiteTable<T> {
    private static final int OFFSET_OF_ITEM = 1 << 15;
    private static final UriMatcher URI_MATCHER = new UriMatcher(-1);

    private static String contentProviderAuthority;
    private static final SparseArray<String> keyList;
    private static final ArrayMap<String, SQLiteTable> tableMap = new ArrayMap<>();

    private final TableColumn<T>[] tableColumns;
    private final int tableIndex;

    private final String tableName;

    static {
        keyList = new SparseArray<>();
    }

    public SQLiteTable(String tableName, TableColumn<T>[] tableColumns) {
        this.tableName = tableName;
        this.tableColumns = tableColumns;
        this.tableIndex = tableMap.size();
        addNewContent(this);
    }


    public final Uri contentUriOfItem(long id) {
        return Uri.parse("content://" + getContentProviderAuthority() + "/" + this.tableName + "/" + id);
    }


    public final Uri getContentUri() {
        return Uri.parse("content://" + getContentProviderAuthority() + "/" + this.tableName);
    }


    public final TableColumn<?>[] getTableColumns() {
        return this.tableColumns;
    }

    public final int getTableIndex() {
        return this.tableIndex;
    }

    public final String getTableName() {
        return this.tableName;
    }

    public final int getUriCodeDir() {
        return this.tableIndex;
    }

    public final int getUriCodeItem() {
        return getUriCodeDir() + OFFSET_OF_ITEM;
    }

    public final String getUriPathDir() {
        return this.tableName;
    }

    public final String getUriPathItem() {
        return this.tableName + "/#";
    }

    public final String getUriTypeDir() {
        return "vnd.android.cursor.dir/" + getContentProviderAuthority() + "." + this.tableName;
    }

    public final String getUriTypeItem() {
        return "vnd.android.cursor.item/" + getContentProviderAuthority() + "." + this.tableName;
    }

    public final String addColumnPrefix(String column) {
        return tableName + "." + column;
    }

    public final String[] addColumnPrefixs(String... columns) {
        if (columns == null) {
            return null;
        }
        int length = columns.length;
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = addColumnPrefix(columns[i]);
        }
        return result;
    }

    private static void addNewContent(SQLiteTable paramSQLiteTable) {
        URI_MATCHER.addURI(getContentProviderAuthority(), paramSQLiteTable.getUriPathDir(), paramSQLiteTable.getUriCodeDir());
        URI_MATCHER.addURI(getContentProviderAuthority(), paramSQLiteTable.getUriPathItem(), paramSQLiteTable.getUriCodeItem());
        String str = paramSQLiteTable.getTableName();
        tableMap.put(str, paramSQLiteTable);
        keyList.put(paramSQLiteTable.getTableIndex(), str);
    }

    public static String getContentProviderAuthority() {
        return SQLiteTable.contentProviderAuthority;
    }

    public static SQLiteTable getTable(int tableIndex) {
        final String key = keyList.get(tableIndex);
        if (!tableMap.containsKey(key)) {
            return null;
        }
        return tableMap.get(key);
    }

    public static SQLiteTable getTable(String tableName) {
        return tableMap.get(tableName);
    }

    public static Collection<SQLiteTable> getTables() {
        return tableMap.values();
    }

    public static void initAuthority(String authority) {
        SQLiteTable.contentProviderAuthority = authority;
    }

    public static SQLiteTable.TableNameAndDir getTableNameDir(Uri uri) {
        int match = URI_MATCHER.match(uri);
        if (match == UriMatcher.NO_MATCH) {
            return null;
        }
        int tableCount = getTables().size();
        boolean isDir = match < tableCount;
        if (!isDir && (match -= OFFSET_OF_ITEM) >= tableCount) {
            return null;
        }
        final SQLiteTable table = SQLiteTable.getTable(match);
        if (table == null) return null;
        return new TableNameAndDir(table.tableName, isDir);
    }

    public static String getUriType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        if (match == UriMatcher.NO_MATCH) {
            return null;
        }
        int tableCount = getTables().size();
        boolean isDir = match < tableCount;
        if (!isDir && (match -= OFFSET_OF_ITEM) >= tableCount) {
            return null;
        }
        final SQLiteTable table = SQLiteTable.getTable(match);
        if (table == null) return null;
        return isDir ? table.getUriTypeDir() : table.getUriTypeItem();
    }

    public static final class TableNameAndDir {
        public final boolean isDir;

        public final String tableName;

        private TableNameAndDir(String tableName, boolean isDir) {
            this.tableName = tableName;
            this.isDir = isDir;
        }

        public String toString() {
            return "TableNameAndDir(tableName=" + this.tableName + ", isDir=" + this.isDir + ")";
        }
    }
}
