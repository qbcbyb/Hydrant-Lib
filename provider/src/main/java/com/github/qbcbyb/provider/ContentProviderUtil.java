package com.github.qbcbyb.provider;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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
}
