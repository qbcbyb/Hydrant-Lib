package com.github.qbcbyb.provider;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

/**
 * Created by qbcby on 2016/4/20.
 */
public class UnionQueryParamsTest {

    @org.junit.Test
    public void testToString() throws Exception {
        UnionQueryParams params = new UnionQueryParams("fieldName=?");
        String str = params.toString();
        System.out.println(str);
        UnionQueryParams actual = UnionQueryParams.fromString(str);
        assertEquals(params.selection, actual.selection);
        assertEquals(params.selectionArgs, actual.selectionArgs);

        params = new UnionQueryParams("fieldName=?", new String[]{"dd","c"});
        str = params.toString();
        System.out.println(str);
        actual = UnionQueryParams.fromString(str);
        assertEquals(params.selection, actual.selection);
        assertEquals(Arrays.toString(params.selectionArgs), Arrays.toString(actual.selectionArgs));
    }

    @org.junit.Test
    public void testArrayToString() throws Exception {
        UnionQueryParams[] paramsArray = new UnionQueryParams[10];
        Arrays.fill(paramsArray, new UnionQueryParams("fieldName=?", new String[]{"dd", "cc"}));
        System.out.println(UnionQueryParams.arrayToString(paramsArray));
    }

    @org.junit.Test
    public void testStringToArray() throws Exception {
        String str = "fieldName=?@dd,cc#fieldName=?@dd#fieldName=?@dd,cc#fieldName=?@dd,cc#fieldName=?@dd,cc#fieldName=?@dd,a,cc#fieldName=?@dd,cc#fieldName=?@dd,cc#fieldName=?@dd,cc#fieldName=?@dd,cc";
        final UnionQueryParams[] unionQueryParamses = UnionQueryParams.stringToArray(str);
        assertEquals("fieldName=?", unionQueryParamses[2].selection);
        assertEquals("dd", unionQueryParamses[3].selectionArgs[0]);
        assertEquals(1, unionQueryParamses[1].selectionArgs.length);
        assertEquals("cc", unionQueryParamses[4].selectionArgs[1]);
        assertEquals(3, unionQueryParamses[5].selectionArgs.length);
    }
}