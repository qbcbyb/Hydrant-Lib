package com.github.qbcbyb.provider;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by qbcby on 2016/4/19.
 */
public abstract class ContentProvider extends android.content.ContentProvider {

    public static boolean DEBUG;
    private static SQLiteOpenHelper sqliteOpenHelper;

    public abstract SQLiteOpenHelper getSQLiteOpenHelper(Context context);

    @Override
    public boolean onCreate() {
        sqliteOpenHelper = getSQLiteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return SQLiteTable.getUriType(uri);
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) {
        final Context context = getContext();
        if (context == null) {
            throw new NullPointerException("context must not be null!");
        }
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int numOperations = operations.size();
            ContentProviderResult[] results = new ContentProviderResult[numOperations];
            int i = 0;
            for (ContentProviderOperation operation : operations) {
                results[i] = operation.apply(this, results, i);
                if (DEBUG) {
                    Log.d("CP-applyBatch:" + i, operation.toString());
                }
                if (operation.isYieldAllowed()) {
                    db.yieldIfContendedSafely();
                }
                i++;
            }
            db.setTransactionSuccessful();
            final ContentResolver contentResolver = context.getContentResolver();
            for (ContentProviderOperation operation : operations) {
                contentResolver.notifyChange(operation.getUri(), null);
            }
            return results;
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return new ContentProviderResult[0];
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final Context context = getContext();
        if (context == null) {
            throw new NullPointerException("context must not be null!");
        }
        QueryParams queryParams = ContentProviderUtil.getQueryParams(uri);
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
        int res = 0;
        db.beginTransaction();
        try {
            for (ContentValues v : values) {
                long id = db.insert(queryParams.tableName, null, v);
                db.yieldIfContendedSafely();
                if (id != -1L) {
                    res++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (res != 0 && queryParams.needNotify) {
            context.getContentResolver().notifyChange(uri, null);
        }
        if (DEBUG) {
            Log.d("CP-bulkInsert:" + res, queryParams.toString());
        }
        return res;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final Context context = getContext();
        if (context == null) {
            throw new NullPointerException("context must not be null!");
        }
        QueryParams queryParams = ContentProviderUtil.getQueryParams(uri);
        long rowId = sqliteOpenHelper.getWritableDatabase().insertOrThrow(queryParams.tableName, null, values);
        if (rowId == -1L) return null;
        if (queryParams.needNotify) {
            context.getContentResolver().notifyChange(uri, null);
        }
        if (DEBUG) {
            Log.d("CP-insert:" + rowId, queryParams.toString());
        }
        return uri.buildUpon().appendEncodedPath(Long.toString(rowId)).build();
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final Context context = getContext();
        if (context == null) {
            throw new NullPointerException("context must not be null!");
        }
        QueryParams queryParams = ContentProviderUtil.getQueryParams(uri, selection, selectionArgs);
        int res = sqliteOpenHelper.getWritableDatabase().update(queryParams.tableName, values, queryParams.selection, selectionArgs);
        if (res != 0 && queryParams.needNotify) {
            context.getContentResolver().notifyChange(uri, null);
        }
        if (DEBUG) {
            Log.d("CP-update:" + res, queryParams.toString());
        }
        return res;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final Context context = getContext();
        if (context == null) {
            throw new NullPointerException("context must not be null!");
        }
        QueryParams queryParams = ContentProviderUtil.getQueryParams(uri, selection, sortOrder, projection, selectionArgs);
        final UnionQueryParams[] unionAll = queryParams.unionAll;//UnionQueryParams[]
        Cursor res;
        if (unionAll == null || unionAll.length == 0) {
            res = sqliteOpenHelper.getReadableDatabase().query(queryParams.tableName,
                    queryParams.projection, queryParams.selection, queryParams.selectionArgs, queryParams.groupBy,
                    queryParams.having, queryParams.orderBy, queryParams.limit);
        } else {
            ArrayList<String> argsList = new ArrayList<>();
            ArrayList<String> whereList = new ArrayList<>();
            if (queryParams.selectionArgs != null) {
                Collections.addAll(argsList, queryParams.selectionArgs);
            }
            whereList.add(SQLiteQueryBuilder.buildQueryString(false, queryParams.tableName, queryParams.projection,
                    queryParams.selection, null, null, null, null));
            for (int i = 0; i < unionAll.length; i++) {
                UnionQueryParams unionItem = unionAll[i];//UnionQueryParams
                if (unionItem.selectionArgs != null) {
                    argsList.addAll(Arrays.asList(unionItem.selectionArgs));
                }
                if (i == unionAll.length - 1) {
                    whereList.add(SQLiteQueryBuilder.buildQueryString(false, queryParams.tableName, queryParams.projection,
                            unionItem.selection, queryParams.groupBy, queryParams.having, queryParams.orderBy, queryParams.limit));
                } else {
                    whereList.add(SQLiteQueryBuilder.buildQueryString(false, queryParams.tableName, queryParams.projection,
                            unionItem.selection, null, null, null, null));
                }
            }
            res = sqliteOpenHelper.getReadableDatabase().rawQuery(
                    TextUtils.join(" UNION ALL ", whereList),
                    argsList.toArray(new String[argsList.size()]));
        }
        res.setNotificationUri(context.getContentResolver(), uri);
        if (DEBUG) {
            Log.d("CP-query:${res.count}", queryParams.toString());
        }
        return res;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final Context context = getContext();
        if (context == null) {
            throw new NullPointerException("context must not be null!");
        }
        QueryParams queryParams = ContentProviderUtil.getQueryParams(uri, selection, selectionArgs);
        int res = sqliteOpenHelper.getWritableDatabase().delete(queryParams.tableName, queryParams.selection, selectionArgs);
        if (res != 0 && queryParams.needNotify) {
            context.getContentResolver().notifyChange(uri, null);
        }
        if (DEBUG) {
            Log.d("CP-delete:" + res, queryParams.toString());
        }
        return res;
    }
}
