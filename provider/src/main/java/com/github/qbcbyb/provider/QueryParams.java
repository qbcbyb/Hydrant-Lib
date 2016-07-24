package com.github.qbcbyb.provider;

import java.util.Arrays;

public final class QueryParams {

    final String tableName;
    final String selection;
    final String orderBy;
    final String having;
    final String groupBy;
    final String limit;
    final String[] projection;
    final String[] selectionArgs;
    final UnionQueryParams[] unionAll;
    final boolean needNotify;

    public QueryParams(String tableName, String selection, String orderBy, String having, String groupBy, String limit, String[] projection, String[] selectionArgs, UnionQueryParams[] unionAll, boolean needNotify) {
        this.tableName = tableName;
        this.selection = selection;
        this.orderBy = orderBy;
        this.having = having;
        this.groupBy = groupBy;
        this.limit = limit;
        this.projection = projection;
        this.selectionArgs = selectionArgs;
        this.unionAll = unionAll;
        this.needNotify = needNotify;
    }

    public String toString() {
        return "QueryParams(tableName=" + this.tableName + ", selection=" + this.selection + ", orderBy=" + this.orderBy + ", having=" + this.having + ", groupBy=" + this.groupBy + ", limit=" + this.limit + ", projection=" + Arrays.toString(this.projection) + ", selectionArgs=" + Arrays.toString(this.selectionArgs) + ", unionAll=" + Arrays.toString(unionAll) + ")";
    }
}