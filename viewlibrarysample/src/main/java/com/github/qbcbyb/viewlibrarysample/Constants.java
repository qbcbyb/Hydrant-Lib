package com.github.qbcbyb.viewlibrarysample;

import java.util.Locale;
import java.util.Random;

/**
 * Created by qbcby on 2016/6/14.
 */
public class Constants {
    private static String getImageUrl(int index) {
        return String.format(Locale.US, "http://lorempixel.com/400/200/sports/%1d/", index);
    }

    private static String[] IMAGE_URLS;

    public static String[] getImageUrls() {
        if (IMAGE_URLS == null) {
            IMAGE_URLS = new String[100];
            Random random = new Random(System.currentTimeMillis());
            for (int i = 0; i < 100; i++) {
                IMAGE_URLS[i] = getImageUrl(random.nextInt(10) + 1);
            }
        }
        return IMAGE_URLS;
    }

}
