package com.attila.samplevolley.application;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.attila.samplevolley.BitmapLruCache;


public class VolleyHandler {

	public static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;
	public static int cacheSize;
	
	public static void intializeRequestQueue(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
		 int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
          cacheSize = maxMemory / 8;
         mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
	}
	
	public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue null");
        }
    }
	
	public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader null");
        }
    }
	
}
