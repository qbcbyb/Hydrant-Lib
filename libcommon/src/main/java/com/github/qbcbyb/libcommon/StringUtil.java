package com.github.qbcbyb.libcommon;


import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isEmpty(String str) {
        if ("".equals(str) || str == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String str) {
        return str != null && (!"".equals(str));
    }

    public static boolean isPhoneNumber(String str) {
        return Pattern.matches("^[1-9]\\d{10}$", str);
    }

    public static boolean isNotPhoneNumber(String str) {
        return !isPhoneNumber(str);
    }

    public static String join(CharSequence delimiter, Iterable tokens) {
        if (tokens == null) {
            return null;
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (token != null) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    builder.append(delimiter);
                }
                builder.append(token);
            }
        }
        return builder.toString();
    }

    public static String join(CharSequence delimiter, Object[] tokens) {
        if (tokens == null) {
            return null;
        }
        if (tokens.length == 0) {
            return "";
        }
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder builder = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (token != null) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    builder.append(delimiter);
                }
                builder.append(token);
            }
        }
        return builder.toString();
    }

}
