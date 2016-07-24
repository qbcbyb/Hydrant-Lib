package com.github.qbcbyb.provider;

import java.io.Serializable;
import java.util.List;

public final class UnionQueryParams implements Serializable {

    public static final String NULL = "null";
    public final String selection;
    public final String[] selectionArgs;

    public UnionQueryParams(String selection) {
        this(selection, null);
    }

    public UnionQueryParams(String selection, String[] selectionArgs) {
        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(selection == null ? NULL : selection);
        sb.append("@");
        if (selectionArgs != null && selectionArgs.length > 0) {
            sb.append(selectionArgs[0]);
            for (int i = 1; i < selectionArgs.length; i++) {
                sb.append(",");
                sb.append(selectionArgs[i]);
            }
        } else {
            sb.append(NULL);
        }
        return sb.toString();
    }

    public static UnionQueryParams[] listToArray(List<String> str) {
        if (str == null || str.size() == 0) return null;
        UnionQueryParams[] unionQueryParamsArray = new UnionQueryParams[str.size()];
        for (int i = 0; i < unionQueryParamsArray.length; i++) {
            unionQueryParamsArray[i] = fromString(str.get(i));
        }
        return unionQueryParamsArray;
    }

    public static UnionQueryParams fromString(String str) {
        if (str == null || str.length() == 0 || str.equals(NULL)) return null;
        final String[] split = str.split("@");
        final String selection = split[0];
        final String args = split[1];
        final String[] selectionArgs = args == null || args.length() == 0 || args.equals(NULL) ? null : args.split(",");
        return new UnionQueryParams(selection == null || selection.length() == 0 || selection.equals(NULL) ? null : selection, selectionArgs);
    }
}