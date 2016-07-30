package com.github.qbcbyb.provider;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by qbcby on 2016/4/20.
 */
public class ContentProviderUtil {

    public static final String QUERYPARAM_GROUPBY = "QUERYPARAM_GROUPBY";
    public static final String QUERYPARAM_HAVING = "QUERYPARAM_HAVING";
    public static final String QUERYPARAM_LIMIT = "QUERYPARAM_LIMIT";
    public static final String QUERYPARAM_UNIONALL = "QUERYPARAM_UNIONALL";
    public static final String QUERYPARAM_NEEDNOTIFY = "QUERYPARAM_NEEDNOTIFY";
    public static final String QUERYPARAM_JOIN = "QUERYPARAM_JOIN";

    public static final String QUERYPARAM_JOINTYPE = "QUERYPARAM_JOINTYPE";
    public static final String QUERYPARAM_JOINLEFTCOLUMN = "QUERYPARAM_JOINLEFTCOLUMN";
    public static final String QUERYPARAM_JOINRIGHTCOLUMN = "QUERYPARAM_JOINRIGHTCOLUMN";

    public enum JoinType {
        Inner("INNER JOIN"), Left("LEFT JOIN"), Right("RIGHT JOIN"), Full("FULL JOIN");
        public final String joinString;

        JoinType(String joinString) {
            this.joinString = joinString;
        }
    }

    public static void createTable(SQLiteDatabase db, SQLiteTable table) {
        TableColumn[] tableColumns = table.getTableColumns();

        final StringBuilder builder = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ")
                .append(table.getTableName())
                .append(" (");
        if (tableColumns != null && tableColumns.length > 0) {
            final String sep = " ";
            final String comma = ",";

            TableColumn tableColumn = tableColumns[0];
            builder.append(tableColumn.getFieldName());
            builder.append(sep);
            builder.append(tableColumn.getFieldType());

            for (int i = 1; i < tableColumns.length; i++) {
                tableColumn = tableColumns[i];
                builder.append(comma);
                builder.append(tableColumn.getFieldName());
                builder.append(sep);
                builder.append(tableColumn.getFieldType());
            }
        }
        builder.append(");");

        db.execSQL(builder.toString());
    }

    public static Uri groupBy(Uri uri, String groupBy) {
        return uri.buildUpon().appendQueryParameter(QUERYPARAM_GROUPBY, groupBy).build();
    }

    public static Uri having(Uri uri, String having) {
        return uri.buildUpon().appendQueryParameter(QUERYPARAM_HAVING, having).build();
    }

    public static Uri limit(Uri uri, int limit) {
        return uri.buildUpon().appendQueryParameter(QUERYPARAM_LIMIT, String.valueOf(limit)).build();
    }

    public static Uri unionAll(Uri uri, UnionQueryParams... unionAll) {
        if (unionAll == null || unionAll.length == 0) {
            return uri;
        }
        Uri.Builder builder = uri.buildUpon();
        for (UnionQueryParams union : unionAll) {
            builder.appendQueryParameter(QUERYPARAM_UNIONALL, union.toString());
        }
        return builder.build();
    }

    public static Uri noNeedNotify(Uri uri) {
        return uri.buildUpon().appendQueryParameter(QUERYPARAM_NEEDNOTIFY, String.valueOf(false)).build();
    }

    public static Uri join(Uri leftUri, Uri rightUri, JoinType joinType, String leftColumn, String rightColumn) {
        String joinStr = rightUri.buildUpon().appendQueryParameter(QUERYPARAM_JOINTYPE, joinType.name())
                .appendQueryParameter(QUERYPARAM_JOINLEFTCOLUMN, leftColumn)
                .appendQueryParameter(QUERYPARAM_JOINRIGHTCOLUMN, rightColumn).build().toString();
        return leftUri.buildUpon().appendQueryParameter(QUERYPARAM_JOIN, joinStr).build();
    }

    static QueryParams getQueryParams(Uri uri) {
        return getQueryParams(uri, null, null, null, null);
    }

    static QueryParams getQueryParams(Uri uri, String selection, String[] selectionArgs) {
        return getQueryParams(uri, selection, null, null, selectionArgs);
    }

    static QueryParams getQueryParams(Uri uri, String selection, String orderBy, String[] projection, String[] selectionArgs) {
        SQLiteTable.TableNameAndDir tableNameDir = SQLiteTable.getTableNameDir(uri);
        if (tableNameDir == null) {
            throw new IllegalArgumentException(String.format("The uri '%1$s' is not supported by this ContentProvider", uri));
        }
        String tableName = tableNameDir.tableName;
        String where;
        String having = uri.getQueryParameter(ContentProviderUtil.QUERYPARAM_HAVING);
        String limit = uri.getQueryParameter(ContentProviderUtil.QUERYPARAM_LIMIT);
        String groupBy = uri.getQueryParameter(ContentProviderUtil.QUERYPARAM_GROUPBY);
        List<String> unionAllStr = uri.getQueryParameters(ContentProviderUtil.QUERYPARAM_UNIONALL);
        UnionQueryParams[] unionAll = UnionQueryParams.listToArray(unionAllStr);
        final String needNotifyParam = uri.getQueryParameter(ContentProviderUtil.QUERYPARAM_NEEDNOTIFY);
        boolean needNotify = needNotifyParam == null || Boolean.parseBoolean(needNotifyParam);
        List<String> joins = uri.getQueryParameters(ContentProviderUtil.QUERYPARAM_JOIN);
        if (joins != null && joins.size() > 0) {
            for (String join : joins) {
                if (join != null && join.length() > 0) {
                    Uri joinUri = Uri.parse(join);
                    SQLiteTable.TableNameAndDir joinTable = SQLiteTable.getTableNameDir(joinUri);
                    if (joinTable == null) {
                        throw new IllegalArgumentException(String.format("The join uri '%1$s' is not supported by this ContentProvider", joinUri));
                    }
                    String joinType = ContentProviderUtil.JoinType.valueOf(joinUri.getQueryParameter(ContentProviderUtil.QUERYPARAM_JOINTYPE)).joinString;
                    String leftColumn = joinUri.getQueryParameter(ContentProviderUtil.QUERYPARAM_JOINLEFTCOLUMN);
                    String rightColumn = joinUri.getQueryParameter(ContentProviderUtil.QUERYPARAM_JOINRIGHTCOLUMN);
                    tableName += String.format(" %2$s %3$s ON %1$s.%4$s = %3$s.%5$s ",
                            tableNameDir.tableName, joinType, joinTable.tableName, leftColumn, rightColumn);
                }
            }
        }
        if (tableNameDir.isDir) {
            where = selection;
        } else {
            String idSelection = String.format("%s = %s ", BaseColumns._ID, uri.getLastPathSegment());
            where = TextUtils.isEmpty(selection) ? idSelection : String.format(" %s and %s", idSelection, selection);
        }
        return new QueryParams(tableName, where, orderBy, having, groupBy, limit, projection, selectionArgs, unionAll, needNotify);
    }
}
